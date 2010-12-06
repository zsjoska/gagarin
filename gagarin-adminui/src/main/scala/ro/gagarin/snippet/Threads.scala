package ro.gagarin.snippet

import java.util.{Date, Locale}
import ro.gagarin.model.wsSession
import ro.gagarin.model.adminService
import net.liftweb.http.SHtml._
import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.util.Helpers._

class Threads {
    def __(text: String) = if(text == null) "" else text
    def __(text: Any) = if(text == null) "" else text.toString
	
  def show(in: NodeSeq): NodeSeq  = {
    val jobs = adminService.getThreads
    jobs.flatMap( s => 
    	bind("thread", in,
          "name" -> Text(__(s.getName())),
          "jobname" -> {
              if(s.getActiveJob()!=null)
        	  Text(__(s.getActiveJob().getName()))
              else 
        	  Text("")
          },
          "jobpercent" -> {
              if(s.getActiveJob()!=null)
        	  Text(__(s.getActiveJob().getPercentComplete()))
              else 
        	  Text("")
          }
      ))
    }
}

