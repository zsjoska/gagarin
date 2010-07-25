package ro.gagarin.view

import _root_.scala.xml.{NodeSeq, Text,Unparsed, Group, Node, Elem}
import _root_.scala.collection.mutable.HashMap

object TemplateStore {
  
  val templateStore = new HashMap[String, Node]()
  
  def storeReplace(template: NodeSeq): NodeSeq = 
    template.flatMap{ x => x match {
      case elem: Elem if ((elem \ "@id").size == 1 ) => {
        templateStore.put((elem \ "@id").text, elem)
        Elem(elem prefix, elem label, elem attributes, elem scope, Text("Placeholder"))
      }
      case text: Text => Seq.empty
      case any => { println(any.getClass+":"+any); Seq.empty }
    }}

  def getTemplate(id:String):NodeSeq = {
    templateStore.get(id).get 
  }
}
