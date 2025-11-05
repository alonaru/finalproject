FROM jenkins/jenkins:lts

USER root

# Install Docker CLI
RUN apt-get update && \
    apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null && \
    apt-get update && \
    apt-get install -y docker-ce-cli && \
    rm -rf /var/lib/apt/lists/*

# Install Python and pip tools for linting and security
RUN apt-get update && \
    apt-get install -y python3 python3-pip shellcheck curl && \
    pip3 install --no-cache-dir --break-system-packages flake8 bandit && \
    curl -L -o /usr/local/bin/hadolint https://github.com/hadolint/hadolint/releases/download/v2.12.0/hadolint-Linux-x86_64 && \
    chmod +x /usr/local/bin/hadolint && \
    rm -rf /var/lib/apt/lists/*

ENV PATH="/usr/local/bin:${PATH}"

USER jenkins

# Install Jenkins plugins
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
