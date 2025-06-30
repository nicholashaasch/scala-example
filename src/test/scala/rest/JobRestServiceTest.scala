package rest

import dao.{JobDao, TestDb}
import domain.Job
import io.circe.jawn.decode
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import scalaj.http.{Http, HttpOptions}

class JobRestServiceTest extends AnyFunSuiteLike {
  val testDb = TestDb
  val jobDao = new JobDao(testDb.connectionPoolName)
  val subject = new JobRestService(jobDao)

  test("create and find all") {
    Javalin.create { config =>
      config.router.apiBuilder(() => {
        crud("/job/{jobId}", subject)
      })
    }.start(9090)

    val response = Http("http://localhost:9090/job").postData("""{"company":"My company","description":"desc"}""")
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString
    val job = decode[Job](response.body).toOption.get

    val foundJobsResponse = Http("http://localhost:9090/job")
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString

    val jobs = decode[Seq[Job]](foundJobsResponse.body).getOrElse(Seq())
    jobs shouldBe Seq(job)
  }
}
