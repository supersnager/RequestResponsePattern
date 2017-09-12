package crms.tools.RequestResponsePattern
import java.util.UUID

import akka.actor.{ActorContext, ActorRef, Props}

import scala.concurrent.{ExecutionContextExecutor, Future, Promise}
import scala.reflect.ClassTag

trait RequestResponse {

  /**
    * Asynchronously sends request to recipient
    * Returns Future of expected response type R
    * Returned future can be:
    * - successed with received response
    * - failured with WaitingForResponseTimeoutException
    * - completed by unexpectedResponse handler (this can be done in your own callback)
    *
    * @param request Message to send
    * @param recipient Message recipient
    * @param unexpectedResponse Unexpected response handler
    * @param timeout Waiting for response timeout in milliseconds. If response will not be received till timeout. Promise will fail with WaitingForResponseTimeoutException
    * @tparam R Expected response type
    */
  def sendAsyncRequest[R](request:Any,
                          recipient: ActorRef,
                          unexpectedResponse: UnexpectedResponse[R] = defaultUnexpectedResponse[R] _,
                          timeout:Int = 5000 )(implicit context: ActorContext,
                                               tag: ClassTag[R],
                                               ec: ExecutionContextExecutor): Future[R] = {

      val p = Promise[R]()

      val handlerName = "crms.tools.RequestResponsePattern.ResponseHandler_" + UUID.randomUUID()

      context.actorOf(Props(new ResponseHandler(request, recipient, timeout, unexpectedResponse, p)), handlerName)

      p.future

  }

}
