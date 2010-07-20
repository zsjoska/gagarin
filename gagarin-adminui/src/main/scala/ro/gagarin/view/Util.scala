package ro.gagarin.view

import _root_.scala.xml.{NodeSeq, Text,Unparsed, Group, Node, Elem}
import _root_.scala.collection.mutable.HashMap

object TemplateStore {
  
  val templateStore = new HashMap[String, Node]()
  
  def storeReplace(template: NodeSeq): NodeSeq = {
    val oldElem = template.first;
    val id: String = (oldElem \ "@id").first text;
    val x = templateStore.put(id, oldElem)
    Elem(oldElem prefix, oldElem label, oldElem attributes, oldElem scope, Text("Placeholder"))
  }

  def getTemplate(id:String):NodeSeq = {
    templateStore.get(id).get 
  }
}
