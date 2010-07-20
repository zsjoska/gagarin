import _root_.scala.xml.{NodeSeq, Text,Unparsed, Group, Node, Elem}
import _root_.ro.gagarin.view.TemplateStore

object TestTemplateStore {
  def testNode: NodeSeq = {
    <div class="test" id="test">
    <p>Test1</p>
    <p>Test2</p>
    </div>
  }
  
  def main(args : Array[String]) {
    val ns = testNode
    println("1:"+TemplateStore.storeReplace(testNode))
    println("2:"+TemplateStore.getTemplate("test"))
    exit(0)
  }
}
