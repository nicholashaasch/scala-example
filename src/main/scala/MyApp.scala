import dao.{EmailDao, JobDao, MemberDao}
import domain.{Email, Job, Member}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Json, jawn}
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import scalikejdbc.{ConnectionPool, NamedDB}
import zio.ZIOAppDefault
import zio.http._

object MyApp  extends ZIOAppDefault {
  private val logger = LoggerFactory.getLogger(classOf[MemberDao])
  val flyway: Flyway = Flyway.configure.dataSource("jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password").load
  flyway.migrate()

  logger.error("Hello world")

  val memberDao = new MemberDao
  val emailDao = new EmailDao
  val jobDao = new JobDao

//  val allRoutes = Seq(MinimalRoutes(), MemberController(memberDao))
  val routes =
    Routes(
      Method.GET / Root -> handler(Response.text("Greetings at your service")),
      Method.GET / "member" -> handler { (req: Request) =>
        val membersJson = NamedDB("foo").readOnly{ implicit session =>
          memberDao.findAll().asJson.spaces2
        }
        val resp = Response.json(membersJson)
        resp.copy(headers = resp.headers ++ Headers("Access-Control-Allow-Origin" -> "*"))
      },
      Method.POST / "member" -> handler { (req: Request) =>
        req.body.asString.map { ab =>
          val memberPost:MemberPost = try {
            jawn.decode[MemberPost](ab).getOrElse(throw new Exception())
          }catch {
            case e:Exception => {
              e.printStackTrace()
              throw e;
            }
          }
          val memberJson = NamedDB("foo").localTx { implicit session =>
            memberDao.create(memberPost.name).asJson.spaces2
          }
          val resp = Response.json(memberJson)
          resp.copy(headers = resp.headers ++ Headers("Access-Control-Allow-Origin" -> "*"))
        }
      }.sandbox,
      Method.GET / "job" ->  handler { (req: Request) =>
        val json = NamedDB("foo").readOnly{ implicit session =>
          jobDao.findAll().asJson.spaces2
        }
        val resp = Response.json(json)
        resp.copy(headers = resp.headers ++ Headers("Access-Control-Allow-Origin" -> "*"))
      },
      Method.POST / "job" -> handler { (req: Request) =>
        req.body.asString.map { ab =>
          val job:Job = try {
            jawn.decode[Job](ab).getOrElse(throw new Exception())
          }catch {
            case e:Exception => {
              e.printStackTrace()
              throw e;
            }
          }
          val json = NamedDB("foo").localTx { implicit session =>
            jobDao.create(job).asJson.spaces2
          }
          val resp = Response.json(json)
          resp.copy(headers = resp.headers ++ Headers("Access-Control-Allow-Origin" -> "*"))
        }
      }.sandbox,
    )




  override def run = Server.serve(routes).provide(Server.default)

    // ### Database connection ###
    Class.forName("org.postgresql.Driver")

  //Class.forName("org.h2.Driver")

  //ConnectionPool.singleton("jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password")
  ConnectionPool.add("foo", "jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password")
  //ConnectionPool.singleton("jdbc:h2:mem:hello;MODE=PostgreSQL", "user", "pass")
  //ConnectionPool.add("foo", "jdbc:h2:mem:hello;MODE=PostgreSQL", "user", "pass")

  //Class.forName("org.h2.Driver")
  //implicit val session: DBSession = AutoSession

  // ### Create tables ###
//  sql"""create table member (
//    id serial not null primary key,
//    name varchar(64),
//    created_at timestamp not null,
//    updated_at timestamp
//  )""".execute.apply()
//  sql"""create table member_email (
//    id serial not null primary key,
//    member_id int not null,
//    address varchar(256) not null
//  )""".execute.apply()



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