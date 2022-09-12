#!/bin/zsh

############################ VARS ###################################
source "../../vars.sh"
# ---
export DEPLOYMENT_CONTEXT='minikube'
export DEPLOYMENT_ENVIRONMENT='local'
export DEPLOYMENT_NAMESPACE='local'
export DEPLOYMENT_NAME=$APP_NAME

# --- APP ---
export APP_DEPLOY_NAME=$DEPLOYMENT_NAME
export APP_DEPLOY_PORT=$APP_PORT

export APP_SERVICE_NAME=$APP_NAME-service
export APP_SERVICE_PORT=8080
# --- DB ---
export DB_DEPLOY_NAME=$DEPLOYMENT_NAME-db
export DB_DEPLOY_TARGET_PORT=5432
export DB_DEPLOY_VOLUME_NAME=$DB_DEPLOY_NAME-volume

export DB_SERVICE_NAME=$DB_DEPLOY_NAME-service
export DB_SERVICE_PORT=5432

# --- CONFIG ---
export CONFIGMAP_NAME=$DEPLOYMENT_NAME-config
export SECRET_NAME=$DEPLOYMENT_NAME-secret

# Generated resolved resource file to use
RESOURCES_FILE="$(envresolve "resources.yml")"


############################ DEPLOY ###################################
ALREADY_ON="$(minikube status | grep "kubelet: Running")"
if [[ -z $ALREADY_ON ]]; then

  ### Deploy APP Docker image
  (cd "$PROJECT_DIR" || exit; chmod 777 deployImage.sh; ./deployImage.sh) &&

  ### Start minikube
  minikube start --cpus 4 --memory 5g

  ### Set docker env
  eval "$(minikube docker-env)"             # unix shells
  minikube docker-env | Invoke-Expression # PowerShell

  ### Load local Docker image into Minikube
  minikube image load "$APP_DOCKER_IMAGE_NAME"

  ### Build container directly in minikube
  minikube image build -t "$APP_DOCKER_IMAGE_NAME" .
fi

### Apply app
echo -e "$RED"
echo
kubectl config use-context minikube &&
kubectl create namespace $DEPLOYMENT_NAMESPACE || true &&
kubectl apply -f "$RESOURCES_FILE" --context=$DEPLOYMENT_CONTEXT --namespace=$DEPLOYMENT_NAMESPACE &&
echo -e "$NOCOLOR" &&

#### Check that it's running
echo -e "$GREEN" &&
kubectl get service --namespace=$DEPLOYMENT_NAMESPACE &&
echo -e "$NOCOLOR" &&
printf "\n\n" &&
kubectl logs deployment.apps/"${APP_NAME}" --namespace=$DEPLOYMENT_NAMESPACE







