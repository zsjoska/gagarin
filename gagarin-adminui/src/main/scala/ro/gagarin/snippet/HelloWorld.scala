package ro.gagarin.snippet

import _root_.ro.gagarin.model.wsSessionId

class HelloWorld {
	
  def howdy = <span>Welcome to helloworld at {
    wsSessionId.is
  }</span>
}

