pipeline {
    agent any
    tools {
        maven 'M398'
    }
    stages {
        stage('Echo Versions') {
            steps {
                bat 'echo Print Maven Version'
                bat 'echo mvn -version'
            }
        }
        
        stage('Build') {
            steps {
                // git branch: 'main', url: 'https://github.com/Nicholauszoom/bayport-bank-mandate-backend.git'
                bat 'mvn clean package -DskipTests=true'
            }
        }
        
        stage ('Unit Test') {
            steps {
                bat 'mvn test'
            }
        }
      
    }
}
