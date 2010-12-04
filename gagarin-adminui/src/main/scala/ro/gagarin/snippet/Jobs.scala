package ro.gagarin.snippet

import java.util.{Date, Locale}
import ro.gagarin.model.wsSession
import ro.gagarin.model.adminService
import net.liftweb.http.SHtml._
import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.util.Helpers._

class Jobs {
	
  def show(in: NodeSeq): NodeSeq  = {
    val logs = adminService.getJobs
    logs.flatMap( s => 
    	bind("job", in,
          "name" -> Text(s.getName()),
          "lastExec" -> Text(s.getLastExecution().toString),
          "nextExec" -> Text(s.getNextExecution().toString),
          "percent" -> Text(s.getPercentComplete().toString),
          "period" -> Text(s.getPeriod().toString)
      ))
    }
  
}

