package job

import dao.JobDao
import scalikejdbc.NamedDB

class DeleteMyCompanyJobsJob(jobDao: JobDao) extends Runnable {

  override def run(): Unit = {
    val jobs = NamedDB(jobDao.connectPoolName).readOnly { implicit session =>
      jobDao.findAll()
    }
    val jobsToDelete = jobs.filter(_.company == "My company")
    NamedDB(jobDao.connectPoolName).localTx { implicit session =>
      jobsToDelete.foreach { job =>
        jobDao.deleteById(job.id.get)
      }
    }
  }
}