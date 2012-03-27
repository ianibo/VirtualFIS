dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:devDb;MVCC=TRUE"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE"
        }
    }
    production {
       dataSource {
            // dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            // url = "jdbc:hsqldb:mem:devDB"            // url = "jdbc:hsqldb:mem:devDB"
            driverClassName = "com.mysql.jdbc.Driver"
            dbCreate =  "update" // "create-drop"           // "create"
            username = "k-int"
            password = "k-int"
            pooled = true
            url = "jdbc:mysql://localhost/VFISLive?autoReconnect=true&amp;characterEncoding=utf8"
            properties {
                validationQuery="select 1"
                testWhileIdle=true
                timeBetweenEvictionRunsMillis=60000
            }
        }
    }
}
