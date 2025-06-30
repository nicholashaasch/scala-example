import dao.JobDao
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import job.{DeleteMyCompanyJobsJob, QuartzJob, RunDataMapJob}
import org.flywaydb.core.Flyway
import org.quartz.impl.StdSchedulerFactory
import org.quartz._
import org.slf4j.LoggerFactory
import rest.JobRestService
import scalikejdbc.ConnectionPool

object Application extends App {
  private val logger = LoggerFactory.getLogger(classOf[App])
  val flyway: Flyway = Flyway.configure.dataSource("jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password").load
  flyway.migrate()

  logger.info("Initing database")
  val connectPoolName = "foo"
  Class.forName("org.postgresql.Driver")
  ConnectionPool.add(connectPoolName, "jdbc:postgresql://192.168.0.243:5432/postgres", "postgres", "password")

  logger.info("Wiring")
  val jobDao = new JobDao(connectPoolName)
  val jobRestService = new JobRestService(jobDao)
  val complicationJob = new DeleteMyCompanyJobsJob(jobDao)

  logger.info("Starting web server")
  Javalin.create { config =>
    config.router.apiBuilder(() => {
      crud("/job/{jobId}", jobRestService)
    })
  }.start(8080)

  logger.info("Scheduling jobs")
  val jobs: Seq[QuartzJob] = Seq(
    QuartzJob(complicationJob, "1 * * * * ?")
  )

  val schedulerFactory = new StdSchedulerFactory();
  val scheduler = schedulerFactory.getScheduler
  jobs.foreach{ quartzJob:QuartzJob =>
    val dataMap = new JobDataMap()
    dataMap.put("job", quartzJob.job)
    val job: JobDetail = JobBuilder
      .newJob(classOf[RunDataMapJob])
      .setJobData(dataMap)
      .build
    val trigger = TriggerBuilder.newTrigger()
      .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.schedule))
      .build();
    scheduler.scheduleJob(job, trigger)
  }
  scheduler.start()
}