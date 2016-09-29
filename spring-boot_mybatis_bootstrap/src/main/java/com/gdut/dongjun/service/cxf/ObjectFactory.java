package com.gdut.dongjun.service.cxf;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.gdut.dongjun.service.cxf.changeCtxOpen.ChangeCtxOpen;
import com.gdut.dongjun.service.cxf.changeCtxOpen.ChangeCtxOpenResponse;
import com.gdut.dongjun.service.cxf.generateCloseSwitchMessage.GenerateCloseSwitchMessage;
import com.gdut.dongjun.service.cxf.generateCloseSwitchMessage.GenerateCloseSwitchMessageResponse;
import com.gdut.dongjun.service.cxf.generateOpenSwitchMessage.GenerateOpenSwitchMessage;
import com.gdut.dongjun.service.cxf.generateOpenSwitchMessage.GenerateOpenSwitchMessageResponse;
import com.gdut.dongjun.service.cxf.getCtxInstance.GetCtxInstance;
import com.gdut.dongjun.service.cxf.getCtxInstance.GetCtxInstanceResponse;
import com.gdut.dongjun.service.cxf.getOnlineAddressById.GetOnlineAddressById;
import com.gdut.dongjun.service.cxf.getOnlineAddressById.GetOnlineAddressByIdResponse;
import com.gdut.dongjun.service.cxf.getStatusbyId.GetStatusbyId;
import com.gdut.dongjun.service.cxf.getStatusbyId.GetStatusbyIdResponse;
import com.gdut.dongjun.service.cxf.getSwitchGPRS.GetSwitchGPRS;
import com.gdut.dongjun.service.cxf.getSwitchGPRS.GetSwitchGPRSResponse;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the demo.spring.service package. 
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

    private final static QName _SayHiResponse_QNAME = new QName("http://cxf.service.dongjun.gdut.com/", "generateOpenSwitchMessageResponse");
    private final static QName _SayHi_QNAME = new QName("http://cxf.service.dongjun.gdut.com/", "generateOpenSwitchMessage");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: demo.spring.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SayHiResponse }
     * 
     */
    public GenerateOpenSwitchMessageResponse createGenerateOpenSwitchMessageResponse() {
        return new GenerateOpenSwitchMessageResponse();
    }

    /**
     * Create an instance of {@link SayHi }
     * 
     */
    public GenerateOpenSwitchMessage createGenerateOpenSwitchMessage() {
        return new GenerateOpenSwitchMessage();
    }
    
    public GetOnlineAddressByIdResponse createGetOnlineAddressByIdResponse() {
    	return new GetOnlineAddressByIdResponse();
    }
    
    public GetOnlineAddressById createGetOnlineAddressById() {
    	return new GetOnlineAddressById();
    }
    
    /**
     * Create an instance of {@link SayHiResponse }
     * 
     */
    public GenerateCloseSwitchMessageResponse createGenerateCloseSwitchMessageResponse() {
        return new GenerateCloseSwitchMessageResponse();
    }

    /**
     * Create an instance of {@link SayHi }
     * 
     */
    public GenerateCloseSwitchMessage createGenerateCloseSwitchMessage() {
        return new GenerateCloseSwitchMessage();
    }
    
    public GetCtxInstanceResponse createGetCtxInstanceResponse() {
        return new GetCtxInstanceResponse();
    }

    /**
     * Create an instance of {@link SayHi }
     * 
     */
    public GetCtxInstance createGetCtxInstance() {
        return new GetCtxInstance();
    }
    
 /*   *//**
     * Create an instance of {@link SayHiResponse }
     * 
     */
    public GetStatusbyIdResponse createGetStatusbyIdResponse() {
        return new GetStatusbyIdResponse();
    }
