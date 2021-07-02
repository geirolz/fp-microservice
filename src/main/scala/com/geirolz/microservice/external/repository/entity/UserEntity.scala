package com.geirolz.microservice.external.repository.entity

import com.geirolz.microservice.model.User

private[repository] case class UserEntity() {

  def toDomain: User = ???
}
