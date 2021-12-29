package com.knoldus.template.app

import com.knoldus.template.flyway.FlywayService
import com.knoldus.template.repository.{SqlRepositoryInstantiator, UserRow}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val config = ConfigFactory.load()

  val flywayService = new FlywayService(config.getConfig("db"))
  flywayService.migrateDatabaseSchema()

  val sqlRepositoryInstantiator: SqlRepositoryInstantiator = new SqlRepositoryInstantiator(config.getConfig("db"))
  val userRepo                                              = sqlRepositoryInstantiator.userRepository

  val userRow = UserRow(
    id = Some("userId1"),
    name = "Yash Gupta",
    username = "gupta_yash",
    password = "123456789"
  )

  userRepo.insertUserRecord(userRow)

  Thread.sleep(10000)
}
