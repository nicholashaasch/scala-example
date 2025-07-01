package service

import job.{DeleteMyCompanyJobsJob, QuartzJob, RunDataMapJob}
import org.quartz.{CronScheduleBuilder, JobBuilder, JobDataMap, JobDetail, TriggerBuilder}
import org.quartz.impl.StdSchedulerFactory

class QuartzService(deleteMyCompanyJobsJob:DeleteMyCompanyJobsJob) {

  def schedule(): Unit = {
    val jobs: Seq[QuartzJob] = Seq(
      QuartzJob(deleteMyCompanyJobsJob, "1 * * * * ?")
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
}
