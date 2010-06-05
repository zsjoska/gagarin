package ro.gagarin.snippet

import java.util.{Date, Locale}
import _root_.ro.gagarin.model.wsSession
import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.model.adminService
import _root_.net.liftweb.http.SHtml._
import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.util.Helpers._

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

