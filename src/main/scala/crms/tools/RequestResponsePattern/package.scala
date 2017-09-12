package crms.tools
import akka.actor.{Actor, ActorRef, PoisonPill}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContextExecutor, Promise}
import scala.reflect.ClassTag

package object RequestResponsePattern {

  /**
    * Type for unexpected response code
    */
  type UnexpectedResponse[R] = (Any, Promise[R]) => Unit

  object Exceptions {

    case class WaitingForResponseTimeoutException(message:String) extends Exception(message)

  }

  /********************* Internal API *********************/

  /**
    * Default handler of unexpected response
    * Handler will be invoked when internal response handler actor receives message different from expected type
    * By default it does nothing, and response actor will be wait for expected message till timeout.
    * But programmer can define own handler for example to log received message, or complete promise before timeout
    * @param res
    * @param promise
    * @tparam R
    * @return
    */
  private[RequestResponsePattern] def defaultUnexpectedResponse[R](res:Any, promise:Promise[R]):Unit = ()

  private[RequestResponsePattern] object ResponseHandler {

    object Messages {
      case class TimeoutDown()
    }

  }

  /**
    * Internal response handler
    * @param request Request to send to remote Actor
    * @param receiver Message receiver
    * @param timeout Response timeout
    * @param unexpectedResponse Function to run, when receive unexpected message instead of expected response of type R
    * @param promise Promise to resolve on result
    */
  private[RequestResponsePattern] sealed class ResponseHandler[R](request: Any,
                                                                  receiver: ActorRef,
                                                                  timeout: Int,
                                                                  unexpectedResponse: UnexpectedResponse[R],
                                                                  promise:Promise[R] )(implicit tag: ClassTag[R], ec: ExecutionContextExecutor) extends Actor  {

    override def receive:Receive = waitForResponse()

    receiver ! request

    private val timeoutScheduler = context.system.scheduler.scheduleOnce(timeout.milliseconds, self, ResponseHandler.Messages.TimeoutDown)


    private def waitForResponse()(implicit tag: ClassTag[R]): Receive = {

      case response:R =>
        timeoutScheduler.cancel()
        promise.success(response)
        self ! PoisonPill

      case ResponseHandler.Messages.TimeoutDown =>

        promise.failure(Exceptions.WaitingForResponseTimeoutException("Waiting for response timeout exceed"))
        self ! PoisonPill

    }

    override def unhandled(message: Any): Unit = {

      unexpectedResponse(message, promise)

      // unexpectedResponse can complete promise, so if promise is completed
      // we must cleanup to avoid memory leak
      if ( promise.isCompleted ) {
        timeoutScheduler.cancel()
        self ! PoisonPill
      }

    }

  }

}
