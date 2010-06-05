package ro.gagarin.snippet

import java.util.{Date, Locale}
import _root_.ro.gagarin.model.wsSession
import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.model.adminService
import _root_.net.liftweb.http.SHtml._
import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.util.Helpers._

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

