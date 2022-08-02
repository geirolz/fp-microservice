#!/bin/zsh

### load generic infra vars
source "../../vars.sh"

# Generated resolved docker-compose file to use
DOCKER_COMPOSE_FILE="$(envresolve "docker-compose.yml")"

### Stop and run
docker-compose stop -f "$DOCKER_COMPOSE_FILE" &&
open http://localhost:"$APP_PORT"/docs &&
docker-compose up -f "$DOCKER_COMPOSE_FILE"

### Init DB
docker exec -it fp-ms-db /bin/bash -c "createdb -U postgres fp_ms_dev" || true
