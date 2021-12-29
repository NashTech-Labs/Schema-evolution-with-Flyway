package com.knoldus.template.app

import com.dimafeng.testcontainers.PostgreSQLContainer
import com.knoldus.template.flyway.FlywayService
import com.knoldus.template.repository.{SqlRepositoryInstantiator, UserRepository, UserRow}
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Minute, Span}
import org.testcontainers.utility.DockerImageName

import scala.concurrent.ExecutionContext.Implicits.global

class DBTest  extends AnyFlatSpec with Matchers with BeforeAndAfterAll with ScalaFutures {

  val latestPostgresImage: DockerImageName = DockerImageName.parse("postgres:latest")
  val postgresContainer: PostgreSQLContainer = PostgreSQLContainer(dockerImageNameOverride = latestPostgresImage, databaseName = "ccf-schema")
  postgresContainer.start()

  System.setProperty("DB_URL", postgresContainer.jdbcUrl)
  System.setProperty("DB_USERNAME", postgresContainer.username)
  System.setProperty("DB_PASSWORD", postgresContainer.password)

  ConfigFactory.invalidateCaches()

  val config: Config = ConfigFactory.load()

  val flywayService = new FlywayService(config.getConfig("db"))
  flywayService.migrateDatabaseSchema()

  val sqlRepositoryInstantiator: SqlRepositoryInstantiator = new SqlRepositoryInstantiator(config.getConfig("db"))
  val userRepo: UserRepository = sqlRepositoryInstantiator.userRepository

  override def afterAll(): Unit = {
    postgresContainer.stop()
  }

  it should "insert the data in the tables and fetch from them" in {
    val userRow = UserRow(
      id = Some("userId1"),
      name = "Yash Gupta",
      username = "gupta_yash",
      password = "123456789"
    )

    val x = for {
      _ <- {
        userRepo.insertUserRecord(userRow)
      }
      r <- {
        Thread.sleep(3000)
        userRepo.getAll
      }
    } yield r

    whenReady(x, Timeout(Span(1, Minute))) { userRow =>
      userRow.nonEmpty shouldBe true
      userRow.get.username shouldBe "gupta_yash"
    }
  }

}
