import dao.{EmailDao, JobDao, MemberDao}
import domain.{Email, Member}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import io.javalin.apibuilder.CrudHandler
import io.javalin.http.{Context, Handler}
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import rest.JobRestService
import scalikejdbc.{ConnectionPool, NamedDB}

object MyApp extends App {
  private val logger = LoggerFactory.getLogger(classOf[MemberDao])
  val flyway: Flyway = Flyway.configure.dataSource("jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password").load
  flyway.migrate()

  logger.error("Hello world")

  val memberDao = new MemberDao
  val emailDao = new EmailDao
  val jobDao = new JobDao


   val jobRestService = new JobRestService(jobDao)


  val app = Javalin.create { config =>

    config.router.apiBuilder(() => {
      crud("/job/{jobId}", jobRestService)
    })
  }.start(8080)


  Class.forName("org.postgresql.Driver")
  ConnectionPool.add("foo", "jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password")

  // ### Insert rows ###
  val ids = Seq("Alice", "Bob", "Chris") map { name =>
    // insert into member (name, created_at, updated_at) values ('Alice', '2024-05-11 14:52:27.13', '2024-05-11 14:52:27.13');

    NamedDB("foo").localTx{ implicit session =>
      memberDao.create(name)

    }
  }

  // ### Find all rows ###
  // select m.id as i_on_m, m.name as n_on_m, m.created_at as ca_on_m, m.updated_at as ua_on_m from member m order by m.id;
  val member2: Seq[Member] = NamedDB("foo").readOnly{ implicit session =>
      val allMembers1: Seq[Member] = memberDao.findAll()

  println(allMembers1)
//  // select m.id as i_on_m, m.name as n_on_m, m.created_at as ca_on_m, m.updated_at as ua_on_m from member m where m.id in (1, 2, 3);
  val allMembers2: Seq[Member] = memberDao.findAllById(ids.map(_.id))
//
  // ### Run queries with where conditions ###
  // Quick way but less type-safety
  // select m.id as i_on_m, m.name as n_on_m, m.created_at as ca_on_m, m.updated_at as ua_on_m from member m where m.name = 'Alice' order by m.id;
  val member1: Seq[Member] = memberDao.findWhereNameEquals("Alice")
  // Types-safe query builder
  // select m.id as i_on_m, m.name as n_on_m, m.created_at as ca_on_m, m.updated_at as ua_on_m from member m where name = 'Alice' order by m.id;
    //Member.where(sqls.eq(m.name, "Alice")).apply()
    allMembers2
  }
//
  val memberId = member2.head.id

  // ### Run join queries ###
  val e = Email.column
  // insert into member_email (member_id, address) values (1, 'a@example.com');
  NamedDB("foo").localTx{ implicit session =>
    //Email.createWithNamedValues(e.memberId -> memberId, e.address -> "a@example.com")
    emailDao.create(memberId, "a@example.com")
//
//  // Note that member3.email exists while it does not in member1,2
//  // select m.id as i_on_m, m.name as n_on_m, m.created_at as ca_on_m, m.updated_at as ua_on_m , me.id as i_on_me, me.member_id as mi_on_me, me.address as a_on_me from member m left join member_email me on m.id = me.member_id where name = 'Alice' order by m.id;
  //val member3 = Member.joins(Member.email).where(sqls.eq(m.name, "Alice")).apply()
//
//  // ### Update/delete rows ###
//  // update member set updated_at = '2024-05-11 14:52:27.188', name = 'Ace' where id = 1;
    memberDao.updateName(memberId, "Ace")
//  // delete from member where id = 1;
   // memberDao.deleteById(memberId)
  }
}





case class MemberPost(name:String)

object MemberPost {
  implicit val encPerson: Encoder[MemberPost] = deriveEncoder[MemberPost]
  implicit val decPerson: Decoder[MemberPost] = deriveDecoder[MemberPost]

}