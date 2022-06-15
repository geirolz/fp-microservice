sbt deployLocal
open http://localhost:9000/info
docker-compose up
docker exec -it fp-ms-db /bin/bash -c "createdb -U postgres fp_ms_dev" || true
