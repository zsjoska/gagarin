package ro.gagarin.snippet

import _root_.ro.gagarin.model.wsSession

class HelloWorld {
	
  def howdy = <span>Welcome to helloworld at {
    wsSession.is
  }</span>
}

