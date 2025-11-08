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
1. Pipeline stage step - for visibale stage flow
2. Kubernetes - exist for vanilla, but its mandatory so make sure its exist and if not download it.


# Prepare Docker Hub Credentials
manual authentication to Docker hub
1. run: docker login --usename=<mydockerusarname>
output expect to enter password or personal access token.
when authentication seccessfully the process create file name config.json is .docker dir.

==================================================
======example output======
alonarusi4@cloudshell:~$ docker login --username=alonaru

i Info → A Personal Access Token (PAT) can be used instead.
         To create a PAT, visit https://app.docker.com/settings
         
         
Password: 

WARNING! Your credentials are stored unencrypted in '/home/alonarusi4/.docker/config.json'.
Configure a credential helper to remove this warning. See
https://docs.docker.com/go/credential-store/

Login Succeeded
=================================================

***or create file manualy using next actions***

1. create docker hub config.json manualy
Generate the base64 string:
echo -n "alonaru:YOUR_TOKEN" | base64

Create a config.json file:

run nano config.json
paste the string belog to config.json

{
  "auths": {
    "https://index.docker.io/v1/": {
      "auth": "BASE64_OF_alonaru:YOUR_TOKEN"
    }
  }
}

2. Create Kubernetes Secret for Kaniko
kubectl create secret generic kaniko-secret \
  --from-file=config.json=/path/to/config.json

to verify run:
kubectl get secret kaniko-secret -o yaml

4. verify kaniko configuration in Jenkinsfile.
go to repo https://github.com/alonaru/finalproject.git
in root dir find jenkinsfile
verify this line exist in jenkinsfile at root directory

secretVolume(mountPath: '/kaniko/.docker/', secretName: 'kaniko-secret')


# Run Your Jenkins Pipeline

* in jenkins main page open new item
1. enter item name: your-pipeline-description
2. item type: Pipeline
3. click OK

* next page - General
1. Pipleline >> Definition >> choose Pipeline script from SCM
2. under SCM >> Git
3. Repositories >> https://github.com/alonaru/finalproject.git
4. Branches to build >> branch Specifier >> enter current branch in the repo.
and the current branch