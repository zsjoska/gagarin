package ro.gagarin.snippet

import java.util.{Date, Locale}
import _root_.ro.gagarin.model.wsSession
import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.model.WebServiceClient._
import _root_.net.liftweb.http.SHtml._
import _root_.scala.xml.{NodeSeq, Text, Group, Node}

class Logs {
	
  def show = {
	val logs = Buffer(getUserService.getLogEntries(wsSession.session, null))
    <table>
      {logs.flatMap( u => <tr>
                      		<td>{Text(new Date(u.getDate().longValue()).toString)}</td>
                      		<td>{Text(u.getLogLevel())}</td>
                      		<td>{if(u.getUser()!=null)Text(u.getUser().getUsername()) else Text("NULL")}</td>
                      		<td>{Text(u.getMessage())}</td>
                      	   </tr>)}
    </table>
    }
  
}

