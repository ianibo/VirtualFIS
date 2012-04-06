#!/usr/bin/groovy

@Grapes([
  @Grab(group='net.sf.json-lib', module='json-lib', version='2.3', classifier='jdk15'),
  @Grab(group='xom', module='xom', version='1.2.5')
])


def a=1
println "Test1" 
def xs=new net.sf.json.xml.XMLSerializer();
println "Test2" 
xs.setSkipNamespaces( true );  
xs.setTrimSpaces( true );  
xs.setRemoveNamespacePrefixFromElements( true );  
result = xs.read("<a><b>Hello</b><c>Goodbye</c></a>")
println "result:${result}" 
