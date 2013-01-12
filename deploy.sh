#!/bin/bash


cd /home/ibbo/deploy/localchatter/apache-tomcat-7.0.34/webapps
rm -Rf api api.war localchatter localchatter.war

cd /home/ibbo/dev/VirtualFIS/api
grails prod war
mv target/api-0.1.war /home/ibbo/deploy/localchatter/apache-tomcat-7.0.34/webapps/api.war

cd /home/ibbo/dev/VirtualFIS/localchatter
grails prod war
mv target/localchatter-0.1.war /home/ibbo/deploy/localchatter/apache-tomcat-7.0.34/webapps/localchatter.war


