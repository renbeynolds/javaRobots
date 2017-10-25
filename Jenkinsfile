#!groovy

library 'pipeline-libraries@email'

timestamps {

    postBuildEmail('breynolds@mitre.org') {
    
        node {
    
            stage('Checkout') {
                deleteDir()
                checkout scm
            }
    
            stage('Compile') {
                sh './gradlew shadowJar'
            }
    
            stage('Unit Test') {
                try {
                    sh './gradlew test'
                } catch(e) {
                    throw(e)
                } finally {
                    junit '**/TEST-*.xml'
                    jacoco()
                }
            }
    
            stage('OWASP Dependency Check') {
                sh './gradlew dependencyCheckAnalyze'
                dependencyCheckPublisher canComputeNew: false
            }
    
            stage('SonarQube Analysis') {
                sh './gradlew -Dsonar.host.url=http://bluemoon-sonarqube.aocws.test sonarqube'
            }
    
        }
    
    }

}
