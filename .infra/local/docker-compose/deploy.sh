#!/bin/bash

# Stop and run
docker-compose stop &&
open http://localhost:9000/docs &&
docker-compose up

docker exec -it fp-ms-db /bin/bash -c "createdb -U postgres fp_ms_dev" || true
