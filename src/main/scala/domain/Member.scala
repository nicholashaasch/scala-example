package domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Member(
                   id: Long,
                   name: Option[String],
                   email: Option[Email] = None,
                 )

object Member {
  implicit val encoder: Encoder[Member] = deriveEncoder[Member]
  implicit val decoder: Decoder[Member] = deriveDecoder[Member]

}