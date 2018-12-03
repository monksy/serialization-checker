package com.monksy.serializationchecker.sourcecollector

import java.io.File

import com.monksy.serializationchecker.model.{FileSource, SourceItem}

object Sources {
  def fromFolder(folderPath: String): List[SourceItem] = {
    val folder = new File(folderPath)
    folder.listFiles().filter(_.isFile).map(
      new FileSource(_)
    ).toList
  }
}
