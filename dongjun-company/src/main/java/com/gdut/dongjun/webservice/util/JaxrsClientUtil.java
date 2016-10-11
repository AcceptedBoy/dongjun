package com.gdut.dongjun.webservice.util;

import com.gdut.dongjun.webservice.Constant;
import com.gdut.dongjun.webservice.client.CommonServiceClient;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.apache.poi.ss.formula.functions.T;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.util.Arrays;

/**
 * Created by symon on 16-9-29.
 */
public class JaxrsClientUtil {

    public Object getClient(String host, Class clazz) {

        return JAXRSClientFactory.create(
                host,
                clazz,
                Arrays.asList(JacksonJsonProvider.class, BinaryDataProvider.class)
        );
    }
}
