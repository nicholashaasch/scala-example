import controller.MinimalRoutes
import dao.EmailDao
import dao.controller.MemberDao
import domain.{Email, Member}
import org.slf4j.LoggerFactory
import scalikejdbc.{ConnectionPool, NamedDB, sqls}

object MyApp extends cask.Main {
  private val logger = LoggerFactory.getLogger(classOf[MemberDao])
  logger.error("Hello world")

  val allRoutes = Seq(MinimalRoutes())

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


  val memberDao = new MemberDao
  val emailDao = new EmailDao

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





