#!/bin/sh
mvn clean package && docker build -t dmit2015/issuetrack-clientapp .
docker rm -f issuetrack-clientapp || true && docker run -d -p 8080:8080 -p 4848:4848 --name issuetrack-clientapp dmit2015/issuetrack-clientapp