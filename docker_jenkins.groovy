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
             git 'https://github.com/MithunTechnologiesDevOps/maven-web-application.git'
            }
        }
        stage('Build'){
            steps {
                sh "mvn clean package"
            }
        }
        stage('BuildDockerImage'){
            steps {
                sh "docker build -t raavangokul/maven-web-application:appV${buildNumber} ."
            }
        }
        stage('DockerLoginAndPush'){
            steps {
                withCredentials([string(credentialsId: 'DockerHub', variable: 'DockerHub')]) {
                 sh "docker login -u raavangokul -p ${DockerHub}"
                }
                sh "docker push raavangokul/maven-web-application:appV${buildNumber}"
            }
        }
         stage('DockerContainerDeploymentOnQaEnv'){
            steps {
                sshagent(['Docker_QA_SSH']) {
                    sh "ssh -o StrictHostKeyChecking=no  ubuntu@172.31.6.125  docker run --name mavenwebappg1 -d -p 8080:8080 raavangokul/maven-web-application:appV${buildNumber}" 
                }
            }
        }
    }
}
