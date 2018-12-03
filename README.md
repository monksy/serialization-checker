# Serialization checker 

## About 

This project is intended to run and verify that the examples provided (often times a folder full of JSONs) can deserialize against a particular object.

### What this project does not do

This utility is intended to confirm that the object's format will match the examples provided. This is not intended to verify that the parsing parsed out all of the information correctly. It's better to test that out within your unit test on a case by case basis. 

## Problem statement

The root problem that led to this project's creation is that REST typically uses JSON, and that JSON is Schemaless. This makes it difficult to create data objects to interact with services. In the case of connecting to a third-party REST service, you typically have lots of examples. This project helps you, the developer, iterate through the creation of the data objects.

## Dependency Information

## Example 

This project does not contain any particular specifics of integration. (I.e. ReactiveMongo, JSON4s, etc)

An example of an integration with Json4S can be found under the Json4SSerialization Integration test:

      val source = Sources.fromFolder("serialized_files")
      
      val actual = SerializationChecker.tryParse(source) { src =>
        implicit val formats = DefaultFormats 
        read[SimplePerson](src.content()) != null
      }

The main functionality of this library comes from SerializationChecker.tryParse. It's goal is to run through all of the `SourceItem`s in the input coming in. It will return a list of Either success or failures.

## How to go about using this and where should you use it?

There are 2 main usages of this project. The first usage of this is to use it as a test in your tests suite. For this you should confirm that all of the output is purely successful parses.

The second usage of this library would be to construct a single application that would verify the data object's validation against a bunch of examples. (Or if you're confident on the data object definition, the validity of the examples)

## How to build 

Build with sbt:

    sbt clean coverage compile test coverageReport coverageOff clean compile package

## TODO

 * Parse, Write, Parse verification (checking for repeatability serialization)
 * SBT Plugin that does automatic verification
 * ReactiveMongo Module
 * Templated CLI Application
 * Scalatest extension
 * Profiling of serialization examples and verification that they will stay under a limit.
 * Provide the ability to test serialization
 