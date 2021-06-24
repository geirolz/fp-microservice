## Functional HTTP microservice template
[![Scala Steward](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://gitlab.com/moneyfarm-tech/sandbox/steward)

---

Simple POC for dockerized HTTP microservice using Scala in a functional programming way.


### Usage

This is a normal sbt project, you can compile code with `sbt compile` and run it
with `sbt run`.

### Technologies
- effects                               -> [cats-effect](https://github.com/typelevel/cats-effect)
- logging                               -> [log4cats](https://github.com/typelevel/log4cats)
- jdbc (plain, transactions, etc..)     -> [doobie](https://github.com/tpolecat/doobie)
- db migration                          -> [flyway](https://github.com/flyway/flyway)
- tracing (jdbc, http, ...)             -> [kamon](https://github.com/kamon-io/Kamon), https://github.com/kamon-io/kamon-http4s
- http client and server                -> [http4s](https://github.com/http4s/http4s)
- http route definitions                -> [tapir](https://github.com/softwaremill/tapir)
- config                                -> [pureconfig](https://github.com/pureconfig/pureconfig)
- json (with decent errors)             -> [spray-json](https://github.com/spray/spray-json) till circe will have decent errors
- jwt + cats-effects



