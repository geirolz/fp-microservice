package com.geirolz.fpmicroservice.model

import com.geirolz.fpmicroservice.model.values.{Email, FirstName, LastName, MiddleName, UserId}

case class User(
  id: UserId,
  email: Email,
  firstName: FirstName,
  middleName: Option[MiddleName],
  lastName: LastName
)
