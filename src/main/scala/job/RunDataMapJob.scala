package job

import org.quartz.{Job, JobExecutionContext}

case class QuartzJob(job: Runnable, schedule:String )

class RunDataMapJob extends Job {

  override def execute(jobExecutionContext: JobExecutionContext): Unit = {
    jobExecutionContext.getJobDetail.getJobDataMap.get("job").asInstanceOf[Runnable].run()
  }
}






