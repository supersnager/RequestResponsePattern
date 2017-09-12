package crms.tools.RequestResponsePattern.tests


import crms.tools.RequestResponsePattern.RequestResponse
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestKitBase}
import crms.tools.RequestResponsePattern.tests.SetSpec.{Api, SenderActor}
import crms.tools.RequestResponsePattern.tests.SetSpec.SenderActor.Messages.Run
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers, WordSpecLike}

import scala.concurrent.Future

class SetSpec() extends TestKit(ActorSystem("SetSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with RequestResponse {

    override def afterAll {
        TestKit.shutdownActorSystem(system)
    }

    "An sender actor" must {
        "reply with completed Future[Res]" in {

            val apiActor = system.actorOf(Props(new Api()))
            val senderActor = system.actorOf(Props(classOf[SenderActor],apiActor))

            senderActor ! SenderActor.Messages.Run()

            // TODO: Niby działa, ale trochę to jest dookoła, możeby zapytać na stackoverflow o to
            val z = expectMsgClass[Future[Api.Messages.Res]](classOf[Future[Api.Messages.Res]])
            assert(z.isCompleted == true)

        }

    }

    it must {
        "reply with "

    }

    "An Echo actor" must {

        "send back messages unchanged" in {
            val echo = system.actorOf(TestActors.echoActorProps)
            echo ! "hello world"
            expectMsg("hello world")
        }

    }
}

object SetSpec {

    class Api extends Actor {

        override def receive:Receive = {
            case m: Api.Messages.Req =>
                println("Kurwa")
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

    class SenderActor(apiActor:ActorRef) extends Actor with RequestResponse {

        override def receive:Receive = {

            case m: Run =>
                import context.dispatcher
                sender !  sendAsyncRequest[Api.Messages.Res](Api.Messages.Req(0),apiActor)

        }

    }

    object SenderActor {
        object Messages {
            case class Run()
        }
    }

}

/*
class SetSpec extends FlatSpec with TestKitBase with Actor
     with Matchers with BeforeAndAfterAll with RequestResponse {

    override implicit val system = ActorSystem("SetSpec")

    override def afterAll {
        TestKit.shutdownActorSystem(system)
    }

    override def receive:Receive = {
      case _ =>
    }

    implicit val ec = system.dispatcher
    implicit val ctx = context

    val apiActor = system.actorOf(Props(new Api()))

   "An sendAsyncRequest" should "return Future[Res]" in {
        sendAsyncRequest(Api.Messages.Req(1),apiActor) shouldBe a [Future[_]]
    }

  "An empty Set" should "have size 0" in {
    assert(Set.empty.size == 0)
  }

  it should "produce NoSuchElementException when head is invoked" in {
    assertThrows[NoSuchElementException] {
      Set.empty.head
    }
  }

}

*/