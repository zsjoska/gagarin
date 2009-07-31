package ro.gagarin.model

import _root_.ro.gagarin.wsclient.WSClient

object WebServiceClient {
  
  // TODO: move this URL to config file
  val wsclient = WSClient.getWSClient("http://localhost:8080/gagarin-ws/ws/")
  
  def getAuthentication = wsclient.getAuthentication()
  def getUserService = wsclient.getUserService()
}

