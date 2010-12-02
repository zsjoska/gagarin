package ro.gagarin.snippet

import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.http._
import net.liftweb.http.S
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import net.liftweb.util._
import ro.gagarin.model.authService
import ro.gagarin.model.{wsSession, SessionInfo}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById, Run,ReplaceOptions}
import ro.gagarin.view.TemplateStore

class Test {

  val names = Array("Daniel", "Chris", "Joseph")
  
  def link(in: NodeSeq): NodeSeq  = {
    a(() => replaceTable,Text("Test"))
  } 
  
  def replaceTable: JsCmd = {
    Replace("test-table", TemplateStore.getTemplate("test-table"))
  }
  
  def table(in: NodeSeq): NodeSeq  = {
    names.flatMap( u => bind("table", in, 
	   "value" -> Text(u)
    )).toSeq
  }
  
}

