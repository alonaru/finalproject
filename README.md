### CI/CD Application

## Prerequisites
1. Kubernetes Cluster (Minikube, GKE, or other)
    * before run any step in this README, make sure K8s cluster is on
    * in local enviroment I used minikube.
2. `kubectl` and `helm` installed and configured
3. Docker Hub account with a Personal Access Token (Read, Write, Delete)
4. GitHub repository with:
   - `Dockerfile`
   - `app/` directory (your code)
   - `requirements.txt`
   - `Jenkinsfile`

# Prepering Jenkins CI/CD
1. installing Jenkins

helm repo add jenkinsci https://charts.jenkins.io
helm repo update
helm install jenkins jenkinsci/jenkins

2. get the admin password

kubectl get secret jenkins -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode

3. port-forward jenkins UI:

kubectl port-forward svc/jenkins 8080:8080
*for cloudshell need to activate web preview*
browse to http://localhost:8080

login username: admin
password: output from step 2


# Install Jenkins plugin
jenkins in this guide installed as vanilla, if needed install nessery plugin.
1. Pipeline stage view - for visibale stage flow
2. Kubernetes - exist for vanilla, but its mandatory so make sure its exist and if not download it.


# Prepare Docker Hub Credentials
1. Generate the base64 string:
echo -n "alonaru:YOUR_TOKEN" | base64

2. Create a config.json file:

run nano config.json
paste the string belog to config.json

{
  "auths": {
    "https://index.docker.io/v1/": {
      "auth": "BASE64_OF_alonaru:YOUR_TOKEN"
    }
  }
}

3. Create Kubernetes Secret for Kaniko
kubectl create secret generic kaniko-secret \
  --from-file=config.json=config.json

to verify run:
kubectl get secret kaniko-secret -o yaml

4. Configure Jenkins Pipeline
go to repo https://github.com/alonaru/finalproject.git
verify this line exist in jenkinsfile at root directory

secretVolume(mountPath: '/kaniko/.docker/', secretName: 'kaniko-secret')


# Run Your Jenkins Pipeline

create new item use the repo 
https://github.com/alonaru/finalproject.git
and the current branch