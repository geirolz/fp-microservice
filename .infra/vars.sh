#!/bin/bash

############################ VARS ###################################
# colors
export RED="\033[1;31m"
export GREEN="\033[1;32m"
export NOCOLOR="\033[0m"

# app
export APP_NAME="fp-microservice"
export APP_PORT=8080
export APP_DOCKER_IMAGE_NAME=$APP_NAME
export APP_DOCKER_IMAGE_VERSION="latest"
export APP_HEALTHCHECK_PATH="/healthcheck"
export APP_HEALTHCHECK_ENDPOINT="http://localhost:"${APP_PORT}${APP_HEALTHCHECK_PATH}

envresolve() {
  filename=$(basename -- "$1")
  extension="${filename##*.}"
  filename="${filename%.*}"
  resolvedfile="$filename.resolved.$extension"
  envsubst < "$1" > "$resolvedfile"
  echo "$resolvedfile"
}