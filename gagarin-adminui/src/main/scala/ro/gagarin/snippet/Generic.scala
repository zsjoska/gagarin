package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.ro.gagarin.view.TemplateStore

class TemplateMgr {
  def storeAndReplace(in: NodeSeq): NodeSeq  = {
    TemplateStore.storeReplace(in)
  }
}

