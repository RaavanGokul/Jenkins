node{

stage('CheckOutCode'){
git branch: 'dev', credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/nodejs-app-mss.git'
}
stage('Build'){
nodejs(nodeJSInstallationName: 'nodejs21.50'){
sh "npm install"
}
}
stage('SonarQubeTest'){
node
}
}