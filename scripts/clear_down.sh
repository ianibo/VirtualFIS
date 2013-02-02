#!/bin/bash


# A full cleardown so the database can be reloaded from scract (Not including ofsted crawl db)

# Remove the sourcerecs collection from the mongo db "localchatter"
echo Remove localchatter db
mongo <<!!!
use localchatter
db.dropDatabase();
use ofsted_crawl_db
db.config.drop();
!!!


# Don't clear down the os gaz cache, it's useful
# use osgazcache
# db.dropDatabase()
# clear down elasticsearch indexes
echo Delete localchatter db
curl -XDELETE 'http://localhost:9200/localchatter'


echo Create db
curl -X PUT "localhost:9200/localchatter" -d '{
  "settings" : {}
}'

echo Put mapping

# db.orgs.ensureIndex({"ukfam": 1});
# db.tipps.ensureIndex({"lastmod": 1});
# db.pkgs.ensureIndex({"sub": 1});
# db.platforms.ensureIndex({"normname": 1});
# db.titles.ensureIndex({"identifier":1});
# db.titles.ensureIndex({"lastmod":1});
# db.tipps.ensureIndex({"titleid":1, "pkgid":1, "platformid":1});
# db.st.ensureIndex({"tipp_id":1, "org_id":1, "sub_id":1});

curl -X PUT "localhost:9200/localchatter/resource/_mapping" -d '{
  "resource" : {
    "properties" : {
      "position" : {
        type : "geo_point"
      },
      "district_facet" : {
        type : "string",
        index : "not_analyzed"
      },
      "ofstedUrn" : {
        type : "string",
        index : "not_analyzed"
      } ,
      "ward_facet" : {
        type : "string",
        index : "not_analyzed"
      },
      "childcareType" : {
        type : "string",
        index : "not_analyzed"
      },
      "outcode" : {
        type : "string",
        index : "not_analyzed"
      },
      "postalArea" : {
        type : "string",
        index : "not_analyzed"
      },
      "infotypes" : {
        type : "string",
        index : "not_analyzed",
        index_name : "infotype"
      },
      "shortcode" : {
        type : "string",
        index : "not_analyzed"
      }
    }
  }
}'


