#!/bin/bash


# A full cleardown so the database can be reloaded from scract (Not including ofsted crawl db)

# Remove the sourcerecs collection from the mongo db "localchatter"
echo Remove localchatter db
mongo <<!!!
use localchatter
db.currentRecords.remove({});
use ofsted_crawl_db
db.config.drop();
use lcrecon
db.sourceRecords.drop();
!!!
