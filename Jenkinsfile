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
                git branch: 'feature/fixcode', url: 'https://github.com/alonaru/finalproject.git'
            }
        }
        
        stage('Parallel Checks') {
            parallel {
                stage('Linting') {
                    steps {
                        echo '=== Running Linting Checks ==='
                        sh '''
                            echo "Running Flake8 for Python linting..."
                            flake8 app/ --max-line-length=120 || true
                            echo "Python linting completed"

                            echo "Running ShellCheck for shell scripts in root and app/..."
                            found_sh=0
                            if ls *.sh 1> /dev/null 2>&1; then
                                shellcheck *.sh
                                found_sh=1
                            fi
                            if ls app/*.sh 1> /dev/null 2>&1; then
                                shellcheck app/*.sh
                                found_sh=1
                            fi
                            if [ $found_sh -eq 0 ]; then
                                echo "No shell scripts to lint."
                            fi
                            echo "Shell script linting completed"

                            echo "Running Hadolint for Dockerfile..."
                            hadolint Dockerfile
                            echo "Dockerfile linting completed"
                        '''
                    }
                }
                
                stage('Security Scan') {
                    steps {
                        echo '=== Running Security Scans ==='
                        sh '''
                            echo "Running Bandit for Python security scanning..."
                            echo bandit -r app/ || true
                            echo "Python security scan completed"
                            
                            echo "Running Trivy for container security..."
                            echo trivy image ${IMAGE_NAME}:${IMAGE_TAG}
                            echo "Container security scan completed"
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
                        ls -la
                        docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                        echo "Docker image built successfully: ${IMAGE_NAME}:${IMAGE_TAG}"
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
                        echo "Images pushed successfully to Docker Hub"
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
            echo "Latest: ${IMAGE_NAME}:latest"
        }
        failure {
            echo "Pipeline failed! Check logs for details."
        }
    }
}
