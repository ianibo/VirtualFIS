#!/usr/bin/groovy

@Grapes([
  @Grab(group='net.sf.json-lib', module='json-lib', version='2.3', classifier='jdk15'),
  @Grab(group='xom', module='xom', version='1.2.5')
])


def testdoc1 = "<a><b>Hello</b><c>Goodbye</c></a>"
def testdoc2 = """
<a>
  <b>
    <c>Some c value</c>
    <d>Complex <e>value</e></d>
    <f>Some</f>
  </b>
  <g>
    Some g
  </g>
  <h/>
  <i prop="iprop">ival</i>
</a>
"""
def testdoc3 = """
<ProviderDescription xmlns="http://dcsf.gov.uk/XMLSchema/Childcare" xmlns:exp="http://www.oobjects.com/ds4/exportdata" xmlns:fn="http://www.w3.org/2005/02/xpath-functions" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:esd="http://www.esd.org.uk/standards" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://dcsf.gov.uk/XMLSchema/Childcare http://www.ispp-aggregator.org.uk/f1les/ISPP/docs/schemas/v5/ProviderTypes-v1-1e.xsd">
<DC.Title>Jane Pearson</DC.Title>
<DC.Identifier>PYTd7cHkxjI</DC.Identifier>
<Description>
<DC.Description Format="plain">Childminder</DC.Description>
</Description>
<DC.Language>en</DC.Language>
<DC.Creator>Surrey County Council</DC.Creator>
<DC.Publisher>Surrey County Council</DC.Publisher>
<DC.Date.Created>2011-12-02T00:00:00Z</DC.Date.Created>
<DC.Date.Modified>2011-12-02T00:00:00Z</DC.Date.Modified>
<eGMS.Copyright>Surrey County Council</eGMS.Copyright>
<ProviderDetails>
<ProviderName>Jane Elizabeth Pearson</ProviderName>
<ConsentVisibleAddress>true</ConsentVisibleAddress>
<SettingDetails>
<TelephoneNumber TelUse="work" TelMobile="no" TelPreferred="no">
<apd:TelNationalNumber>01737 210947</apd:TelNationalNumber>
<apd:TelCountryCode>44</apd:TelCountryCode>
</TelephoneNumber>
<PostalAddress>
<apd:A_5LineAddress>
<apd:Line>REDHILL</apd:Line>
<apd:Line>Surrey</apd:Line>
<apd:PostCode>RH1 3LJ</apd:PostCode>
</apd:A_5LineAddress>
</PostalAddress>
<EmailAddress EmailUsage="work" EmailPreferred="no">
<apd:EmailAddress>jane81969@aol.co.uk</apd:EmailAddress>
</EmailAddress>
</SettingDetails>
<ContactDetails>
<TelephoneNumber TelUse="work" TelMobile="no" TelPreferred="no">
<apd:TelNationalNumber>01737 210947</apd:TelNationalNumber>
<apd:TelCountryCode>44</apd:TelCountryCode>
</TelephoneNumber>
<EmailAddress EmailUsage="work" EmailPreferred="no">
<apd:EmailAddress>jane81969@aol.co.uk</apd:EmailAddress>
</EmailAddress>
</ContactDetails>
<ChildcareType>childminder</ChildcareType>
<ProvisionType>CMR</ProvisionType>
<ChildcareAges>
<ChildNumberOverallLimit>6</ChildNumberOverallLimit>
</ChildcareAges>
<CostDetails ContactForCosts="false">
<Costs>Contact provider for details</Costs>
</CostDetails>
<Pickups HasProvision="true">
<Details>Contact provider for details</Details>
</Pickups>
<FutureVacancyDetails ContactForVacancies="true"/>
<SpecialProvisions>
<SpecialNeeds HasProvision="false"/>
<SpecialDiet HasProvision="false"/>
<CulturalProvision HasProvision="false"/>
<WheelchairAccess HasProvision="false"/>
</SpecialProvisions>
<Country>United Kingdom</Country>
<ModificationDate>2011-12-02</ModificationDate>
<RegistrationExempt>false</RegistrationExempt>
<QualityAssurance>
<QualityLevel>
<QualityStatus Id="1" ItemName="Unknown" ListName="QualityAssurance-1.0">Unknown!</QualityStatus>
<QualityStatusChangeDate>
                        2011-12-02T00:00:00Z
                    </QualityStatusChangeDate>
<Details>
                        
                    </Details>
</QualityLevel>
</QualityAssurance>
<FromOfsted>false</FromOfsted>
<OfstedURN>EY413009</OfstedURN>
</ProviderDetails>
</ProviderDescription>
"""


def a=1
println "Test1" 
def xs=new net.sf.json.xml.XMLSerializer();
println "Test2" 
xs.setSkipNamespaces( true );  
//xs.setSkipWhitespace( true );  
xs.setTrimSpaces( true );  
xs.setRemoveNamespacePrefixFromElements( true );  
result = xs.read(testdoc3)
println "result:${result}" 
// recurse(result)


def recurse(doc,depth=0) {
  if ( doc != null ) {
    println("Recuse [${depth}] class = ${doc.getClass().name}");
    if ( doc instanceof net.sf.json.JSONObject ) {
      doc.keys().each { k ->
        child=doc.get(k)
        println("processing ${k}");
        recurse(child,depth+1)
      }
    }
    else {
      println("Unhandled to string = ${doc}")
    }
  }
  else {
    println("null");
  }
}
