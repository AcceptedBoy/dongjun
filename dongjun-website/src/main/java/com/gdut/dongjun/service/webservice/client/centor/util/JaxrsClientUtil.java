package com.gdut.dongjun.service.webservice.client.centor.util;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.util.Arrays;

public class JaxrsClientUtil {

    public Object getClient(String host, Class clazz) {

        return JAXRSClientFactory.create(
                host,
                clazz,
                Arrays.asList(JacksonJsonProvider.class, BinaryDataProvider.class)
        );
    }
}
