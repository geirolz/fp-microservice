#!/bin/bash

### Set the right JVM to use
jenv local 17.0

### build and publish docker image locally
sbt dockerPublishValidLocal

sbt_prj_name=$(sbt name | tail -n1 | cut -d' ' -f2)

### log
echo "Docker image published locally:"
docker images | grep "$sbt_prj_name"