pipepline{
    agent any
    tools{
        maven "3.9.6"
    }
    stages {
        stage('CodeCheckOut'){
            steps{
                git credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/maven-web-application.git'
            }
        }
        stage('RunStagesInParallel'){
            steps{
                parallel(
                    RunUnitTestCases: {
                        sh "mvn clean test"
                    },
                    Build: {
                        sh "mvn clean package"
                    }
                )
                    }
            }
        }
    }