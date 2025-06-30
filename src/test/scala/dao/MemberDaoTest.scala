package dao

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.slf4j.LoggerFactory
import scalikejdbc.NamedDB

class MemberDaoTest extends AnyFunSuite {
  val testDb = TestDb
  val subject = new MemberDao
  private val logger = LoggerFactory.getLogger(classOf[MemberDaoTest])

  test("create and find all") {
    NamedDB("foo").localTx { implicit session =>
      subject.create("test")
      val foundAll = subject.findAll()
      assert(foundAll.size == 1)
      foundAll.head.name.get shouldBe "test"
    }
  }

  test("find where name equals") {
    NamedDB("foo").localTx { implicit session =>
      subject.create("test2")
      subject.create("my other name")
      val foundAll = subject.findWhereNameEquals("test2")
      assert(foundAll.size == 1)
      foundAll.head.name.get shouldBe "test2"
    }
  }

  test("update name") {
    NamedDB("foo").localTx { implicit session =>
      val member = subject.create("test")
      subject.updateName(memberId = member.id, name = "new name")
      val found = subject.findById(member.id).get
      found.name.get shouldBe "test"
    }
  }
}