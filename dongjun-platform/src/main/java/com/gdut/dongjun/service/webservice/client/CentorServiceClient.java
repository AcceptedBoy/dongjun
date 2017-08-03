package com.gdut.dongjun.service.webservice.client;


import java.util.Arrays;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.InitializingBean;

/**
 * 有中央项目交互的类
 */
//@Component
public class CentorServiceClient implements InitializingBean {

//    private CenterService centerService;
//
    @Override
    public void afterPropertiesSet() throws Exception {
//        centerService = JAXRSClientFactory.create(
//                "http://localhost:8789/dongjun_service/ws/common",
//                CenterService.class,
//                Arrays.asList(JacksonJsonProvider.class,
//                        BinaryDataProvider.class));
    }
//
//    public CenterService getService() {
//        return centerService;
//    }
}
