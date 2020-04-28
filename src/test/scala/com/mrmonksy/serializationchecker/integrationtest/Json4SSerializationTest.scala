package com.mrmonksy.serializationchecker.integrationtest

import java.io.File

import com.mrmonksy.serializationchecker.SerializationChecker
import com.mrmonksy.serializationchecker.model.{FileSource, SuccessfulParse}
import com.mrmonksy.serializationchecker.sourcecollector.Sources
import org.json4s.FieldSerializer._
import org.json4s._
import org.json4s.jackson.Serialization.read
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class Json4SSerializationTest extends AnyWordSpec with Matchers {
  "Test single transformations" should {
    "Test a simple object without the option" in {
      val classLoader = getClass.getClassLoader
      val file = classLoader.getResource("serialized_files/simple_person-base.json").getFile
      val fSource = new FileSource(new File(file))
      val expected = Right(SuccessfulParse("simple_person-base.json", new File(file).toURI.toURL.toString))

      val actual = SerializationChecker.parseOneItem(fSource) { src =>
        implicit val formats = DefaultFormats +
          FieldSerializer[SimplePerson_First](renameTo("registeredStatus", "registered_status"),
            renameFrom("registered_status", "registeredStatus"))
        read[SimplePerson_First](src.content()) != null
      }

      actual should be(expected)
    }

    "Fail the second message because the option wasn't there" in {
      val classLoader = getClass.getClassLoader
      val file = classLoader.getResource("serialized_files/simple_person_missing_registeredstats.json").getFile
      val fSource = new FileSource(new File(file))
      val expected = true

      val actual = SerializationChecker.parseOneItem(fSource) { src =>
        implicit val formats = DefaultFormats +
          FieldSerializer[SimplePerson_First](renameTo("registeredStatus", "registered_status"),
            renameFrom("registered_status", "registeredStatus"))
        read[SimplePerson_First](src.content()) != null
      }

      actual.isLeft should be(expected)
    }

    "Verify that a option change to register status will succeed" in {
      val classLoader = getClass.getClassLoader
      val file = classLoader.getResource("serialized_files/simple_person_missing_registeredstats.json").getFile
      val fSource = new FileSource(new File(file))
      val expected = Right(SuccessfulParse("simple_person_missing_registeredstats.json", new File(file).toURI.toURL.toString))

      val actual = SerializationChecker.parseOneItem(fSource) { src =>
        implicit val formats = DefaultFormats +
          FieldSerializer[SimplePerson_First](renameTo("registeredStatus", "registered_status"),
            renameFrom("registered_status", "registeredStatus"))
        read[SimplePerson_Second](src.content()) != null
      }

      actual should be(expected)
    }

    "Add friend connection object" in {
      val classLoader = getClass.getClassLoader
      val file = classLoader.getResource("serialized_files/simple_person_with_connection.json").getFile
      val fSource = new FileSource(new File(file))
      val expected = Right(SuccessfulParse("simple_person_with_connection.json", new File(file).toURI.toURL.toString))

      val actual = SerializationChecker.parseOneItem(fSource) { src =>
        implicit val formats = DefaultFormats +
          FieldSerializer[SimplePerson_First](
            renameTo("registeredStatus", "registered_status"),
            renameFrom("registered_status", "registeredStatus")
          ) + FieldSerializer[Friend](renameTo("howLong", "how_long"), renameFrom("how_long", "howLong"))
        read[SimplePerson](src.content()) != null
      }

      actual should be(expected)
    }
  }
  "Test that all of the files can be read" should {
    "from he serialized_files directory" in {
      val classLoader = getClass.getClassLoader
      val folder = classLoader.getResource("serialized_files").getFile
      val source = Sources.fromFolder(folder)
      val expected = List(
        Right(SuccessfulParse("simple_person-base.json", s"file:$folder/simple_person-base.json")),
        Right(SuccessfulParse("simple_person_missing_registeredstats.json", s"file:$folder/simple_person_missing_registeredstats.json")),
        Right(SuccessfulParse("simple_person_with_connection.json", s"file:$folder/simple_person_with_connection.json"))
      )

      val actual = SerializationChecker.tryParse(source) { src =>
        implicit val formats = DefaultFormats +
          FieldSerializer[SimplePerson_First](
            renameTo("registeredStatus", "registered_status"),
            renameFrom("registered_status", "registeredStatus")
          ) + FieldSerializer[Friend](renameTo("howLong", "how_long"), renameFrom("how_long", "howLong"))
        read[SimplePerson](src.content()) != null
      }

      actual should be(expected)
    }
  }
}


case class SimplePerson_First(name: String, age: Int, registeredStatus: Boolean)

case class SimplePerson_Second(name: String, age: Int, registeredStatus: Option[Boolean])

case class Friend(name: String, howLong: Long)

case class SimplePerson(name: String, age: Int, registeredStatus: Option[Boolean], friends: List[Friend])