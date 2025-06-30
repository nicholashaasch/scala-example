import dao.JobDao
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import rest.JobRestService
import scalikejdbc.ConnectionPool

object Application extends App {
  private val logger = LoggerFactory.getLogger(classOf[App])
  val flyway: Flyway = Flyway.configure.dataSource("jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password").load
  flyway.migrate()s

  logger.info("Initing database")
  val connectPoolName = "foo"
  Class.forName("org.postgresql.Driver")
  ConnectionPool.add(connectPoolName, "jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password")

  logger.info("Wiring")
  val jobDao = new JobDao(connectPoolName)
  val jobRestService = new JobRestService(jobDao)


  logger.info("Starting web server")
  val app = Javalin.create { config =>
    config.router.apiBuilder(() => {
      crud("/job/{jobId}", jobRestService)
    })
  }.start(8080)


}