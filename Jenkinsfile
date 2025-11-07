def appname = "flask-aws-monitor"
def repo = "alonaru"  // Your DockerHub username
def artifactory = "docker.io"
def appimage = "${artifactory}/${repo}/${appname}"
def apptag = "${env.BUILD_NUMBER}"

podTemplate(
  containers: [
    containerTemplate(name: 'jnlp', image: 'jenkins/inbound-agent', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker:20.10.16', ttyEnabled: true, command: 'cat'),
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
          container('docker') {
            sh '''
              echo "Running Flake8 for Python linting..."
              docker run --rm -v $PWD/app:/app python:3.11-slim bash -c "pip install flake8 && flake8 /app --max-line-length=120"
              echo "Python linting completed"

              echo "Running Hadolint for Dockerfile..."
              docker run --rm -v $PWD:/workspace hadolint/hadolint hadolint /workspace/Dockerfile
              echo "Dockerfile linting completed"
            '''
          }
        },
        "Security Scan": {
          container('docker') {
            sh '''
              echo "Running Bandit for Python security scanning..."
              docker run --rm -v $PWD/app:/app python:3.11-slim bash -c "pip install bandit && bandit -r /app"
              echo "Python security scan completed"

              echo "Running Trivy for container security..."
              docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image ${appimage}:${apptag} || true
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
            --context `pwd` \
            --dockerfile `pwd`/Dockerfile \
            --destination=${appimage}:${apptag} \
            --destination=${appimage}:latest
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