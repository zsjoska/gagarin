package ro.gagarin.snippet

import java.util.{Date, Locale}
import ro.gagarin.model.wsSession
import ro.gagarin.model.adminService
import net.liftweb.http.SHtml._
import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.util.Helpers._

class Sessions {
	
  def show(in: NodeSeq): NodeSeq  = {
    val logs = adminService.getSessionList
    logs.flatMap( s =>
    	bind("session", in,
          "sessionid" -> Text(s.getSessionid()),
          "username" -> Text(s.getUsername()),
          "reason" -> Text(s.getReason()),
          "date" -> Text(new Date(s.getExpires().longValue()).toString),
          "logout" -> link("/sessions", () => { adminService.logoutSession(s.getSessionid()) }, Text("Terminate"))
    	))
    }
}

