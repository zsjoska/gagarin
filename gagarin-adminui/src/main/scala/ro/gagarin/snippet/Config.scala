package ro.gagarin.snippet

import java.util.{Date, Locale}
import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import ro.gagarin.model.wsSession
import ro.gagarin.model.adminService
import ro.gagarin.WsConfig
import ro.gagarin.ConfigScope

class Config {
	
  def show = {
	val config = adminService.getConfigEntries
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
				adminService.setConfigEntry(config)
            })% ("style" -> "display: none;") % ("id" -> id )}</td>
		</tr>})}
    </table>
    }
  
}

