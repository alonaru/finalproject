# Jenkins CI/CD Server

## Version

Jenkins: LTS (Latest Long-Term Support)
Docker Image: flask-jenkins:latest

## Purpose

Pre-configured Jenkins server for Flask AWS Monitor CI/CD pipeline.
Automates Docker image builds and pushes to Docker Hub.
Supports Kubernetes deployments.

## Run Guide

Step 1: Configure credentials (optional - defaults to admin/admin)
cp .env.example .env
nano .env

Step 2: Build and run Jenkins
cd jenkins
docker compose up -d --build

Step 3: Access Jenkins
Open browser: http://localhost:8080

Alternative - Using Docker Run:
docker build -t flask-jenkins:latest .
docker run -d --name jenkins-cicd -p 8080:8080 -p 50000:50000 -e JENKINS_USER=admin -e JENKINS_PASS=YourPassword -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock --privileged --user root flask-jenkins:latest

Stop Jenkins:
docker compose down

Remove Jenkins:
docker compose down -v

## Plugins Installed

- Git
- Matrix Authorization Strategy
- Credentials Binding
- Pipeline (Workflow Aggregator)
- Docker Pipeline
- BlueOcean
- Job DSL
- Workflow Job
- Workflow CPS
- Kubernetes
- Kubernetes Credentials
- Pipeline Stage View

## How to Login

URL: http://localhost:8080
Default Username: admin
Default Password: admin

To use custom credentials:
Set JENKINS_USER and JENKINS_PASS in .env file before starting Jenkins
