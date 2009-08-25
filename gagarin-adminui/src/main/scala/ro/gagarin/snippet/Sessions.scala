package ro.gagarin.snippet

import java.util.{Date, Locale}
import _root_.ro.gagarin.model.wsSession
import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.model.userService
import _root_.net.liftweb.http.SHtml._
import _root_.scala.xml.{NodeSeq, Text, Group, Node}

class Sessions {
	
  def show = {
	val logs = userService.getSessionList
    <table border="1" cellspacing="0">
    <tr>
    <th>Session identifier</th>
    <th>Username</th>
    <th>Expiration date</th>
    <th>Session reason</th>
    <th>Terminate</th>
    </tr>
      {logs.flatMap( s => <tr>
                      		<td>{Text(s.getSessionid())}</td>
                      		<td>{Text(s.getUsername())}</td>
                      		<td>{Text(s.getReason())}</td>
                      		<td>{Text(new Date(s.getExpires().longValue()).toString)}</td>
                      		<td>{link("/sessions", () => { userService.logoutSession(s.getSessionid()) }, Text("Terminate"))}</td>
                      	   </tr>)}
    </table>
    }
  
}

