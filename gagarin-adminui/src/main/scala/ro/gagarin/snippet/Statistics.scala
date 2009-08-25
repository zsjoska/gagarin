package ro.gagarin.snippet

import java.util.{Date, Locale}
import _root_.ro.gagarin.model.wsSession
import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.model.userService
import _root_.net.liftweb.http.SHtml._
import _root_.scala.xml.{NodeSeq, Text, Group, Node}

class Statistics {
	
  def show = {
	val logs = userService.getStatistics(null)
    <table border="1" cellspacing="0">
    <tr>
    <th>Name</th>
    <th>Count</th>
    <th>Total duration</th>
    <th>Minimum</th>
    <th>Maximum</th>
    </tr>
      {logs.flatMap( s => <tr>
                      		<td>{Text(s.getName())}</td>
                      		<td>{Text(s.getCount().toString)}</td>
                      		<td>{Text(s.getTotalDuration().toString)}</td>
                      		<td>{Text(s.getMin().toString)}</td>
                      		<td>{Text(s.getMax().toString)}</td>
                      	   </tr>)}
    </table>
    }
  
}

