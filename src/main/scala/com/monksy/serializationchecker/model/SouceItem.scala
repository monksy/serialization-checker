package com.monksy.serializationchecker.model

import java.io.File

import scala.io.Source

/**
  * This defines the default interface used for handling a source file.
  */
trait SourceItem {
  /**
    * The name of the source item. Used for quick identification.
    * @return Quick name.
    */
  def name():String

  /**
    * How you can find out where the content came from.
    * @return Typically this is a URL to where you can find this source item.
    */
  def location(): String

  /**
    * The content of the source item. This is used for attempting the parse.
    * @return Content of the source item.
    */
  def content(): String
}

/**
  * This constructs a source item from a single file.
  * @param filePath The path to where you can find the file.
  */
class FileSource(file: File) extends SourceItem {
  private val src = Source.fromFile(file)
  private val rawContent = src.mkString

  override def name(): String = file.getName

  override def location(): String = file.toURI.toURL.toString

  override def content(): String = rawContent
}