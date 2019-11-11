package com.dutt

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import cats.effect.IO
import cats.implicits._
import com.softwaremill.sttp.StringBody
import io.circe.generic.auto._
import tapir.json.circe._
import tapir.{Endpoint, endpoint, jsonBody, query, _}
import io.circe.generic.auto._
import io.circe.syntax._
import fs2.Stream
import io.circe.{Decoder, Encoder}
import org.http4s.headers.{Location, `Content-Type`}
import org.http4s.{HttpService, MediaType, Uri}
import tapir.EndpointOutput.StatusCode
import tapir.model.StatusCodes


// FRONTEND ENDPOINTS
trait CountryEndpoints {

  val getCountries: Endpoint[Unit, Unit, Stream[IO, Byte], Stream[IO, Byte]] =
    endpoint
      .get
      .name("getCountries")
      .in("countries")
      .out(streamBody[Stream[IO, Byte]](schemaFor[Byte], tapir.MediaType.Json()))

  val getCountry: Endpoint[String, Int, Country, Nothing] =
    endpoint
      .get
      .name("getCountry")
      .in("countries")
      .in(path[String]("code"))
      .out(jsonBody[Country])
      .errorOut(statusCode)

}

// LOGIC TO MAP FRONTEND TO BACKEND
class CountryEndpointLogic(dao: CountryDao[IO]) {

  def getCountriesLogic(a: Unit): IO[Either[Unit, Stream[IO, Byte]]] = {
    val s: Stream[IO, String] = Stream("[") ++ dao.get.map(_.asJson.noSpaces).intersperse(",") ++ Stream("]")
    IO(Right(s.through(fs2.text.utf8Encode)))
  }

  def getCountryLogic(a: String): IO[Either[Int, Country]] = {
    dao.get(a).map{
      case Some(f) => Right(f)
      case None => Left(StatusCodes.NotFound)
    }
  }
}
