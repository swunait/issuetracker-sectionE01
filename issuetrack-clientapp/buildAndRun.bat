@echo off
call mvn clean package
call docker build -t dmit2015/issuetrack-clientapp .
call docker rm -f issuetrack-clientapp
call docker run -d -p 8080:8080 -p 4848:4848 --name issuetrack-clientapp dmit2015/issuetrack-clientapp