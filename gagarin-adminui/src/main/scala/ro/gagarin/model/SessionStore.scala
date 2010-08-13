package ro.gagarin.model

import _root_.net.liftweb.http.SessionVar
import _root_.ro.gagarin.PermissionEnum

case class SessionInfo(session: String, user: WsUser, permMap: Map[Long,Set[PermissionEnum]]) 

object wsSession extends SessionVar[SessionInfo](null)

object PermissionHelper {
  def hasPermission(ce: CE, p: PermissionEnum) = wsSession.permMap.get(ce.id).getOrElse(Set.empty).contains(p)
}