/*
    *//**
     * Create an instance of {@link SayHi }
     * 
     */
    public GetStatusbyId createGetStatusbyId() {
        return new GetStatusbyId();
    }
    
    /**
     * Create an instance of {@link SayHiResponse }
     * 
     */
    public GetSwitchGPRSResponse createGetSwitchGPRSResponse() {
        return new GetSwitchGPRSResponse();
    }

    /**
     * Create an instance of {@link SayHi }
     * 
     */
    public GetSwitchGPRS createGetSwitchGPRS() {
        return new GetSwitchGPRS();
    }
    
    /**
     * Create an instance of {@link SayHiResponse }
     * 
     */
    public ChangeCtxOpenResponse createChangeCtxOpenResponse() {
        return new ChangeCtxOpenResponse();
    }

    /**
     * Create an instance of {@link SayHi }
     * 
     */
    public ChangeCtxOpen createChangeCtxOpen() {
        return new ChangeCtxOpen();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHiResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "generateOpenSwitchMessageResponse")
    public JAXBElement<GenerateOpenSwitchMessageResponse> createSayHiResponse(GenerateOpenSwitchMessageResponse value) {
        return new JAXBElement<GenerateOpenSwitchMessageResponse>(_SayHiResponse_QNAME, GenerateOpenSwitchMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHi }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "generateOpenSwitchMessage")
    public JAXBElement<GenerateOpenSwitchMessage> createSayHi(GenerateOpenSwitchMessage value) {
        return new JAXBElement<GenerateOpenSwitchMessage>(_SayHi_QNAME, GenerateOpenSwitchMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHiResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "generateCloseSwitchMessageResponse")
    public JAXBElement<GenerateCloseSwitchMessageResponse> createGenerateCloseSwitchMessageResponse(GenerateCloseSwitchMessageResponse value) {
        return new JAXBElement<GenerateCloseSwitchMessageResponse>(_SayHiResponse_QNAME, GenerateCloseSwitchMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHi }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "generateCloseSwitchMessage")
    public JAXBElement<GenerateCloseSwitchMessage> createGenerateCloseSwitchMessage(GenerateCloseSwitchMessage value) {
        return new JAXBElement<GenerateCloseSwitchMessage>(_SayHi_QNAME, GenerateCloseSwitchMessage.class, null, value);
    }
    
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "getOnlineAddressByIdResponse")
    public JAXBElement<GetOnlineAddressByIdResponse> createGetOnlineAddressByIdResponseCloseSwitchMessageResponse(GetOnlineAddressByIdResponse value) {
        return new JAXBElement<GetOnlineAddressByIdResponse>(_SayHiResponse_QNAME, GetOnlineAddressByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHi }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "getOnlineAddressById")
    public JAXBElement<GetOnlineAddressById> createGetOnlineAddressById(GetOnlineAddressById value) {
        return new JAXBElement<GetOnlineAddressById>(_SayHi_QNAME, GetOnlineAddressById.class, null, value);
    }
    
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHiResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "getCtxInstanceResponse")
    public JAXBElement<GetCtxInstanceResponse> createGetCtxInstanceResponse(GetCtxInstanceResponse value) {
        return new JAXBElement<GetCtxInstanceResponse>(_SayHiResponse_QNAME, GetCtxInstanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHi }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "getCtxInstance")
    public JAXBElement<GetCtxInstance> createGetCtxInstance(GetCtxInstance value) {
        return new JAXBElement<GetCtxInstance>(_SayHi_QNAME, GetCtxInstance.class, null, value);
    }
    
/*    *//**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHiResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "getStatusbyIdResponse")
    public JAXBElement<GetStatusbyIdResponse> createGetStatusbyIdResponse(GetStatusbyIdResponse value) {
        return new JAXBElement<GetStatusbyIdResponse>(_SayHiResponse_QNAME, GetStatusbyIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHi }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "getStatusbyId")
    public JAXBElement<GetStatusbyId> createGetCtxInstance(GetStatusbyId value) {
        return new JAXBElement<GetStatusbyId>(_SayHi_QNAME, GetStatusbyId.class, null, value);
    }
    
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHiResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "getSwitchGPRSResponse")
    public JAXBElement<GetSwitchGPRSResponse> createGetSwitchGPRSResponse(GetSwitchGPRSResponse value) {
        return new JAXBElement<GetSwitchGPRSResponse>(_SayHiResponse_QNAME, GetSwitchGPRSResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHi }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "getSwitchGPRS")
    public JAXBElement<GetSwitchGPRS> createGetSwitchGPRS(GetSwitchGPRS value) {
        return new JAXBElement<GetSwitchGPRS>(_SayHi_QNAME, GetSwitchGPRS.class, null, value);
    }
    
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHiResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "changeCtxOpenResponse")
    public JAXBElement<ChangeCtxOpenResponse> createChangeCtxOpenResponse(ChangeCtxOpenResponse value) {
        return new JAXBElement<ChangeCtxOpenResponse>(_SayHiResponse_QNAME, ChangeCtxOpenResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHi }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.service.dongjun.gdut.com/", name = "changeCtxOpen")
    public JAXBElement<ChangeCtxOpen> createChangeCtxOpen(ChangeCtxOpen value) {
        return new JAXBElement<ChangeCtxOpen>(_SayHi_QNAME, ChangeCtxOpen.class, null, value);
    }
}
