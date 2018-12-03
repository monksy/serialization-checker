# Serialization checker 

## About 

This project is intended to run and verify that the examples provided (often times a folder full of JSONs) can deserialize against a particular object.

### What this project does not do

This utility is intended to confirm that the object's format will match the examples provided. This is not intended to verify that the parsing parsed out all of the information correctly. It's better to test that out within your unit test on a case by case basis. 

## Problem statement

## Dependency Information

## Example 

## How to go about using this and where should you use it?

## How to build 

## Other projects

## TODO

 * Parse, Write, Parse verification (checking for repeatability serialization)
 * SBT Plugin that does automatic verification
 * ReactiveMongo Module
 * Templated CLI Application
 * Scalatest extension
 * Profiling of serialization examples and verification that they will stay under a limit.
 * Provide the ability to test serialization
 