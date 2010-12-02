package ro.gagarin.snippet

import java.util.{Date, Locale}
import ro.gagarin.model.wsSession
import ro.gagarin.model.adminService
import net.liftweb.http.SHtml._
import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.util.Helpers._

class Statistics {
	
  def show(in: NodeSeq): NodeSeq  = {
    val logs = adminService.getStatistics(null)
    logs.flatMap( s => 
    	bind("statistic", in,
          "name" -> Text(s.getName()),
          "count" -> Text(s.getCount().toString),
          "totDuration" -> Text(s.getTotalDuration().toString),
          "avg" -> Text(s.getAverage().toString),
          "min" -> Text(s.getMin().toString),
          "max" -> Text(s.getMax().toString)
      ))
    }
  
}

