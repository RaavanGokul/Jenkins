@Library('gpsharedlibs') _

pipeline{

agent any

tools {
  maven 'maven3.9.6'
}

options {
  timestamps()
  buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '1', numToKeepStr: '3')
}

stages {
  stage('CodeCheckOut') {
    steps {
    git credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/maven-web-application.git'
    sh "echo This Person name is: ${params.PersonName}"
    }
  }

  stage('Build') {
    steps {
    sh "mvn clean package"
    }
  }


}//stages

post {
  success {
    sendSlackNotifications(currentBuild.result)
  }
  failure {
    sendSlackNotifications(currentBuild.result)
  }
}

}//Pipeline