package com.dutt


import cats.effect._
import config.Config
import db.Database
import doobie.hikari.HikariTransactor
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import tapir.docs.openapi._
import tapir.openapi.circe.yaml._
import tapir.server.http4s._
import tapir.swagger.http4s.SwaggerHttp4s

object Server extends IOApp with CountryEndpoints {

  // mandatory implicits
  val docs: String = List(getCountries).toOpenAPI("The Tapir Library", "1.0").toYaml

  // add to your akka routes
  private val swagger = new SwaggerHttp4s(docs).routes

  private val config = Config.load()

  // starting the server
  override def run(args: List[String]): IO[ExitCode] = {
    Database.transactor(config.database).use { xa =>
      val logic = new CountryEndpointLogic(new CountryDaoImpl(xa))
      val server = for {
        server <- BlazeServerBuilder[IO]
          .bindHttp(config.server.port, config.server.host)
          .withHttpApp(
            Router(
              "/" -> getCountries.toRoutes(logic.getCountryLogic),
              "/docs" -> swagger).orNotFound)
          .serve
      } yield server
      server.compile.lastOrError
    }
  }
}