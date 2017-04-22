package com.gdut.dongjun.service.webservice.client;

import com.gdut.dongjun.service.webservice.client.service.HardwareService;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 不管以后采取的服务框架是哪一个，只要在这个类加入框架的服务调用类，然后在该类作为封装对象，
 * 返回给该项目其他地方使用即可，通过{@code getService()}暴露服务；
 * <p>这样做的好处就是以后服务框架的替换改变不会影响项目的其他地方的服务调用，只需要在这个类做一些修改就好
 */
@Component
public class HardwareServiceClient implements InitializingBean {

    private HardwareService hardwareService;

    @Override
    public void afterPropertiesSet() throws Exception {
        hardwareService = JAXRSClientFactory.create(
                "http://localhost:8090/dongjun-hardware/ws/hardware",
                HardwareService.class,
                Arrays.asList(JacksonJsonProvider.class,
                        BinaryDataProvider.class));
    }

    public HardwareService getService() {
        return hardwareService;
    }
}
