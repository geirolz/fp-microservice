package com.geirolz.microservice.route.endpoint.util

import cats.effect.IO

trait ToDomainMapper[C, D] extends Function[C, IO[D]]
object ToDomainMapper {
  implicit class ToDomainMapperSyntax[C, D](c: C)(implicit m: ToDomainMapper[C, D]) {
    def toDomain: IO[D] = m(c)
  }
}

trait ToContractMapper[D, C] extends Function[D, C]
object ToContractMapper {
  implicit class ToContractMapperSyntax[D, C](d: D)(implicit m: ToContractMapper[D, C]) {
    def toContract: C = m(d)
  }
}
