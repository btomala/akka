/**
 * Copyright (C) 2009-2011 Typesafe Inc. <http://www.typesafe.com>
 */
package akka.docs.routing

import akka.actor.{ Actor, Props, ActorSystem }
import com.typesafe.config.ConfigFactory
import akka.routing.FromConfig

case class Message(nbr: Int)

class ExampleActor extends Actor {
  def receive = {
    case Message(nbr) ⇒ println("Received %s in router %s".format(nbr, self.path.name))
  }
}

object RouterWithConfigExample extends App {
  val config = ConfigFactory.parseString("""
    //#config
    akka.actor.deployment {
      /router {
        router = round-robin
        nr-of-instances = 5
      }
    }
    //#config
      """)
  val system = ActorSystem("Example", config)
  //#configurableRouting
  val router = system.actorOf(Props[ExampleActor].withRouter(FromConfig()),
    "router")
  //#configurableRouting
  1 to 10 foreach { i ⇒ router ! Message(i) }
}