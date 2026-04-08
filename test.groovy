pipeline {

agent any

tools {
  maven 'maven3.9.6'
}

parameters {
  choice choices: ['development', 'master', 'uat', 'qa'], name: 'BranchName'
  string defaultValue: 'raavangokul', description: 'Change the PersonName', name: 'PersonName'
}

stages{

    stage('CheckOutCode'){
        steps{
             git branch: "${params.BranchName}" credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/maven-web-application.git'
             sh "echo The PersonName is: ${params.PersonName}"
            }
    }
    stage('Build'){
        steps{
            sh "mvn clean package"
            }
    }
}//stages

}//pipeline