package com.dutt

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import cats.Applicative
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
trait CountryEndpoints[F[_]] {

  val getCountries: Endpoint[Unit, Unit, Stream[F, Byte], Stream[F, Byte]] =
    endpoint
      .get
      .name("getCountries")
      .in("countries")
      .out(streamBody[Stream[F, Byte]](schemaFor[Byte], tapir.MediaType.Json()))

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
class CountryEndpointLogic[F[_] : Applicative](dao: CountryDao[F]) {

  def getCountriesLogic(a: Unit): F[Either[Unit, Stream[F, Byte]]] = {
    val s = Stream("[") ++ dao.get.map(_.asJson.noSpaces).intersperse(",") ++ Stream("]")
    val ret: Either[Unit, Stream[F, Byte]] = Right(s.through(fs2.text.utf8Encode))
    ret.pure[F]
  }

  def getCountryLogic(a: String): F[Either[Int, Country]] = {
    dao.get(a).map{
      case Some(f) => Right(f)
      case None => Left(StatusCodes.NotFound)
    }
  }
}
