pipeline{
  
   agent{
      label 'ansiterra'
   }

   environment{
    AWS_EC2PRIVATEKEY=credentials('ec2instance-private-key')
   }

   stages{
    stage('checkoutCode'){
        steps{
            git branch: 'main', credentialsId: '039d9e6d-9b4b-4ab5-87da-663e8850d508', url: 'https://github.com/playdevops-co/jenkins_ansible_dynamic-inv_terraform.git'
        }
    }
    
    stage('ExecuteTerraformScript'){
     steps{
       sh "terraform -chdir=terraform_scripts init"
       sh "terraform -chdir=terraform_scripts apply -auto-approve"
     }
    }
    stage('RunAnsiblePlaybook'){
      steps{
       sh "ansible-inventory --graph -i inventory/aws_ec2.yml"
       sh "ansible-playbook -i inventory/aws_ec2.yml  playbooks/installhttpd.yml --private-key=$AWS_EC2PRIVATEKEY --ssh-common-args='-o StrictHostKeyChecking=no' -l tomcatappserver "
     }
   }
   }
}