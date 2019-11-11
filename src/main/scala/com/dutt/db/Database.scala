package com.dutt.db

import cats.effect.{ContextShift, IO}
import com.dutt.config.DatabaseConfig
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.hikari._

object Database {

  def transactor(config: DatabaseConfig)(implicit cs: ContextShift[IO]): Resource[IO, HikariTransactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      be <- Blocker[IO]    // our blocking EC
      xa <- HikariTransactor.newHikariTransactor[IO](
        config.driver,                        // driver classname
        config.url,   // connect URL
        config.user,                                   // username
        config.password,                                     // password
        ce,                                     // await connection here
        be                                      // execute JDBC operations here
      )
    } yield xa

  def initialize(transactor: HikariTransactor[IO]): IO[Unit] = {
    transactor.configure { dataSource =>
      IO {
        val flyWay = Flyway.configure().dataSource(dataSource).load()
        //flyWay.baseline()
        flyWay.migrate()
        ()
      }
    }
  }
}
