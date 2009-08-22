package ro.gagarin.snippet

import java.util.{Date, Locale}
import _root_.ro.gagarin.model.wsSession
import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.model.WebServiceClient._
import _root_.net.liftweb.http.SHtml._
import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.util.Helpers._

class Config {
	
  def show = {
	val config = Buffer(getUserService.getConfigEntries(wsSession.session))
	var name:String = null
	var value:String = null
    <table border="1" cellspacing="0">
    <script type="text/javascript">
    {"function showSet(x){ $(x).show();}"}
    </script>
      {config.flatMap( c => {
        val id = nextFuncName
		<tr> 
			<td>{Text(c.getConfigScope().name())}</td>
			<td>{Text(c.getConfigName())}</td>
			<td>{
			  if(ConfigScope.LOCAL.equals(c.getConfigScope()))
				  Text(c.getConfigValue())
              else 
			  text(c.getConfigValue(), (x)=> {
			  name=c.getConfigName(); value=x
			}) %
				("onkeypress" -> ("showSet('#" + id + "')" )) %
				("onchange" -> ("showSet('#" + id + "')" ))
			}</td>
			<td style="width: 50px;">{submit("Set", () => {
				val config = new WsConfig()
				config.setConfigName(name)
				config.setConfigValue(value)
				getUserService.setConfigEntry(wsSession.session,config)
            })% ("style" -> "display: none;") % ("id" -> id )}</td>
		</tr>})}
    </table>
    }
  
}

