@echo off
call mvn clean package
call docker build -t dmit2015/issuetracker .
call docker rm -f issuetracker
call docker run -d -p 8080:8080 -p 4848:4848 --name issuetracker dmit2015/issuetracker