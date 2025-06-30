package rest

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context


class RestService extends CrudHandler{

  override def create(context: Context): Unit = {
    context.res().setStatus(501)
    context.res().setHeader("Access-Control-Allow-Origin", "*")
  }

  override def delete(context: Context, s: String): Unit = {
    context.res.setStatus(501)
    context.res().setHeader("Access-Control-Allow-Origin", "*")
  }

  override def getAll(context: Context): Unit = {
    context.res.setStatus(501)
    context.res().setHeader("Access-Control-Allow-Origin", "*")
  }

  override def getOne(context: Context, s: String): Unit = {
    context.res.setStatus(501)
    context.res().setHeader("Access-Control-Allow-Origin", "*")
  }

  override def update(context: Context, s: String): Unit = {
    context.res.setStatus(501)
    context.res().setHeader("Access-Control-Allow-Origin", "*")
  }
}
