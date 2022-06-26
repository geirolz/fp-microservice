## Purely Functional HTTP microservice
[![Build Status](https://github.com/geirolz/fp-microservice/actions/workflows/cicd.yml/badge.svg)](https://github.com/geirolz/fp-microservice/actions)
[![Scala Steward](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://gitlab.com/moneyfarm-tech/sandbox/steward)
[![Mergify Status](https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/geirolz/fp-microservice&style=flat)](https://mergify.io)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/5c46a629cc5d447ca3d1e36ad776ba19)](https://www.codacy.com/gh/geirolz/fp-microservice/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=geirolz/fp-microservice&amp;utm_campaign=Badge_Grade)

---

Simple POC for dockerized HTTP microservice using Scala in a functional programming way.


### Usage

Deploy the docker image locally
```shell
sbt docker:publishLocal
```


Run docker compose in the project folder to startup the postgress instance and app instance
```shell
docker-compose up
```

And then create the database if not present

```shell
docker exec -it fp-ms-db /bin/bash -c "createdb -U postgres fp_ms_dev"
```


You can reach the app via browser, run
```shell
open http://localhost:9000/docs
```

> ⚠️ **You should use `http`** and not `https` beware that Chrome by default use `https`

You can also run directly 

```shell
./run.sh
```
--- 

To customize this project please read the [guide](doc/guide.md)

--- 

### Application stack
| SCOPE                               | TECH                                                                     |
|-------------------------------------|--------------------------------------------------------------------------|
| ✅ Effects                           | [cats-effect](https://github.com/typelevel/cats-effect)                  |
| ✅ Logging                           | [log4cats](https://github.com/typelevel/log4cats)                        |
| ✅ Jdbc (plain, transactions, etc..) | [doobie](https://github.com/tpolecat/doobie)                             |
| ✅ Db migration                      | [fly4s](https://github.com/geirolz/fly4s)                                |
| ✅ Http client and server            | [http4s](https://github.com/http4s/http4s)                               |
| ✅ Http route definitions            | [tapir](https://github.com/softwaremill/tapir)                           |
| ✅ Config                            | [pureconfig](https://github.com/pureconfig/pureconfig)                   |
| ✅ Json                              | [circe](https://github.com/circe/circe)                                  |
| ✅ Refinement types                  | [refined](https://github.com/fthomas/refined)                            |
| ⬜ Rabbit Client                     | [fs2-rabbit](https://github.com/profunktor/fs2-rabbit)                   |
| ⬜ Kafka Client                      | [fs2-kafka](https://github.com/fd4s/fs2-kafka)                           |
| ⬜ Unit tests                        | [munit](https://github.com/scalameta/munit)                              |
| ⬜ IT tests                          | [testcontainers](https://github.com/testcontainers/testcontainers-scala) |



### Infrastructure stack
| SCOPE                     | TECH                                                                                 |
|---------------------------|--------------------------------------------------------------------------------------|
| ⬜ Project as template     | [g8](http://www.foundweekends.org/giter8/)                                           |
| ✅ Containerized app       | [Docker](https://www.docker.com/) via [DockerSbt](https://github.com/sbt/docker-sbt) |
| ⬜ Containers orchestrator | [K8s](https://kubernetes.io/)                                                        |
| ⬜ Service mesh            | [Istio](https://istio.io/)                                                           |


#### Nice to have
- Redis
- MongoDB
