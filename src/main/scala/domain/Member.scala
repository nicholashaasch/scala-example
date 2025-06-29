package domain

import scalikejdbc.{WrappedResultSet, autoConstruct}
import scalikejdbc.orm.CRUDMapper
import scalikejdbc.orm.timstamps.TimestampsFeature

import java.time.ZonedDateTime

case class Member(
                   id: Long,
                   name: Option[String],
                   createdAt: ZonedDateTime,
                   updatedAt: Option[ZonedDateTime],
                   email: Option[Email] = None,
                 )

