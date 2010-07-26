package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.ro.gagarin.model.authService
import _root_.ro.gagarin.model.{wsSession, SessionInfo}
import _root_.net.liftweb.http.js.JsCmd
import _root_.net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById, Run,ReplaceOptions}
import _root_.ro.gagarin.view.TemplateStore

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
    ))
  }
  
}

