#!/bin/bash

echo Clear down old files
cd /home/ibbo/deploy/localchatter/apache-tomcat-7.0.34/webapps
rm -Rf api api.war localchatter localchatter.war ../logs/*

echo build api
cd /home/ibbo/dev/VirtualFIS/api
grails prod war
echo deploy api
mv target/api-0.1.war /home/ibbo/deploy/localchatter/apache-tomcat-7.0.34/webapps/api.war

echo build app
cd /home/ibbo/dev/VirtualFIS/localchatter
grails prod war
echo deploy app
mv target/localchatter-0.1.war /home/ibbo/deploy/localchatter/apache-tomcat-7.0.34/webapps/localchatter.war


