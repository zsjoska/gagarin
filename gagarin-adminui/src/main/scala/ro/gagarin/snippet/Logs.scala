package ro.gagarin.snippet

import java.util.{Date, Locale}
import ro.gagarin.model.wsSession
import ro.gagarin.model.adminService
import net.liftweb.http.SHtml._
import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.util.Helpers._

class Logs {

  // TODO: put this in a utility
  def __(text: String) = if(text == null) "" else text

  def show(in: NodeSeq): NodeSeq  = {
    val logs = adminService.getLogEntries(null)
      logs.flatMap( u =>
        bind("log", in,
             "date" -> Text(new Date(u.getDate().longValue()).toString),
             "level" -> Text(u.getLogLevel()),
             "user" -> {if(u.getUser()!=null)Text(u.getUser().getUsername()) else Text("NULL")},
             "message" -> Text(u.getMessage())
        ))
    }
  
}

