

Paralle Execution
-----------------
/* Parallel execution */
stage('BuildandSoanrQubeReport') {
  steps {
    parallel(
      Build: {
        sh "mvn clean package"
      },
      SonarQubeReportExecution: {
        sh "mvn sonar:sonar"
      }
    )
  }
}



Build With Parameters
---------------------
pipeline {
    agent any
    
    parameters {
  choice choices: ['development', 'stage', 'master'], description: 'Branch Name', name: 'branchName'
}

    stages {
        stage('CheckOutCode') {
            steps {
              script {
                    
                        git branch: "${params.branchName}", credentialsId: '12993250-1ff3-40a0-9978-794e74dcf712', url: 'https://github.com/MithunTechnologiesDevOps/maven-web-application.git'
                    
            }
        }
		}
        

  }//Stages Closing
}//Pipeline Closing

Write a pipeline script to create files and dirs
------------------------------------------------
pipeline {
    agent any
	
    stages {
        stage('CreateFilesandDirs') {
            steps {
                  sh 'touch bhaskar.txt'
                  dir('/tmp/bhaskar/') {
				       sh 'touch bhaskar.txt'
                       sh 'touch devops.txt'
                       sh 'touch mithun.txt'
                  }
            }
        }
    }
}



Upstream and down stream Job
-----------------------

wallmart-dev --> Upstream Project for qa and prod

wallmart-qa --> Upstream project for prod

wallmart-prod

a)Freestyle Projects

b)Pipeline Script Projects

pipeline {
    agent any

    stages {
        stage ('StartingDownStreamJob') {
            steps{
             build job: 'wallmart-dev'
            }
}
    }
}







Environment Section
-------------------

pipeline {
    agent any
    
    parameters {
  choice choices: ['Dev', 'QA', 'Prod'], description: 'Environment Name', name: 'environment'
}

environment {
  DEPLOY_ENV = "${params.environment}"
}

tools{
    maven "maven3.8.4"
}

    stages {
        stage('CheckOutCode') {
            steps {
              script {
                    if("${DEPLOY_ENV}" == 'Dev'){
                        def branchName = 'development'
                        git branch: "${branchName}", credentialsId: '12993250-1ff3-40a0-9978-794e74dcf712', url: 'https://github.com/MithunTechnologiesDevOps/maven-web-application.git'
                    } else if("${DEPLOY_ENV}" == 'QA'){
                       def branchName = 'stage'
                       git branch: "${branchName}", credentialsId: '12993250-1ff3-40a0-9978-794e74dcf712', url: 'https://github.com/MithunTechnologiesDevOps/maven-web-application.git'
                    }else if("${DEPLOY_ENV}" == 'Prod'){
                      def branchName = 'master'
                      git branch: "${branchName}", credentialsId: '12993250-1ff3-40a0-9978-794e74dcf712', url: 'https://github.com/MithunTechnologiesDevOps/maven-web-application.git'
                    }
                 }
            }
        }
        /*
        stage('Build'){
            steps{
               sh "mvn clean package"
            }
        }
        */
      
    }//Stages Close
}//Pipeline Close














