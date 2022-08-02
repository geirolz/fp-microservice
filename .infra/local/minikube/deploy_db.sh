#!/bin/bash

# Stop and run
DCFILE="docker-compose-db.yml"

docker-compose -f "${DCFILE}" stop &&
docker-compose -f "${DCFILE}" up

docker exec -it minikube-fp-ms-db /bin/bash -c "createdb -U postgres fp_ms_dev" || true
