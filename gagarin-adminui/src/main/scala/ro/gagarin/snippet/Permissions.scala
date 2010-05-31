package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.ro.gagarin.model.{wsSession, SessionInfo}
import _root_.ro.gagarin.model.userService

class Permissions {
      def listCategories(in: NodeSeq): NodeSeq  = {
        val categories = userService.getControlEntityCategories
        <ul>
        {
          categories.flatMap( u =>
            <li>{link("permissionPage", () => {println(u.name)}, <span>{u.name}</span>)}</li>
        )}
        </ul>
      }
}

