package ro.gagarin.model

import _root_.net.liftweb.http.SessionVar
import _root_.ro.gagarin.PermissionEnum

case class SessionInfo(session: String, user: WsUser, permMap: Map[Long,Set[PermissionEnum]]) 

object wsSession extends SessionVar[SessionInfo](null)
