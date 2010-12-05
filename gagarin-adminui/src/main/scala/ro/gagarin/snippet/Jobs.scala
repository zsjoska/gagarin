package ro.gagarin.snippet

import java.util.{Date, Locale}
import ro.gagarin.model.wsSession
import ro.gagarin.model.adminService
import net.liftweb.http.SHtml._
import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.util.Helpers._

class Jobs {
    def __(text: String) = if(text == null) "" else text
    def __(text: Any) = if(text == null) "" else text.toString
	
  def show(in: NodeSeq): NodeSeq  = {
    val logs = adminService.getJobs
    logs.flatMap( s => 
    	bind("job", in,
          "name" -> Text(__(s.getName())),
          "lastExec" -> Text(__(s.getLastExecution())),
          "nextExec" -> Text(__(s.getNextExecution())),
          "percent" -> Text(__(s.getPercentComplete())),
          "period" -> Text(__(s.getPeriod()))
      ))
    }
  
}

