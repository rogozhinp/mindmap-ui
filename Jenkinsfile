pipeline {
    agent any

    tools {
        jdk 'java 21'
    }

    environment {
        GRADLE_OPTS = '-Dorg.gradle.daemon=false'
    }

    options {
        timestamps()
    }

    triggers {
        pollSCM('H/2 * * * *')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                sh 'chmod +x gradlew'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean assemble'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test jacocoTestReport'
            }
        }

        stage('JaCoCo Report') {
            steps {
                sh './gradlew jacocoTestReport'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('Local SonarQube') {
                    sh './gradlew sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        always {
            junit 'build/test-results/jvmTest/**/*.xml'
            archiveArtifacts artifacts: 'build/reports/**', allowEmptyArchive: true
        }
        failure {
            echo 'Pipeline failed'
        }
        success {
            echo 'Pipeline succeeded'
        }
    }
}