package job

import dao.{JobDao, TestDb}
import domain.Job
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import scalikejdbc.NamedDB

class DeleteMyCompanyJobsJobTest extends AnyFunSuiteLike {

  val testDb = TestDb
  val jobDao = new JobDao(testDb.connectionPoolName)
  val subject = new DeleteMyCompanyJobsJob(jobDao)

  test("create and find all") {
    val (job, job2) = NamedDB(testDb.connectionPoolName).localTx { implicit session =>
      val job = jobDao.create(Job(None, "My company", "description"))
      val job2 =jobDao.create(Job(None, "Company", "description"))

      (job, job2)
    }

    subject.run()

    NamedDB(testDb.connectionPoolName).readOnly { implicit session =>
      jobDao.findById(job.id.get) shouldBe None
      jobDao.findById(job2.id.get).get shouldBe job2
    }
  }

}
