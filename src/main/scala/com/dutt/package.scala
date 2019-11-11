package com

package object dutt {

  case class Country(code: String,
                     name: String,
                     continent: String,
                     region: String,
                     surfacearea: Float,
                     indepyear: Option[Int],
                     population: Int,
                     lifeexpectancy: Option[Float],
                     gnp: BigDecimal,
                     gnpold: Option[BigDecimal],
                     localname: String,
                     governmentform: String,
                     headofstate:  Option[String],
                     capital: Option[Int],
                     code2: String
                    )


//  code2 character(2) NOT NULL
}
