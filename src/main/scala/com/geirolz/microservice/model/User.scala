package com.geirolz.microservice.model

import com.geirolz.microservice.model.values.UserId

case class User(
  id: UserId,
  name: String,
  surname: String
)
