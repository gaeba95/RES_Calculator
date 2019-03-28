#!/bin/bash

# Ask maven to build the executable jar file from the source files
mvn clean install --file ../src/Client/pom.xml

# Copy the executable jar file in the current directory
cp ../src/Client/target/Client-1.0-SNAPSHOT-standalone.jar .

# Build the Docker image locally
docker build --tag java-calculator-client .
