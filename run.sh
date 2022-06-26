#!/bin/bash
jenv local 17.0

sbt deployLocal &&
docker-compose stop &&
docker-compose up &&
open "http://localhost:9000/docs"

docker exec -it fp-ms-db /bin/bash -c "createdb -U postgres fp_ms_dev" || true
