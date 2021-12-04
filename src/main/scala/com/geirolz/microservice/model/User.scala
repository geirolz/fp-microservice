package com.geirolz.microservice.model

import com.geirolz.microservice.model.values.{FirstName, LastName, MiddleName, UserId}

case class User(
  id: UserId,
  firstName: FirstName,
  middleName: Option[MiddleName],
  lastName: LastName
)
