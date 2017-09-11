import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import crms.tools.RequestResponsePattern.Exceptions.WaitingForResponseTimeoutException
import crms.tools.RequestResponsePattern.{Handled, RequestResponse, Unhandled}

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}


class Start extends Actor with RequestResponse {

  /**
    * Temporary testing messages
    */


    case class Req(r:Int = 0)
    case class Res(r:Int = 0)


  val system = ActorSystem("main")

  override def receive = {

    case Req(x: Int) => {
      println("Main: Przyszedł request od " + sender.path.name)

      class huj(snd:ActorRef) extends Runnable {
        override def run(): Unit = snd ! Res()
      }

      import context.dispatcher
      context.system.scheduler.scheduleOnce(2000.milliseconds, new huj(sender))

      //sender ! Messages.Res()


      //sender ! "CHUJ CIE JEBAŁ"
    }

  }

  override def unhandled(message: Any): Unit = {
    println("Main: received unhandled message " + message)
    context.stop(self)
  }


  import context.dispatcher

  val hndl: Handled[Res,String] = (ret: Res) => {
    println("Mój handler")
    "PIERDOL SIĘ PSIE dostałem info, że " + ret.r
  }

  /*
  (ret: Res) => {
    println("Mój handler")
    "PIERDOL SIĘ PSIE dostałem info, że " + ret.r
  }*/
  /*
  , (e:Throwable) => {
    println("Wyjebało się coś w moim handlerze")
    e
  },
  */
  /*(m:Any) => println("Przyszedł nieznany message: " + m)*/

  val fut = sendAsyncRequest[Res,String](Req(0), self)

  import crms.tools.RequestResponsePattern.Exceptions.WaitingForResponseTimeoutException

  fut.onComplete {
    case Success(result: String) => {
      println("JEST OK Result: " + result)
    }
    case Failure(e:Throwable) => {
      println("JEST CHUJNIA", e)
      e match {
        case e:WaitingForResponseTimeoutException => println("Na samym końcu zaliczyłem timeout")
        case _ => println("Jakiś inny nieznany exception")
      }
    }
  }


}
