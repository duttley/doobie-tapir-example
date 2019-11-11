package com.dutt


import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import tapir.docs.openapi._
import tapir.openapi.circe.yaml._
import tapir.server.http4s._
import tapir.swagger.http4s.SwaggerHttp4s

object Server extends IOApp with CountryEndpoints {

  val countryLogic = new CountryEndpointLogic(new CountryDaoImpl(CountryDao.xa))

  // mandatory implicits
  val docs: String = List(getCountries, getCountry).toOpenAPI("The Tapir Library", "1.0").toYaml

  // add to your akka routes
  private val swagger = new SwaggerHttp4s(docs).routes

  val getCountriesRoutes: HttpRoutes[IO] = getCountries.toRoutes(countryLogic.getCountriesLogic)
  val getCountryRoutes: HttpRoutes[IO] = getCountry.toRoutes(countryLogic.getCountryLogic)

  // starting the server
  override def run(args: List[String]): IO[ExitCode] = {

    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(
        Router(
          "/" -> getCountriesRoutes,
          "/" -> getCountryRoutes,
          "/docs" -> swagger).orNotFound)
      .serve
      .compile
      .lastOrError
  }
}