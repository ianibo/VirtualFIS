mongoexport -d gazcache -c entries -o gazcache_dump.json
# mongoimport -d gazcache -c entries --file gazcache_dump.json
