/**
 * Map of hostname to a comma separated list of properties that are applied to the peer listeners.
 * If the hostname does not match any of the keys it will use the default configuration.  To see the default configuration
 * look in the ehcache.xml file that is supplied by the plugin.  Look at the section describing CacheManagerPeerListener.
 * Possible values that can be added to the properties are the following:
 * hostName=fully_qualified_hostname_or_ip,
   port=40001,
   remoteObjectPort=40002,
   socketTimeoutMillis=120000"
   An example might look like the following:
    dist.ehcache.peerListener.properties=['london':"hostName=10.243.24.91", 'vancouver': "hostName=vancouverbe.interface, remoteObjectPort=50002"]

    The box that has a hostname of london will use the default port (40001), remoteObjectPort (40002), and socketTimeoutMillis (120000)
    while the box with a hostname of vancouverbe.interface will use a port (40001), remoteObjectPort of 50002 and the default socketTimeoutMillis
 */
environments {
	development {
        dist.ehcache.peerListener.properties=['canada':"hostName=10.243.24.91"]
    }
    test {
        dist.ehcache.peerListener.properties=['orange':"hostName=localhost"]
    }
}
