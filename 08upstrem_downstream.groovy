pipeline{

agent any

tools{
    maven "3.9.6"
}

stages{
    stage('CodeCheckOut'){
        steps{
         git branch: 'development', credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/maven-web-application.git'   
        }
    }
    stage('Build'){
        steps{
            sh "mvn clean package"
        }
    }
    stage('TriggerJob'){
        stepsP{
         Build Job: 'uat-pipe'
        }
    }
}
}