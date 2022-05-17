package com.geirolz.fpmicroservice.testing

import cats.effect.IO
import com.geirolz.fpmicroservice.model.User
import com.geirolz.fpmicroservice.model.values.UserId
import com.geirolz.fpmicroservice.service.UserService

object FakeUserService {

  def fromSeq(ls: Seq[User]): UserService = new UserService {
    override def getById(id: UserId): IO[Option[User]] = IO.pure(ls.find(_.id == id))
  }
}
