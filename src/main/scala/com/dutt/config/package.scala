package com.dutt

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import org.specs2.execute.MyClass
import pureconfig.error.ConfigReaderException

package object config {

  case class ServerConfig(host: String ,port: Int)

  case class DatabaseConfig(driver: String, url: String, user: String, password: String)

  case class Config(server: ServerConfig, database: DatabaseConfig)

  object Config {
    import pureconfig._
    import pureconfig.generic.auto._

    def load(configFile: String = "application.conf"): Config = {
      ConfigSource.default.loadOrThrow[Config]
    }
  }
}
