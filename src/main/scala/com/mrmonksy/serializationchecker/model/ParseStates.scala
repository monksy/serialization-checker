package com.mrmonksy.serializationchecker.model

/**
  * This is returned if the parsing was a failed attempt.
  * @param name The name of the data item.
  * @param sourceLocation Where the data item can be found. If the source was a file, this would be a file path.
  * @param content The content of the item that failed.
  * @param exception The full error message that failed.
  */
case class FailedParse(name: String, sourceLocation: String, content: String, exception: Option[Throwable])


/**
  * This is returned if the parsing was a success.
  * @param name The name of the data item.
  * @param sourceLocation Where the data item can be found. If the source was a file, this would be a file path.
  */
case class SuccessfulParse(name:String, sourceLocation: String)
