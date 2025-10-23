FROM jenkins/jenkins:lts

# Install required plugins including Kubernetes and Pipeline View
RUN jenkins-plugin-cli --plugins \
    git \
    matrix-auth \
    credentials-binding \
    workflow-aggregator \
    docker-workflow \
    blueocean \
    job-dsl \
    workflow-job \
    workflow-cps \
    kubernetes \
    kubernetes-credentials \
    pipeline-stage-view

# Skip setup wizard
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false -Dhudson.security.csrf.DefaultCrumbIssuer.EXCLUDE_SESSION_ID=true"

# Copy initialization scripts
COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY default-user.groovy /usr/share/jenkins/ref/init.groovy.d/

VOLUME /var/jenkins_home
