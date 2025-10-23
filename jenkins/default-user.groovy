import jenkins.model.*
import hudson.security.*

def env = System.getenv()

def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)

// Use environment variables with defaults
def username = env.JENKINS_USER ?: 'admin'
def password = env.JENKINS_PASS ?: 'admin'

hudsonRealm.createAccount(username, password)
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()

println "Jenkins user created: ${username}"
