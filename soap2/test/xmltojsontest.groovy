#!/usr/bin/groovy

@Grapes([
  @Grab('log4j:log4j:1.2.14'),
  @Grab('net.sf.json-lib:json-lib:2.4:jdk15'),
  @Grab('xom:xom:1.2.5'),
  @Grab('xalan:xalan:2.7.1')
  
])

import net.sf.json.xml.XMLSerializer
import org.apache.log4j.Level
import org.apache.log4j.Logger

def xml_text = 
"""<?xml version="1.0" encoding="utf-8"?>
<ProviderDescription xsi:schemaLocation="http://dcsf.gov.uk/XMLSchema/Childcare http://www.ispp-aggregator.org.uk/f1les/ISPP/docs/schemas/v5/ProviderTypes-v1-1e.xsd" xmlns="http://dcsf.gov.uk/XMLSchema/Childcare" xmlns:lg="http://www.esd.org.uk/standards" xmlns:gms="http://www.govtalk.gov.uk/CM/gms" xmlns:n2="http://www.govtalk.gov.uk/CM/gms-xs" xmlns:core="http://www.govtalk.gov.uk/core" xmlns:n3="http://www.govtalk.gov.uk/metadata/egms" xmlns:con="http://www.govtalk.gov.uk/people/ContactTypes" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <DC.Title>DJH Cr&#232;che</DC.Title>
  <DC.Identifier Href="Northlincs.gDJHov.uk">e9d0901d-225d-4347-9557-a59ea526ace5</DC.Identifier>
  <Description>
    <DC.Description Format="plain">Cheeky Monkey's Cr&#232;che - Creche</DC.Description>
  </Description>
  <DCTerms.Audience>Public</DCTerms.Audience>
  <DC.Language>EN</DC.Language>
  <DC.Subject Id="4148" ItemName="Creches" ListName="ISPP-001a-M">Creches</DC.Subject>
  <DC.Creator>ONE v4</DC.Creator>
  <DC.Publisher Href="Northlincs.gov.uk">North Lincolnshire Council</DC.Publisher>
  <DC.Date.Created>2009-12-11T14:12:40</DC.Date.Created>
  <DC.Date.Modified>2009-12-17T15:20:17.745806+00:00</DC.Date.Modified>
  <ProviderDetails>
    <ProviderName>North Lincolnshire Council</ProviderName>
    <ConsentVisibleAddress>true</ConsentVisibleAddress>
    <SettingDetails>
      <TelephoneNumber>
        <apd:TelNationalNumber>01724 280555</apd:TelNationalNumber>
      </TelephoneNumber>
      <PostalAddress>
        <apd:A_5LineAddress>
          <apd:Line>Carlton Street</apd:Line>
          <apd:Line>Scunthorpe</apd:Line>
          <apd:Line>South Humberside</apd:Line>
          <apd:PostCode>DN15 6TA</apd:PostCode>
        </apd:A_5LineAddress>
      </PostalAddress>
    </SettingDetails>
    <ChildcareType>Creche</ChildcareType>
    <ProvisionType>CCN</ProvisionType>
    <ChildcareAges />
    <Country>United Kingdom</Country>
    <ModificationDate>2009-12-17</ModificationDate>
    <RegistrationDetails RegistrationId="205685">
      <RegistrationDate>1994-04-18</RegistrationDate>
      <RegistrationConditions />
      <RegistrationTypes>
        <RegistrationType>VCR</RegistrationType>
      </RegistrationTypes>
      <RegistrationStatus>
        <RegistrationStatus>ACTV</RegistrationStatus>
        <RegistrationStatusStartDate>1994-04-18</RegistrationStatusStartDate>
      </RegistrationStatus>
      <LastInspection>
        <InspectionType>CR Inspection</InspectionType>
        <InspectionDate>2009-09-02</InspectionDate>
        <InspectionOverallJudgementDescription>Met</InspectionOverallJudgementDescription>
      </LastInspection>
      <WelfareNoticeHistoryList />
    </RegistrationDetails>
    <LinkedRegistration />
    <QualityAssurance>
      <QualityLevel>
        <QualityStatus Id="1" ItemName="Unknown" ListName="QualityAssurance-1.0">Unknown</QualityStatus>
      </QualityLevel>
    </QualityAssurance>
    <FromOfsted>true</FromOfsted>
    <OfstedURN>205685</OfstedURN>
  </ProviderDetails>
</ProviderDescription>"""
def xs=new net.sf.json.xml.XMLSerializer();
xs.setSkipNamespaces( true );  
xs.setTrimSpaces( true );  
xs.setRemoveNamespacePrefixFromElements( true );  
def result = xs.read(xml_text)

println(result)
