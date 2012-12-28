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


# db.orgs.ensureIndex({"ukfam": 1});
# db.tipps.ensureIndex({"lastmod": 1});
# db.pkgs.ensureIndex({"sub": 1});
# db.platforms.ensureIndex({"normname": 1});
# db.titles.ensureIndex({"identifier":1});
# db.titles.ensureIndex({"lastmod":1});
# db.tipps.ensureIndex({"titleid":1, "pkgid":1, "platformid":1});
# db.st.ensureIndex({"tipp_id":1, "org_id":1, "sub_id":1});

