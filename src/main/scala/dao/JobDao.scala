package dao

import domain.Job
import scalikejdbc.orm.CRUDMapper
import scalikejdbc.{DBSession, WrappedResultSet, autoConstruct}

class JobDao(val connectPoolName:String) {
  object Job extends CRUDMapper[Job]{
    override lazy val tableName = "job"
    override val connectionPoolName: String = connectPoolName
    lazy val defaultAlias = createAlias("j")

    override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Job]): Job = autoConstruct(rs, n)
  }

  def create(job:Job)(implicit session: DBSession):Job= {
    val id = Job.createWithNamedValues(
      Job.column.company -> job.company,
      Job.column.description -> job.description
    )
    Job.findById(id).get
  }

  def findAll()(implicit session: DBSession):List[Job]= {
    Job.findAll()
  }

  def findById(id:Long)(implicit session: DBSession)= {
    Job.findById(id)
  }

  def findAllById(ids:Seq[Long])(implicit session: DBSession):List[Job] = {
    Job.findAllByIds(ids:_*)
  }

  def deleteById(id:Long)(implicit session: DBSession) = {
    Job.deleteById(id)
  }
}





