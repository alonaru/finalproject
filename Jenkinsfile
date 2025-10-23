pipeline {
    agent any
    
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKERHUB_USERNAME = 'alonaru'
        IMAGE_NAME = "${DOCKERHUB_USERNAME}/flask-aws-monitor"
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Clone Repository') {
            steps {
                echo 'Cloning repository from GitHub...'
                git branch: 'main', url: 'https://github.com/alonaru/finalproject.git'
            }
        }
        
        stage('Parallel Checks') {
            parallel {
                stage('Linting') {
                    steps {
                        echo '=== Running Linting Checks ==='
                        sh '''
                            echo "Running Flake8 for Python linting..."
                            echo "Python linting completed (mock)"
                            echo "Running ShellCheck for shell scripts..."
                            echo "Shell script linting completed (mock)"
                            echo "Running Hadolint for Dockerfile..."
                            echo "Dockerfile linting completed (mock)"
                        '''
                    }
                }
                
                stage('Security Scan') {
                    steps {
                        echo '=== Running Security Scans ==='
                        sh '''
                            echo "Running Bandit for Python security scanning..."
                            echo "Python security scan completed (mock)"
                            echo "Running Trivy for container security..."
                            echo "Container security scan completed (mock)"
                        '''
                    }
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo "=== Building Docker Image ==="
                script {
                    sh """
                        docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                        echo "Docker image built successfully"
                    """
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                echo "=== Pushing to Docker Hub ==="
                script {
                    sh """
                        echo \$DOCKERHUB_CREDENTIALS_PSW | docker login -u \$DOCKERHUB_CREDENTIALS_USR --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        docker push ${IMAGE_NAME}:latest
                        echo "Images pushed successfully"
                        docker logout
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo "Pipeline completed successfully!"
            echo "Image: ${IMAGE_NAME}:${IMAGE_TAG}"
        }
        failure {
            echo "Pipeline failed!"
        }
        always {
            sh 'docker system prune -f || true'
        }
    }
}
EOFcat > Jenkinsfile << 'EOF'
pipeline {
    agent any
    
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKERHUB_USERNAME = 'alonaru'
        IMAGE_NAME = "${DOCKERHUB_USERNAME}/flask-aws-monitor"
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Clone Repository') {
            steps {
                echo 'Cloning repository from GitHub...'
                git branch: 'main', url: 'https://github.com/alonaru/finalproject.git'
            }
        }
        
        stage('Parallel Checks') {
            parallel {
                stage('Linting') {
                    steps {
                        echo '=== Running Linting Checks ==='
                        sh '''
                            echo "Running Flake8 for Python linting..."
                            echo "Python linting completed (mock)"
                            echo "Running ShellCheck for shell scripts..."
                            echo "Shell script linting completed (mock)"
                            echo "Running Hadolint for Dockerfile..."
                            echo "Dockerfile linting completed (mock)"
                        '''
                    }
                }
                
                stage('Security Scan') {
                    steps {
                        echo '=== Running Security Scans ==='
                        sh '''
                            echo "Running Bandit for Python security scanning..."
                            echo "Python security scan completed (mock)"
                            echo "Running Trivy for container security..."
                            echo "Container security scan completed (mock)"
                        '''
                    }
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo "=== Building Docker Image ==="
                script {
                    sh """
                        docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                        echo "Docker image built successfully"
                    """
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                echo "=== Pushing to Docker Hub ==="
                script {
                    sh """
                        echo \$DOCKERHUB_CREDENTIALS_PSW | docker login -u \$DOCKERHUB_CREDENTIALS_USR --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        docker push ${IMAGE_NAME}:latest
                        echo "Images pushed successfully"
                        docker logout
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo "Pipeline completed successfully!"
            echo "Image: ${IMAGE_NAME}:${IMAGE_TAG}"
        }
        failure {
            echo "Pipeline failed!"
        }
        always {
            sh 'docker system prune -f || true'
        }
    }
}
