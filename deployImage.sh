#!/bin/bash

### Set the right JVM to use
jenv local 17.0

### build and publish docker image locally
sbt publishValidLocal

### log
docker images | grep fp-microservice