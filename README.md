# FindMeSomePlaces

Simple webservice built on [Spark](http://sparkjava.com/) that fetches places using Google Places API.

## Installation

1. Add your Google Places API Key to src/main/resources/config.properties
2. Build and run with Maven
  * mvn compile && mvn exec:java

## Usage

Send location(s) to http://localhost:4567/findmesomeplaces?location=<latitude>,<longitude>
Supports multiple locations by adding more location arguments

E.g
http://localhost:4567/findmesomeplaces?location=19.423992,-99.141125&location=19.420977,-99.145224

