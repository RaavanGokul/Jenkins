pipeline{

agent any

tools {
  maven 'maven3.9.6'
}

options {
  timestamps()
  buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '1', numToKeepStr: '3')
}
parameters {
  choice choices: ['master', 'development', 'qa', 'uat'], description: 'BranchNames', name: 'BranchName'
  string defaultValue: 'raavangokul', description: 'Change the person name...', name: 'PersonaName'
}

stages {
  stage('CodeCheckOut') {
    steps {
    git branch: "${params.BranchName}", credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/maven-web-application.git'
    sh "echo This Person name is: ${params.PersonName}"
    }
  }

  stage('Build') {
    steps {
    sh "mvn clean package"
    }
  }

  stage('SonarQubeTest') {
    steps {
    sh "mvn clean sonar:sonar"
    }
  }

  stage('UpoadArtifactIntoNexus') {
    steps {
    sh "mvn deploy"
    }
  }

  stage('DeployIntotomcatServer') {
    steps {
    sshagent(['abb1bf1a-35b9-4e66-becf-88e037dd888d']) {
    sh "scp -o StrictHostKeyChecking=no target/maven-web-application.war  ec2-user@172.31.3.186:/opt/tomcat9/webapps/"
     }

    }
  }

}//stages

post {
  
  success{
   mail bcc: '', body: '''Build Over - Success..

  Regards,
  Teradata Software Solutions,
  9003403396''', cc: 'raavangokuldd@gmail.com', from: '', replyTo: '', subject: 'Build over!...', to: 'raavangokuldd@gmail.com'
   
  }
  failure{
  mail bcc: '', body: '''Build Over - Failure..

  Regards,
  Teradata Software Solutions,
  9003403396''', cc: 'raavangokuldd@gmail.com', from: '', replyTo: '', subject: 'Build over!...', to: 'raavangokuldd@gmail.com'
   
   
  }
}
}//Pipeline