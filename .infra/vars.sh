#!/bin/bash

# utils
PROJECT_DIR_RESULT="$( cd ../../../ && pwd)"
export PROJECT_DIR=$PROJECT_DIR_RESULT
export PROJECT_INFRA_DIR="$PROJECT_DIR_RESULT/.infra"

############################ VARS ###################################
# colors
export RED="\033[1;31m"
export GREEN="\033[1;32m"
export BLUE="\033[0;34m"
export NOCOLOR="\033[0m"

# app
sbt_prj_name=$(cd "$PROJECT_DIR_RESULT" && (sbt name | tail -n1 | cut -d' ' -f2))
export APP_NAME=$sbt_prj_name
export APP_PORT=8080
export APP_DOCKER_IMAGE_NAME=$APP_NAME
export APP_DOCKER_IMAGE_VERSION="latest"
export APP_HEALTHCHECK_PATH="/healthcheck"
export APP_HEALTHCHECK_ENDPOINT="http://localhost:"${APP_PORT}${APP_HEALTHCHECK_PATH}

# app db
export DB_NAME="main"

envresolve() {
  filename=$(basename -- "$1")
  extension="${filename##*.}"
  filename="${filename%.*}"
  resolvedfile="$filename.resolved.$extension"
  envsubst < "$1" > "$resolvedfile"
  echo "$resolvedfile"
}