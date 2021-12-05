package com.geirolz.microservice.model

import com.geirolz.microservice.model.values.{Email, FirstName, LastName, MiddleName, UserId}

case class User(
  id: UserId,
  email: Email,
  firstName: FirstName,
  middleName: Option[MiddleName],
  lastName: LastName
)
