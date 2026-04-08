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
                sh "docker build -t raavangokul/spring-boot-mongo:${buildNumber} ."
            }
        }
        stage('DockerLoginAndPush'){
            steps {
                withCredentials([string(credentialsId: 'DockerHub', variable: 'DockerHub')]) {
                 sh "docker login -u raavangokul -p ${DockerHub}"
                }
                sh "docker push raavangokul/spring-boot-mongo:${buildNumber}"
            }
        }
        stage('DeployToK8s'){
            steps{
                sh "sed -i 's|raavangokul/spring-boot-mongo:GOKUL|raavangokul/spring-boot-mongo:${buildNumber}|' spring-boot-mongo-gk"
                sh "kubectl apply -f spring-boot-mongo-gk"
            }
        }
    }
}
