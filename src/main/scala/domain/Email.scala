package domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import scalikejdbc.{WrappedResultSet, autoConstruct}
import scalikejdbc.orm.CRUDMapper

case class Email(
                  id: Long,
                  memberId: Long,
                  address: String,
                )

object Email extends CRUDMapper[Email] {
  override val connectionPoolName = "foo"

  override lazy val tableName = "member_email"
  lazy val defaultAlias = createAlias("me")

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Email]): Email = autoConstruct(rs, n)

  implicit val encoder: Encoder[Email] = deriveEncoder[Email]
  implicit val decoder: Decoder[Email] = deriveDecoder[Email]

}