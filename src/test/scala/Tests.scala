package crms.tools.RequestResponsePattern.tests


import crms.tools.RequestResponsePattern.RequestResponse
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestKitBase}
import crms.tools.RequestResponsePattern.tests.SetSpec.{Api}
import org.scalatest._
import Assertions._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class SetSpec() extends TestKit(ActorSystem("SetSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with RequestResponse {

    override def afterAll {
        TestKit.shutdownActorSystem(system)
    }

    "sendAsyncRequest" must {
        "reply with succeed Future[Res]" in {

            val apiActor = system.actorOf(Props(new Api()))

            implicit val executionContext = ExecutionContext.Implicits.global

            val retFuture = sendAsyncRequest[Api.Messages.Res](Api.Messages.Req(0),apiActor)

            //val z = expectMsgClass[Future[Api.Messages.Res]](classOf[Future[Api.Messages.Res]])
            retFuture.map( res => assert(res.isInstanceOf[Api.Messages.Res]))
            succeed
        }

    }

    it must {
        "reply with faliled Future[Exception]" in {

            val apiActor = system.actorOf(Props(new Api()))

            implicit val executionContext = ExecutionContext.Implicits.global

            val retFuture = sendAsyncRequest[Api.Messages.Res](Api.Messages.Req(0),apiActor, )

            //val z = expectMsgClass[Future[Api.Messages.Res]](classOf[Future[Api.Messages.Res]])
            retFuture.map( res => assert(res.isInstanceOf[Api.Messages.Res]))
            succeed


        }
    }


}

object SetSpec {

    class Api extends Actor {

        override def receive:Receive = {
            case m: Api.Messages.Req =>
                sender ! Api.Messages.Res(0)
            case _ => println("KURWA")
        }
    }

    object Api {

        object Messages {
            case class Req(v:Int)
            case class Res(v:Int)
        }

    }

}
