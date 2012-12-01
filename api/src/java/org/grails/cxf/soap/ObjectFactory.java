
package org.grails.cxf.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.grails.cxf.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UploadRequest_QNAME = new QName("http://dcsf.gov.uk/ISPP/Webservice/encodedTypes", "uploadRequest");
    private final static QName _UploadResponse_QNAME = new QName("http://dcsf.gov.uk/ISPP/Webservice/encodedTypes", "uploadResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.grails.cxf.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UploadRequestT }
     * 
     */
    public UploadRequestT createUploadRequestT() {
        return new UploadRequestT();
    }

    /**
     * Create an instance of {@link UploadResponseT }
     * 
     */
    public UploadResponseT createUploadResponseT() {
        return new UploadResponseT();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadRequestT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcsf.gov.uk/ISPP/Webservice/encodedTypes", name = "uploadRequest")
    public JAXBElement<UploadRequestT> createUploadRequest(UploadRequestT value) {
        return new JAXBElement<UploadRequestT>(_UploadRequest_QNAME, UploadRequestT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadResponseT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcsf.gov.uk/ISPP/Webservice/encodedTypes", name = "uploadResponse")
    public JAXBElement<UploadResponseT> createUploadResponse(UploadResponseT value) {
        return new JAXBElement<UploadResponseT>(_UploadResponse_QNAME, UploadResponseT.class, null, value);
    }

}
