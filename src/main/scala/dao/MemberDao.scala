package dao

import domain.{Email, Member}
import scalikejdbc.orm.CRUDMapper
import scalikejdbc.orm.timstamps.TimestampsFeature
import scalikejdbc.{DBSession, WrappedResultSet, autoConstruct}

class MemberDao () {
  object Member extends CRUDMapper[Member] with TimestampsFeature[Member] {
    override lazy val tableName = "member"
    override val connectionPoolName = "foo"
    lazy val defaultAlias = createAlias("m")
    val email = hasOne[Email](Email, (m, e) => m.copy(email = e))

    override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Member]): Member = autoConstruct(rs, n, "email")
  }
//  object MemberMapper extends CRUDMapper[Member] with TimestampsFeature[Member] {
//    override val connectionPoolName = "foo"
//    lazy val defaultAlias = createAlias("m")
//    val email = hasOne[Email](Email, (m, e) => m.copy(email = e))
//
//    override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Member]): Member = autoConstruct(rs, n, "email")
//  }


  def create(name:String)(implicit session: DBSession):Member= {
    val id = Member.createWithNamedValues(Member.column.name -> name)
    Member.findById(id).get
  }

  def findAll()(implicit session: DBSession):List[Member]= {
    Member.findAll()
  }

  def findById(id:Long)(implicit session: DBSession)= {
    Member.findById(id)
  }

  def findAllById(ids:Seq[Long])(implicit session: DBSession):List[Member] = {
    Member.findAllByIds(ids:_*)
    }

  def findWhereNameEquals(name:String)(implicit session: DBSession):List[Member]= {
    Member.where("name" -> name).apply()
  }

  def updateName(memberId:Long, name:String): Unit = {
    Member.updateById(memberId).withAttributes("name" -> name)
  }
}





