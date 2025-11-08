# Helm Charts

This directory contains the Helm chart for deploying the Flask AWS Resource Monitor application.

## Prerequisites

- Kubernetes cluster (Minikube, GKE, etc.)
- Helm installed and configured
- Docker image pushed to Docker Hub
- AWS credentials (see below)

## Setup

1. **Create AWS Credentials Secret**

   Replace `your_key` and `your_secret` with your actual AWS credentials:
   ```sh
   kubectl create secret generic aws-credentials \
     --from-literal=AWS_ACCESS_KEY_ID=your_key \
     --from-literal=AWS_SECRET_ACCESS_KEY=your_secret
   ```

2. **Configure values.yaml**

   Edit [`values.yaml`](values.yaml) to set your Docker image, AWS region, and other options as needed.

3. **Install the Helm Chart**

   From the project root:
   ```sh
   helm install flask-aws-monitor ./helm
   ```

4. **Access the Application**

   If ingress is disabled (default), forward the service port:
   ```sh
   kubectl port-forward svc/flask-aws-monitor 5001:5001
   ```
   Then open [http://localhost:5001] in your browser.

## Notes

- To upgrade after changes:
  ```sh
  helm upgrade flask-aws-monitor ./helm
  ```
- To uninstall:
  ```sh
  helm uninstall flask-aws-monitor
  ```

See [`values.yaml`](values.yaml) for all configuration options.