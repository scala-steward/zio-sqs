package zio.sqs.producer

import software.amazon.awssdk.services.sqs.model.BatchResultErrorEntry
import zio.test.Assertion._
import zio.test._

object ProducerErrorSpec
    extends DefaultRunnableSpec(
      suite("ProducerError")(
        test("it can be created from BatchResultErrorEntry") {
          val event = ProducerEvent("e1")
          val errEntry =
            BatchResultErrorEntry.builder().id("id1").code("code2").message("message3").senderFault(true).build()

          val e = ProducerError(errEntry, event)

          assert(e.code)(equalTo("code2")) &&
          assert(e.message)(equalTo(Some("message3"))) &&
          assert(e.senderFault)(isTrue) &&
          assert(e.event)(equalTo(event))
        },
        test("it can be created from BatchResultErrorEntry without message") {
          val event    = ProducerEvent("e2")
          val errEntry = BatchResultErrorEntry.builder().id("id1").code("code2").senderFault(true).build()

          val e = ProducerError(errEntry, event)

          assert(e.code)(equalTo("code2")) &&
          assert(e.message)(isNone) &&
          assert(e.senderFault)(isTrue) &&
          assert(e.event)(equalTo(event))
        }
      ),
      List(TestAspect.executionStrategy(ExecutionStrategy.Sequential))
    )
