pipeline{
  
   agent{
      lable 'ansible-master'
   }

   environment{
    AWS_EC2PRIVATEKEY=credentials('ec2instance-private-key')
   }

   stages{
    stage('checkoutCode'){
        steps{
            git branch: 'main', credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/jenkins-with-ansible.git'
        }
    }
    stage('RunAnsiblePlaybook'){
      steps{
       sh "ansible-playbook -i inventory/static.hosts  playbooks/ping.yml --private-key=$AWS_EC2PRIVATEKEY --ssh-common-args='-o StrictHostKeyChecking=no'"
     }
   }
   }
}