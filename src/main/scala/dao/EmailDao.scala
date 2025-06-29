package dao

import domain.{Email, Member}
import scalikejdbc.DBSession

class EmailDao() {

  def create(memberId:Long, email:String)(implicit session: DBSession):Long= {
    Email.createWithNamedValues(Email.column.memberId -> memberId, Email.column.address -> email)
  }
}





