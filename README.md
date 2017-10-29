# RequestResponse
Request-response pattern implementation for Scala, based on Akka and Futures.

Library uses "actor per request" model. 
When you invoke sendAsyncRequest method, a new actor is spawned, and message is send to given recipient.  
Spawned actor is waiting for expected response.  
When expected response is received, future returned by method is resolved with this response.  
If no expected message arrives in given time, WaitingForResponseTimeoutException will be thrown.
 
## Features

* Automatically matching received message with expected response type
* Configurable timeout
* Configurable function for catching unexpected responses
* Easy to use, no boilerplate code
* Can be used from classes and companion objects
 
## Install
There is no tagged version yet.  
Add to your build.sbt:
```scala
  libraryDependencies += "crms.tools" %% "request-response-pattern" % "0.0.1-SNAPSHOT"
``` 
## Use

1. Mix Request response trait into your class or object
2. Invoke sendAsyncRequest method, giving expected response as a type argument

```scala
import akka.actor.Actor
import crms.tools.RequestResponsePattern.RequestResponse
import crms.tools.RequestResponsePattern.Exceptions.WaitingForResponseTimeoutException
import scala.util.{Failure, Success}

class MyClass extends Actor with RequestResponse {

  // Request response messages pair
  case class Req()
  case class Res()

  // Receive timeout in milliseconds
  // Default is 5s
  val timeout = 6000

  // sendAsyncRequest method uses futures, so need implicit execution context
  import context.dispatcher

  // Give expected response as a type parameter
  sendAsyncRequest[Res](Req, self, (res, promise) => {

    // Log unexpected response
    println("Received unexpected response")
    
    // Here you can complete promise, or still wait for expected response

  }, timeout).onComplete {

    case Success(r: Res) =>
      println("Received Res")

    case Failure(e:Throwable) =>
      e match {

        case t: WaitingForResponseTimeoutException =>
          println("Timeout")
          
        case _ => println("Other exception")

      }

  }
  
  override def receive:Receive = {

    case Req =>
      println("Req Received, sending Res back to sender")
      sender ! Res

  }

}

```

### Using in companion object as api for your actor
TODO

## Project which use RequestRespone
Please give me know if you like and use this library :)

