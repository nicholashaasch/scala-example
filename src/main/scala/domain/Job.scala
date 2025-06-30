package domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Job(
                   id: Option[Long],
                   company: String,
                   description: String,
                 )

object Job {
  implicit val encoder: Encoder[Job] = deriveEncoder[Job]
  implicit val decoder: Decoder[Job] = deriveDecoder[Job]
}