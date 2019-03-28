#!/bin/bash

# Ask maven to build the executable jar file from the source files
mvn clean install --file ../src/Server/pom.xml

# Copy the executable jar file in the current directory
cp ../src/Server/target/Server-1.0-SNAPSHOT-standalone.jar .

# Build the Docker image locally
docker build --tag java-calculator-server .
