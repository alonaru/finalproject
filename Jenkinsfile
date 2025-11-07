def appname = "flask-aws-monitor"
def repo = "alonaru"  // Your DockerHub username
def artifactory = "docker.io"
def appimage = "${artifactory}/${repo}/${appname}"
def apptag = "${env.BUILD_NUMBER}"

podTemplate(
  containers: [
    containerTemplate(name: 'jnlp', image: 'jenkins/inbound-agent', ttyEnabled: true),
    containerTemplate(name: 'lintsec', image: 'python:3.11-slim', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'trivy', image: 'get.trivy.dev/trivy:0.67.0', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'kaniko', image: 'gcr.io/kaniko-project/executor:debug-v0.19.0', command: '/busybox/cat', ttyEnabled: true)
  ],
  volumes: [
    secretVolume(mountPath: '/kaniko/.docker/', secretName: 'docker-cred')
  ]
) {
  node(POD_LABEL) {
    stage('Checkout') {
      container('jnlp') {
        sh '/usr/bin/git config --global http.sslVerify false'
        checkout scm
      }
    }

    stage('Parallel Checks') {
      parallel (
        "Linting": {
          container('lintsec') {
            sh '''
              echo "Running Flake8 for Python linting..."
              pip install flake8
              flake8 app --max-line-length=120
              echo "Python linting completed"

              echo "Running Hadolint for Dockerfile..."
              wget -O /usr/local/bin/hadolint https://github.com/hadolint/hadolint/releases/latest/download/hadolint-Linux-x86_64
              chmod +x /usr/local/bin/hadolint
              hadolint Dockerfile
              echo "Dockerfile linting completed"
            '''
          }
        },
        "Security Scan": {
            container('lintsec') {
            sh '''
                echo "Running Bandit for Python security scanning..."
                pip install bandit
                bandit -r /app
                echo "Python security scan completed"
            '''
            }
            container('trivy') {
            sh '''
                echo "Running Trivy for container security..."
                trivy fs /workspace || true
                echo "Container security scan completed"
            '''
            }
        }
      )
    }

    stage('Build and Push with Kaniko') {
      container('kaniko') {
        sh """
          /kaniko/executor \
            --force \
            --context `pwd` \
            --dockerfile `pwd`/Dockerfile \
            --destination=${appimage}:${apptag}
        """
      }
    }

    // Optional: Add deploy stage if needed
    // stage('Deploy') {
    //   container('kaniko') {
    //     echo "Deploy step goes here"
    //   }
    // }
  }
}