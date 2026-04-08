pipeline {
    agent any
    
    tools{
    maven "maven3.9.6"
    }
   
   environment {
        buildNumber = "${env.BUILD_NUMBER}"
    }
   
    stages {
        stage('CheckOutCode') {
            steps {
             git 'https://github.com/playdevops-co/spring-boot-mongo-docker.git'
            }
        }
        stage('Build'){
            steps {
                sh "mvn clean package"
            }
        }
        stage('BuildDockerImage'){
            steps {
                sh "docker build -t raavangokul/spring-boot:${buildNumber} ."
            }
        }
        stage('DockerLoginAndPush'){
            steps {
                withCredentials([string(credentialsId: 'DockerHub', variable: 'DockerHub')]) {
                 sh "docker login -u raavangokul -p ${DockerHub}"
                }
                sh "docker push raavangokul/spring-boot:${buildNumber}"
            }
        }
        stage('UpdateTheImageTagInComposeFile'){
            steps{
                sh "sed -i 's|raavangokul/spring-boot:GOKUL|raavangokul/spring-boot:${buildNumber}|' docker-compose.yaml"
            }
        }
        stage('DockerContainerDeploymentOnQaEnv'){
            steps {
                sshagent(['Docker_creds']) {
                    
                    sh "scp -o StrictHostKeyChecking=no docker-compose.yaml  ubuntu@172.31.17.200:/home/ubuntu/docker-compose.yaml"
                    sh "ssh -o StrictHostKeyChecking=no  ubuntu@172.31.17.200  docker-compose down" 
                    sh "ssh -o StrictHostKeyChecking=no  ubuntu@172.31.17.200  docker-compose up -d" 
                }
            }
        }
    }
}
