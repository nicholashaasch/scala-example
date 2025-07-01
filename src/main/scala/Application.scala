import dao.JobDao
import db.DB
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import job.DeleteMyCompanyJobsJob
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import rest.JobRestService
import scalikejdbc.ConnectionPool
import service.{KafkaService, QuartzService}

object Application extends App {
  private val logger = LoggerFactory.getLogger(classOf[App])
  val flyway: Flyway = Flyway.configure.dataSource(DB.url, DB.username, DB.password).load
  flyway.migrate()

  logger.info("Initing database")
  Class.forName("org.postgresql.Driver")
  ConnectionPool.add(DB.connectionPoolName, DB.url, DB.username, DB.password)

  logger.info("Wiring")
  val jobDao = new JobDao(DB.connectionPoolName)
  val jobRestService = new JobRestService(jobDao)
  val deleteMyCompanyJobsJob = new DeleteMyCompanyJobsJob(jobDao)
  val quartzService = new QuartzService(deleteMyCompanyJobsJob)

  logger.info("Starting web server")
  Javalin.create { config =>
    config.router.apiBuilder(() => {
      crud("/job/{jobId}", jobRestService)
    })
  }.start(8080)

  logger.info("Scheduling jobs")
  quartzService.schedule()

  logger.info("Starting Kafka")
  val kafkaService = new KafkaService
  kafkaService.run()
}