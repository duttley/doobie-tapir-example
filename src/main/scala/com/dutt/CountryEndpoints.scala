package com.dutt

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import cats.effect.IO
import cats.implicits._
import io.circe.generic.auto._
import tapir.json.circe._
import tapir.{Endpoint, endpoint, jsonBody, query, _}

import io.circe.generic.auto._
import io.circe.syntax._
import fs2.Stream
import io.circe.{Decoder, Encoder}
import org.http4s.headers.{Location, `Content-Type`}
import org.http4s.{HttpService, MediaType, Uri}

trait CountryEndpoints {

  val getCountries: Endpoint[Unit, Unit, Stream[IO, Byte], Stream[IO, Byte]] =
    endpoint
      .get
      .name("getSchemas2")
      .in("countries")
      .out(streamBody[Stream[IO, Byte]](schemaFor[Byte], tapir.MediaType.Json()))

}

class CountryEndpointLogic(dao: CountryDao[IO]) {

  def getCountryLogic(a: Unit): IO[Either[Unit, Stream[IO, Byte]]] = {
    val s: Stream[IO, String] = Stream("[") ++ dao.get.map(_.asJson.noSpaces).intersperse(",") ++ Stream("]")
    IO(Right(s.through(fs2.text.utf8Encode)))
  }
}
