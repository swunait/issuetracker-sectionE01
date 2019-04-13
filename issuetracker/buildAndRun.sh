#!/bin/sh
mvn clean package && docker build -t dmit2015/issuetracker .
docker rm -f issuetracker || true && docker run -d -p 8080:8080 -p 4848:4848 --name issuetracker dmit2015/issuetracker