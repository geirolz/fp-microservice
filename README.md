## Functional HTTP microservice template
[![Build Status](https://github.com/geirolz/fp-microservice/actions/workflows/cicd.yml/badge.svg)](https://github.com/geirolz/fp-microservice/actions)
[![Scala Steward](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://gitlab.com/moneyfarm-tech/sandbox/steward)
[![Mergify Status](https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/geirolz/fp-microservice&style=flat)](https://mergify.io)
---

Simple POC for dockerized HTTP microservice using Scala in a functional programming way.


### Usage

This is a normal sbt project, you can compile code with `sbt compile` and run it
with `sbt run`.

### Technologies
- [x] effects                               -> [cats-effect](https://github.com/typelevel/cats-effect)
- [x] logging                               -> [log4cats](https://github.com/typelevel/log4cats)
- [x] jdbc (plain, transactions, etc..)     -> [doobie](https://github.com/tpolecat/doobie)
- [x] db migration                          -> [flyway](https://github.com/flyway/flyway)
- [x] http client and server                -> [http4s](https://github.com/http4s/http4s)
- [x] http route definitions                -> [tapir](https://github.com/softwaremill/tapir)
- [x] config                                -> [pureconfig](https://github.com/pureconfig/pureconfig)
- [ ] json (with decent errors)             -> [spray-json](https://github.com/spray/spray-json) till circe will have decent errors
- [ ] JWT
- [ ] Rabbit Fs2
- [ ] g8 ?

