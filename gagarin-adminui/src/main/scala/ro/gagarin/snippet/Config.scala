package ro.gagarin.snippet

import java.util.{Date, Locale}
import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById, Run,ReplaceOptions}
import ro.gagarin.model.wsSession
import ro.gagarin.model.adminService
import ro.gagarin.WsConfig
import ro.gagarin.ConfigScope

class Config {
	
  def show(in: NodeSeq): NodeSeq  = {
      val config = adminService.getConfigEntries
      config.flatMap( c => {
        bind("config", in,
           "scope" -> Text(c.getConfigScope().name()),
           "name" -> Text(c.getConfigName()),
           "value" -> {
		if(ConfigScope.LOCAL.equals(c.getConfigScope()))
		  Text(c.getConfigValue())
		else 
		  ajaxText(c.getConfigValue(), (x)=> {
		    c.setConfigValue(x)
		    println("new value:" + x)
		    Noop;
		  }) 
           },
           "set" -> {
		if(ConfigScope.LOCAL.equals(c.getConfigScope()))
		    Text("")
		else
		    ajaxButton("Set", () => {
			adminService.setConfigEntry(c)
			Alert(c.getConfigName() + " changed to " + c.getConfigValue);
	})})
      })
  }
}

