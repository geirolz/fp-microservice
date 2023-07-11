#!/bin/zsh

### load generic infra vars
source "../../vars.sh"

export DB_DEPLOY_NAME=$APP_NAME-db

## Generated resolved docker-compose file to use
DOCKER_COMPOSE_FILE="$(envresolve "docker-compose.yml")"

### Deploy APP Docker image
echo "Deploying APP Docker image"
(cd "$PROJECT_DIR" || exit; chmod 777 deployLocalDockerImage.sh; ./deployLocalDockerImage.sh) &&

### Stop and run
echo "Stopping and running docker compose"
docker compose -f "$DOCKER_COMPOSE_FILE" stop
open http://localhost:"$APP_PORT"/docs &&
docker compose -f "$DOCKER_COMPOSE_FILE" up &&

# Init db
echo "Init db"
(docker exec -it "$DB_DEPLOY_NAME" /bin/bash -c "createdb -U postgres $DB_NAME" || true)
