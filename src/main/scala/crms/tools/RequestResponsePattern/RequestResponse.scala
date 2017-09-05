package crms.tools.RequestResponsePattern
import java.util.UUID

import akka.actor.{ActorContext, ActorRef, Props}

import scala.concurrent.{ExecutionContextExecutor, Future, Promise}
import scala.reflect.ClassTag
import scala.util.{Failure, Success}

trait RequestResponse {


  /**
    * Asynchronously sends request/
    * Returns Future of R
    *
    * @param request   Obiekt wiadomości do wysłania
    * @param recipient Aktor, do którego wiadomość ma zostać wysłana
    * @param handled   Handled callback is executed, when expected response arrives
    * @param unhandled Unhandled callback is executed in case of exception(Timeout etc)
    * @param timeout   Waiting for response timeout in milliseconds. If resposnse will not be received till timeout. Promise will fail with WaitingForResponseTimeoutException
    * @tparam S Typ wiadomości odbiorczej
    * @tparam R Typ danych zwracanych przez funkcje handlera - zostanie opakowany w promisę i ta promisa zostanie zwrócona
    */
  def sendAsyncRequest[S,R](request:Any, recipient: ActorRef,
                            handled:Handled[S,R],
                            unhandled: Unhandled,
                            timeout:Int = 1000,
                            unknownResponse: UnknownMessage)(implicit context: ActorContext, tag: ClassTag[S], ec: ExecutionContextExecutor): Future[R] = {


      val p = Promise[R]()

      val internalPromise = Promise[S]()
      val handlerName = "crms.tools.RequestResponsePattern.ResponseHandler_" + UUID.randomUUID()

      context.actorOf(Props(new ResponseHandler(request, recipient, timeout, unknownResponse, internalPromise)), handlerName)

      internalPromise.future.onComplete {

        case Success(response: S) =>

          println("sendAsyncReqest jest sukces")
          p.success(handled(response))



        case Failure(e: Throwable) =>

          // A tutaj  fajilurujemy zwracaną promisę
          // Dlaczego tak?
          // Ano dlatego, że to pozwala nam
          // 1. przemapować wyjątek
          // 2. Zalogować co się stało
          p.failure(unhandled(e))

          println("SendAsuncRequest - jest chujnia")

      }

      // Po wykonaniu kodu wyżej zaczyna się dziać Magia - aktor RHandler przesyła dalej message do prawdziwego recipienta
      // i Oczkekuje na odpowiedź od niego

      // Zwracamy future
      p.future

  }

}
