package ro.gagarin.view

import _root_.scala.xml.{NodeSeq, Text,Unparsed, Group, Node, Elem}
import _root_.scala.collection.mutable.HashMap
import _root_.net.liftweb.util.Helpers._

object TemplateStore {
  
  val templateStore = new HashMap[String, Elem]()
  
  def storeReplace(template: NodeSeq): NodeSeq = 
    template.flatMap{ x => x match {
      case elem: Elem if ((elem \ "@id").size == 1 ) => {
        templateStore.put((elem \ "@id").text, elem)
        Elem(elem prefix, elem label, elem attributes, elem scope, Text("Placeholder"))
      }
      case text: Text => Seq.empty
      case any => { println(any.getClass+":"+any); Seq.empty }
    }}

  def storeReplaceElem(template: NodeSeq): Elem = storeReplace(template).first.asInstanceOf[Elem]

  def getTemplate(id:String):Elem = {
    templateStore.get(id).get 
  }
  def getTemplate(id:String,sufix:String):Elem = {
    templateStore.get(id).get % ("id" -> (id+sufix))
  }
}
