/usr/share/elasticsearch/bin/plugin -install elasticsearch/elasticsearch-mapper-attachments/1.6.0
/usr/share/elasticsearch/bin/plugin -url https://github.com/downloads/richardwilly98/elasticsearch-river-mongodb/elasticsearch-river-mongodb-1.6.1.zip -install river-mongodb
/usr/share/elasticsearch/bin/plugin -install mobz/elasticsearch-head


curl -XPUT "localhost:9200/_river/lcmongo/_meta" -d'
{
  "type": "mongodb",
    "mongodb": {
      "db": "localchatter", 
      "collection": "currentRecords"
    },
    "index": {
      "name": "localchatter", 
      "type": "resource"
    }
}'
