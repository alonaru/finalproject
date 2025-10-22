# Flask AWS Resource Monitor - Final DevOps Project

## Overview
A Flask web application that displays AWS resources (EC2 instances, VPCs, Load Balancers, AMIs) deployed using Docker and Kubernetes.

## Features
- Lists EC2 instances with state, type, and public IP
- Displays VPCs with CIDR blocks
- Shows Load Balancers and DNS names
- Lists available AMIs
- Multi-stage Docker build for optimized images
- Kubernetes deployment with Helm charts
- Secure credential management with K8s Secrets

## Prerequisites

### Required Software
- Docker Desktop or Docker Engine
- Kubernetes (Minikube or Docker Desktop K8s)
- kubectl CLI tool
- Helm 3+ (optional, for Helm deployment)
- Git

### AWS Requirements
- AWS Account
- IAM user with the following permissions:
  - AmazonEC2ReadOnlyAccess
  - AmazonVPCReadOnlyAccess
  - ElasticLoadBalancingReadOnly

## Project Structure

finalproject/
├── app/
│ ├── describe_resources.py
│ ├── requirements.txt
│ └── README.md
├── helm/
│ ├── Chart.yaml
│ ├── values.yaml
│ ├── templates/
│ └── README.md
├── Dockerfile
├── .gitignore
└── README.md

## Quick Start

### Option 1: Run with Docker

```bash
# 1. Clone repository
git clone https://github.com/alonaru/finalproject.git
cd finalproject

# 2. Create .env file with AWS credentials
cat > .env << ENVFILE
AWS_ACCESS_KEY_ID=your_access_key_here
AWS_SECRET_ACCESS_KEY=your_secret_key_here
ENVFILE

# 3. Build Docker image
docker build -t flask-aws-monitor:latest .

# 4. Run container
docker run -p 5001:5001 --env-file .env flask-aws-monitor:latest

# 5. Access: http://localhost:5001

###Option 2: Deploy to Kubernetes
# 1. Create Kubernetes secret
kubectl create secret generic aws-credentials \
  --from-literal=AWS_ACCESS_KEY_ID=your_key \
  --from-literal=AWS_SECRET_ACCESS_KEY=your_secret

# 2. Update AWS region in helm/values.yaml

# 3. Deploy with Helm
helm install flask-aws-monitor ./helm

# 4. Get service URL
minikube service flask-aws-monitor --url


# 1. Create Kubernetes secret
kubectl create secret generic aws-credentials \
  --from-literal=AWS_ACCESS_KEY_ID=your_key \
  --from-literal=AWS_SECRET_ACCESS_KEY=your_secret

# 2. Update AWS region in helm/values.yaml

# 3. Deploy with Helm
helm install flask-aws-monitor ./helm

# 4. Get service URL
minikube service flask-aws-monitor --url

## Configuration

### AWS Region
The application defaults to eu-west-1. To use a different region, edit helm/values.yaml:

```yaml
aws:
  defaultRegion: "us-east-1"  # Change to your region

Common regions: us-east-1, us-west-2, eu-west-1, eu-central-1

Environment Variables
Variable	Description	Required
AWS_ACCESS_KEY_ID	AWS Access Key	Yes
AWS_SECRET_ACCESS_KEY	AWS Secret Key	Yes

Security Best Practices
DO:

Use Kubernetes Secrets for credentials
Keep .env file local (never commit to Git)
Use IAM users with read-only permissions
Rotate credentials regularly
DONT:

Commit .env or credentials to Git
Use root AWS account credentials
Give write permissions to IAM user


## Troubleshooting

### NoCredentialsError
Problem: Application shows "Unable to locate credentials"

Solution:
- Docker: Ensure .env file exists with valid credentials
- Kubernetes: Verify secret exists with: kubectl get secret aws-credentials

### Port Already in Use
```bash
lsof -i :5001
# Kill the process or use a different port

Kubernetes Service Not Found
kubectl get pods
kubectl get svc
kubectl logs -l app=flask-aws-monitor

#Documentation
App Documentation: README.md
Helm Chart Guide: README.md

#Author
Alon Arusi

#Acknowledgments
DevOps Final Project
