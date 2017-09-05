import akka.actor.ActorRef
import crms.tools.RequestResponsePattern.{Handled, Unhandled, UnknownMessage}

import scala.concurrent.{Future, Promise}

def sendAsyncRequest[S,Q,R](request:Q, recipient: String, handled:Handled[S,R],
                                                            unhandled: Unhandled,
                                                            timeout:Int = 1000,
                                                            unknownResponse: UnknownMessage): Future[R] = {

  val p = Promise[R]()

  p.future

}

case class req()

sendAsyncRequest( req(), "huj",
  (h:req) => {
    "String zwracam"
  }, (e:Throwable) => {
    e
  }, 1000,  m => {
    println(m)
    Unit
  })