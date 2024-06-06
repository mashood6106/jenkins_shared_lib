def call(Map pipelineParams){
def projectName = pipelineParams.ecrRepoName

pipeline {
 agent any
   tools {
    jdk 'java8'
  }
  environment {
    registry = "mashood6106/${projectName}"
    registryCredential = 'dockerhub_credentials	'
    dockerImage = ''
  }
  stages {
   stage('build'){
             
             steps{
                 sh 'mvn package'
             }
         }
    stage('Building image') {
      steps{
        script {
          dockerImage = docker.build registry + ":v$BUILD_NUMBER"
        }
      }
    }
    stage('push image') {
      steps{
        script {
          docker.withRegistry( '', registryCredential ) {
            dockerImage.push()
          }
        }
      }
    }
    stage('Remove old docker image') {
      steps{
        sh "docker rmi $registry:v$BUILD_NUMBER"
      }
    }
  }
}
}
