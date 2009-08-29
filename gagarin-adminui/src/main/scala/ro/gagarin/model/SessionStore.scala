package ro.gagarin.model

import _root_.net.liftweb.http.SessionVar

case class SessionInfo(session: String, user: WsUser, permSet: Set[String]) 

object wsSession extends SessionVar[SessionInfo](null)
