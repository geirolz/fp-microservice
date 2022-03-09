## Purely Functional HTTP microservice
[![Build Status](https://github.com/geirolz/fp-microservice/actions/workflows/cicd.yml/badge.svg)](https://github.com/geirolz/fp-microservice/actions)
[![Scala Steward](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://gitlab.com/moneyfarm-tech/sandbox/steward)
[![Mergify Status](https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/geirolz/fp-microservice&style=flat)](https://mergify.io)

---

Simple POC for dockerized HTTP microservice using Scala in a functional programming way.


### Usage

Run docker compose in the project folder to startup the postgress instance

```shell
docker-compose up
```

And then create the dabase if not present

```shell
docker exec -it fp-ms-db /bin/bash -c "createdb -U postgres fp_ms_dev"
```

This is a normal sbt project, you can compile code with `sbt compile` and run it
with `sbt run`.

To customize this project please read the [guide](doc/guide.md)

### Technologies stack

| SCOPE                               | TECH                                                    |
|-------------------------------------|---------------------------------------------------------|
| ✅ Effects                           | [cats-effect](https://github.com/typelevel/cats-effect) |
| ✅ Logging                           | [log4cats](https://github.com/typelevel/log4cats)       |
| ✅ Jdbc (plain, transactions, etc..) | [doobie](https://github.com/tpolecat/doobie)            |
| ✅ Db migration                      | [fly4s](https://github.com/geirolz/fly4s)               |
| ✅ Http client and server            | [http4s](https://github.com/http4s/http4s)              |
| ✅ Http route definitions            | [tapir](https://github.com/softwaremill/tapir)          |
| ✅ Config                            | [pureconfig](https://github.com/pureconfig/pureconfig)  |
| ✅ Json                              | [circe](https://github.com/circe/circe)                 |
| ⬜ Tests                             | [munit](https://github.com/scalameta/munit)             |
| ⬜ Rabbit Client                     | [fs2-rabbit](https://github.com/profunktor/fs2-rabbit)  |
| ⬜ Kafka Client                      | [fs2-kafka](https://github.com/fd4s/fs2-kafka)          |
| ⬜ g8                                |                                                         |
| ⬜ Docker                            |                                                         |
| ⬜ Scala 3                           |                                                         |
| ⬜ Open Telemetry                    |                                                         |

