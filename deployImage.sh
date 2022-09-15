#!/bin/bash

### Set the right JVM to use
jenv local 17.0

### build and publish docker image locally
sbt publishValidLocal

sbt_prj_name=$(sbt name | tail -n1 | cut -d' ' -f2)

### log
docker images | grep "$sbt_prj_name"