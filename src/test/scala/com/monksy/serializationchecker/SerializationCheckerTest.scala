package com.monksy.serializationchecker

import com.monksy.serializationchecker.model.{FailedParse, SourceItem, SuccessfulParse}
import org.scalatest.{Matchers, WordSpec}

class SerializationCheckerTest extends WordSpec with Matchers {

  "SerializationChecker.tryParse" should {
    "show a mixture of failures and successes" in {
      val src = StubSourceItem.SAMPLE
      val srcTwo = StubSourceItem.SAMPLE.copy(n = "newName")
      val input = List(src, srcTwo)
      val fn = { i: SourceItem => i.name() == "newName" }

      val actual = SerializationChecker.tryParse(input)(fn)
      val expSecond = SuccessfulParse(srcTwo.name(), srcTwo.location())
      val expFirst= FailedParse(src.name(), src.location(), src.content(), None)
      val expected = Seq(Left(expFirst), Right(expSecond))

      actual should be(expected)
    }
    "all successes" in {
      val src = StubSourceItem.SAMPLE
      val input = List(StubSourceItem.SAMPLE, StubSourceItem.SAMPLE)
      val fn = { _: SourceItem => true }

      val actual = SerializationChecker.tryParse(input)(fn)
      val expParse = SuccessfulParse(src.name(), src.location())
      val expected = Seq(Right(expParse), Right(expParse))

      actual should be(expected)
    }
    "all failures" in {
      val src = StubSourceItem.SAMPLE
      val input = List(StubSourceItem.SAMPLE, StubSourceItem.SAMPLE)
      val fn = { _: SourceItem => false }

      val actual = SerializationChecker.tryParse(input)(fn)
      val expParse = FailedParse(src.name(), src.location(), src.content(), None)
      val expected = Seq(Left(expParse), Left(expParse))

      actual should be(expected)
    }
  }


  "SerializationChecker.parseOneItem" should {
    "work for a function that succeeds" in {
      val src = StubSourceItem.SAMPLE
      val fn = { _: SourceItem => true }

      val actual = SerializationChecker.parseOneItem(src)(fn)
      val expected = Right(SuccessfulParse(src.name(), src.location()))

      actual should be(expected)
    }

    "fail for a function that rejects the parse with an exception" in {
      val src = StubSourceItem.SAMPLE
      val e = new IllegalArgumentException("Exception thrown")
      val fn = { _: SourceItem => throw e }

      val actual = SerializationChecker.parseOneItem(src)(fn)
      val expected = Left(FailedParse(src.name(), src.location(), src.content(), Some(e)))

      actual should be(expected)
    }

    "fail for a function that rejects the parse with a false" in {
      val src = StubSourceItem.SAMPLE
      val e = new IllegalArgumentException("Exception thrown")
      val fn = { _: SourceItem => throw e }

      val actual = SerializationChecker.parseOneItem(src)(fn)
      val expected = Left(FailedParse(src.name(), src.location(), src.content(), Some(e)))

      actual should be(expected)
    }
  }

}


case class StubSourceItem(n: String, l: String, c: String) extends SourceItem {
  /**
    * The name of the source item. Used for quick identification.
    *
    * @return Quick name.
    */
  override def name(): String = n

  /**
    * How you can find out where the content came from.
    *
    * @return Typically this is a URL to where you can find this source item.
    */
  override def location(): String = l

  /**
    * The content of the source item. This is used for attempting the parse.
    *
    * @return Content of the source item.
    */
  override def content(): String = c
}

object StubSourceItem {
  val SAMPLE = new StubSourceItem("name", "url", "allcontent")
}