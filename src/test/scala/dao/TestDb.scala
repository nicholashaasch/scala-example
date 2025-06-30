package dao

import org.flywaydb.core.Flyway
import scalikejdbc.{ConnectionPool, NamedDB}

object TestDb {

  val connectionPoolName = "foo"
  Class.forName("org.h2.Driver")
  ConnectionPool.add(connectionPoolName, "jdbc:h2:mem:hello;MODE=PostgreSQL", "user", "pass")

  NamedDB(connectionPoolName).localTx { implicit session =>
    val flyway: Flyway = Flyway.configure.dataSource("jdbc:h2:mem:hello;MODE=PostgreSQL", "user", "pass").load
    flyway.migrate()
  }
}
