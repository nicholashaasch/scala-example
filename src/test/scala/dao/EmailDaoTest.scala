package dao

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.slf4j.LoggerFactory
import scalikejdbc.NamedDB

class EmailDaoTest extends AnyFunSuite {
  val testDb = TestDb
  val subject = new EmailDao
  private val logger = LoggerFactory.getLogger(classOf[EmailDaoTest])

  test("create and find all") {
    NamedDB("foo").localTx { implicit session =>
      subject.create(1, "a@b.com")
      val foundAll = subject.findAll()
      assert(foundAll.size == 1)
      foundAll.head.address shouldBe "a@b.com"
    }
  }
}