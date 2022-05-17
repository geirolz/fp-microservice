package com.geirolz.fpmicroservice.testing

import com.geirolz.fpmicroservice.model.values.{Email, FirstName, LastName, MiddleName, UserId}
import com.geirolz.fpmicroservice.model.User

import scala.util.Random

object Samples {

  def aUser(
    id: UserId                     = aUserId,
    email: Email                   = anEmail,
    firstName: FirstName           = aFirstName,
    middleName: Option[MiddleName] = Some(aMiddleName),
    lastName: LastName             = aLastName
  ): User = User(id, email, firstName, middleName, lastName)

  def aUserId: UserId =
    UserId(aLong)

  def anEmail: Email =
    Email(s"${aString()}@${aString()}.com")

  def aFirstName: FirstName =
    FirstName(aString(10))

  def aMiddleName: MiddleName =
    MiddleName(aString(10))

  def aLastName: LastName =
    LastName(aString(10))

  // primitives
  def aString(size: Int = 5): String =
    Random.alphanumeric.filter(_.isLetter).take(size).mkString("")

  def aLong: Long =
    Random.nextLong()
}
