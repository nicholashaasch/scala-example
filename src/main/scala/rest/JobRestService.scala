package rest

import dao.JobDao
import domain.Job
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps
import io.javalin.http.Context
import scalikejdbc.NamedDB

class JobRestService(jobDao:JobDao) extends RestService {

  override def getAll(context: Context): Unit = {
    val jobJson = NamedDB("foo").readOnly{ implicit session =>
      jobDao.findAll().asJson.spaces2
    }
    val response = context.res
    response.addHeader("Access-Control-Allow-Origin", "*")
    context.json(jobJson)
  }

  override def create(context: Context): Unit = {
        val job:Job = try {
          decode[Job](context.body()).getOrElse(throw new Exception())
        }catch {
          case e:Exception => {
            throw e;
          }
        }
        val json = NamedDB("foo").localTx { implicit session =>
          jobDao.create(job).asJson.spaces2
        }
        context.json(json)
      }

}
