package dao

import domain.Job
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.slf4j.LoggerFactory
import scalikejdbc.NamedDB

class JobDaoTest extends AnyFunSuite {
  val testDb = TestDb
  val subject = new JobDao(testDb.connectionPoolName)

  test("create and find all") {
    NamedDB(testDb.connectionPoolName).localTx { implicit session =>
      val job = Job(None, "My company", "description")
      subject.create(job)
      val foundAll = subject.findAll()
      assert(foundAll.size == 1)
      foundAll.head.company shouldBe "My company"
      foundAll.head.description shouldBe "description"
    }
  }

  test("delete"){
    NamedDB(testDb.connectionPoolName).localTx { implicit session =>
      val job = subject.create( Job(None, "My company", "description"))
      subject.deleteById(job.id.get)
      subject.findById(job.id.get) shouldBe None
    }
  }
}