//package controller
//
//import dao.MemberDao
//import domain.{JsonMapper, Member}
//import scalikejdbc.NamedDB
//import upickle.default._
//case class MemberController(memberDao: MemberDao)(implicit cc: castor.Context,
//                           log: cask.Logger) extends cask.Routes{
//
//
//  @cask.get("/member")
//  def get(request: cask.Request) = {
//    val membersJson = NamedDB("foo").readOnly{ implicit session =>
//      JsonMapper.serialize(memberDao.findAll())
//    }
//
//    cask.Response(membersJson, headers = Seq("Access-Control-Allow-Origin" -> "*"))
//
//  }
//
//  @cask.postJson("/member")
//  def post(name: String) = {
//     val memberJson = NamedDB("foo").localTx{ implicit session =>
//       JsonMapper.serialize(memberDao.create(name))
//     }
//    cask.Response(memberJson, headers = Seq("Access-Control-Allow-Origin" -> "*"))
//  }
//
//  initialize()
//}