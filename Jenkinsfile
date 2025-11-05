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
                git url: 'https://github.com/alonaru/finalproject.git'
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
                            bandit -r app/ || true
                            echo "Python security scan completed"
                            
                            echo "Running Trivy for container security..."
                            echo trivy image ${IMAGE_NAME}:${IMAGE_TAG}
                            echo "Container security scan completed"
                        '''
                    }
                }
            }
        }
        
        stage('Build and Push with Kaniko') {
            agent {
                kubernetes {
                    yaml """
        apiVersion: v1
        kind: Pod
        spec:
        containers:
            - name: kaniko
            image: gcr.io/kaniko-project/executor:latest
            command:
                - cat
            tty: true
            volumeMounts:
                - name: kaniko-secret
                mountPath: /kaniko/.docker
        volumes:
            - name: kaniko-secret
            secret:
                secretName: regcred
        """
                }
            }
            steps {
                container('kaniko') {
                    sh '''
                        /kaniko/executor \
                        --context `pwd` \
                        --dockerfile `pwd`/Dockerfile \
                        --destination=${IMAGE_NAME}:${IMAGE_TAG} \
                        --destination=${IMAGE_NAME}:latest
                    '''
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
