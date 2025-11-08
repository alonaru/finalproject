### CI/CD Application

## Prerequisites
1. **Kubernetes Cluster** (Minikube, GKE, or other)
   - Ensure your K8s cluster is running before proceeding.
   - Example: Minikube for local development.
2. **kubectl** and **helm** installed and configured.
3. **Docker Hub account** with a Personal Access Token (Read, Write, Delete).
4. **GitHub repository** with:
   - `Dockerfile`
   - `app/` directory (your code)
   - `requirements.txt`
   - `Jenkinsfile`

## Preparing Jenkins CI/CD

### 1. Install Jenkins

```sh
helm repo add jenkinsci https://charts.jenkins.io
helm repo update
helm install jenkins jenkinsci/jenkins
```

### 2. Get the admin password

```sh
kubectl get secret jenkins -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode
```

### 3. Port-forward Jenkins UI

```sh
kubectl port-forward svc/jenkins 8080:8080
```
*for cloudshell need to activate web preview*
browse to http://localhost:8080

- Username: `admin`
- Password: output from step 2


### 4. Install Jenkins Plugins

- Pipeline Stage View (for visible stage flow)
- Kubernetes (should be present by default, but verify)


## Prepare Docker Hub Credentials
### Option 1 Authenticate to Docker Hub
```sh
docker login --username=<your-docker-username>
```
- Enter your password or personal access token.
**Note** - when authentication seccessfully the process create file name config.json is .docker dir.

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

### Option 2 Create `config.json` manually (optional)
Generate the base64 string:
```sh
echo -n "alonaru:YOUR_TOKEN" | base64
```
Create `config.json`:
```json
{
  "auths": {
    "https://index.docker.io/v1/": {
      "auth": "BASE64_OF_alonaru:YOUR_TOKEN"
    }
  }
}
```

### Create Kubernetes Secret for Kaniko
```sh
kubectl create secret generic kaniko-secret \
  --from-file=config.json=/path/to/config.json
```

Verify:
```sh
kubectl get secret kaniko-secret -o yaml
```

### Verify Kaniko configuration in Jenkinsfile
go to repo https://github.com/alonaru/finalproject.git
in root dir find jenkinsfile
Ensure this line exists in your Jenkinsfile:
```groovy
secretVolume(mountPath: '/kaniko/.docker/', secretName: 'kaniko-secret')
```


## Run Your Jenkins Pipeline

### Creat new Pipeline
1. In Jenkins, create a new item (Pipeline).
2. Set "Pipeline script from SCM" and configure your GitHub repo and branch.

### next page - General
1. Pipleline >> Definition >> choose Pipeline script from SCM
2. under SCM >> Git
3. Repositories >> https://github.com/alonaru/finalproject.git
4. Branches to build >> branch Specifier >> enter current branch in the repo.
and the current branch