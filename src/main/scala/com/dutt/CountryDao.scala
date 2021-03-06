package com.dutt

import cats.effect.IO
import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._
import com.dutt.CountryDao.xa
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import fs2.Stream

object CountryDao {
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  // A transactor that gets connections from java.sql.DriverManager and executes blocking operations
  // on an our synchronous EC. See the chapter on connection handling for more info.
  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",     // driver classname
    "jdbc:postgresql:world",     // connect URL (driver-specific)
    "postgres",                  // user
    "",                          // password
    Blocker.liftExecutionContext(ExecutionContexts.synchronous) // just for testing
  )
}

trait CountryDao[F[_]] {
  def get(id: String): F[Option[Country]]
  def get: Stream[F, Country]
  def update(country: Country): F[Int]
  def insert(country: Country): F[Int]
}


class CountryDaoImpl[F[_] : Async](xa: Transactor[F]) extends CountryDao[F] {

  override def get(): Stream[F, Country] = {
    sql"""
         |select code, name, continent,
         |region, surfacearea, indepyear,
         |population, lifeexpectancy, gnp,
         |gnpold, localname, governmentform,
         |headofstate, capital, code2
         |from country
         |"""
      .stripMargin
      .query[Country]
      .stream
      .transact(xa)
  }

  override def get(id: String): F[Option[Country]] = {
    sql"select * from country where code = $id"
      .query[Country]
      .option
      .transact(xa)
  }

  override def update(country: Country): F[Int] = ???

  override def insert(c: Country): F[Option[Country]] = {
    val ret = sql"""
         |insert into country (name, code, region, population)
         |values ($c.name, $c.code, $c.region, $c.population)
         |"""
      .stripMargin
      .update
      .run
      .transact(xa)

    ret.map{
      case 1 => Some(c)
      case _ => None
    }
  }
}
