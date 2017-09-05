package crms.tools
import akka.actor.{Actor, ActorRef, PoisonPill}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContextExecutor, Promise}
import scala.reflect.ClassTag

/**
  * Created by antonio on 20.08.17.
  */
package object RequestResponsePattern {

  /**
    * Type for handled code
    * @param S Expected response type
    * @param R Result type
    */
  type Handled[S,R] = S => R

  /**
    * Type for unhandled code
    */
  type Unhandled = Throwable => Throwable

  /**
    * Type for unknown message code
    */
  type UnknownMessage = Any => Unit

  object Exceptions {

    case class WaitingForResponseTimeoutException(message:String) extends Exception(message)

  }

  /********************* Internal API *********************/

  /**
    * Musimy miec tutaj ClassTag, żeby obejśc problem z TypeErasure
    * @param request Request to send to remote Actor
    * @param receiver Message receiver
    * @param timeout Response timeout
    * @param unknownFnc Function to run, when receive unknown message
    * @param promise Promise to resolve on result
    */
  private[RequestResponsePattern] sealed class ResponseHandler[S](request: Any,
                                                                  receiver: ActorRef,
                                                                  timeout: Int,
                                                                  unknownFnc: UnknownMessage,
                                                                  promise:Promise[S] )(implicit tag: ClassTag[S], ec: ExecutionContextExecutor) extends Actor  {

    override def receive:Receive = waitForResponse()

    receiver ! request

    private val timeoutScheduler = context.system.scheduler.scheduleOnce(timeout.milliseconds, self, ResponseHandler.Messages.TimeoutDown)


    private def waitForResponse()(implicit tag: ClassTag[S]): Receive = {

      case response:S =>
        println("RHandler otrzymałem oczekiwaną wiadomość, resolwuję sukces promisę")
        timeoutScheduler.cancel()
        promise.success(response)
        self ! PoisonPill

      case ResponseHandler.Messages.TimeoutDown =>

        promise.failure(Exceptions.WaitingForResponseTimeoutException("Waiting for response timeout exceed"))
        self ! PoisonPill


    }

    override def unhandled(message: Any): Unit = unknownFnc(message)

  }

  private[RequestResponsePattern] object ResponseHandler {

    object Messages {
      case class TimeoutDown()
    }

  }

}
