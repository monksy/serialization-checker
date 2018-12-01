package com.monksy.serializationchecker

import com.monksy.serializationchecker.model.{FailedParse, SourceItem, SuccessfulParse}

import scala.util.{Failure, Success, Try}

object SerializationChecker {
  /**
    * This is the main method here. This is intended to take in a source of all of your data and then process each of them with the function provided @f.
    * @param input All of the input items that you would want to use.
    * @param f The function that runs each of the parsing items that you're looking to use.
    * @return Returns a collection of states of a successful parse or a failed parse (with the information provided).
    */
  def tryParse(input: Seq[SourceItem])(f: SourceItem=>Boolean): Seq[Either[FailedParse, SuccessfulParse]] = {
    input.map(parseOneItem(_)(f))
  }

  /**
    * Parses a single item. This is not intended to be used, but if you wish to do all of the leg work yourself, this is what you would use.
    * @param src The source item that is being checked.
    * @param f The function that will be run. If an exception is thrown, it's implied that the parsing failed.
    * @return Returns either a failure or a successful parse.
    */
  def parseOneItem(src: SourceItem)(f: SourceItem=>Boolean): Either[FailedParse, SuccessfulParse]= {
    Try(f(src)) match {
      case Success(true) => Right(SuccessfulParse(src.name(), src.location()))
      case Success(false) => Left(FailedParse(src.name(), src.location(), src.content(), None))
      case Failure(e) => Left(FailedParse(src.name(), src.location(), src.content(), Some(e)))
    }
  }
}
