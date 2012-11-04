#!/bin/bash


#clear down mongo
# db.titles.ensureIndex({"identifier.value":1, "identifier.type":1});
echo Clean up old db
mongo <<!!!
use localchatter
db.dropDatabase();
!!!

# db.orgs.ensureIndex({"ukfam": 1});
# db.tipps.ensureIndex({"lastmod": 1});
# db.pkgs.ensureIndex({"sub": 1});
# db.platforms.ensureIndex({"normname": 1});
# db.titles.ensureIndex({"identifier":1});
# db.titles.ensureIndex({"lastmod":1});
# db.tipps.ensureIndex({"titleid":1, "pkgid":1, "platformid":1});
# db.st.ensureIndex({"tipp_id":1, "org_id":1, "sub_id":1});

