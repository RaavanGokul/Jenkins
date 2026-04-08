node{


stage('CheckoutCode'){
git credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/nodejs-app-mss.git'
}

stage('Build'){
nodejs(nodeJSInstallationName: 'nodejs21.5.0'){
sh "npm install"
}
}

stage('SonarReport'){
nodejs(nodeJSInstallation: 'nodejs21.5.0'){
sh "npm run sonar"
}
}
/*
stage('UplodArtifactIn  toNexus'){
nodejs(nodeJSInstallation: 'nodejs21.5.0'){
sh "npm deploy"
}
}
}
*/
stage('RunNodeJs'){
sh "npm start"
}
}

/*

yum install nodejs -y

node -v

npm -v

*/