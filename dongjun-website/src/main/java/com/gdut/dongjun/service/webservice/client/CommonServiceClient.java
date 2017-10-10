package com.gdut.dongjun.service.webservice.client;


import com.gdut.dongjun.service.webservice.client.service.CommonService;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 有中央项目交互的类
 */
@Component
public class CommonServiceClient implements InitializingBean {

    private CommonService centerService;

    @Override
    public void afterPropertiesSet() throws Exception {
//        centerService = JAXRSClientFactory.create(
//                "http://localhost:8789/dongjun-service/ws/common",
//                CommonService.class,
//                Arrays.asList(JacksonJsonProvider.class,
//                        BinaryDataProvider.class));
    }

    public CommonService getService() {
        return centerService;
    }
}
