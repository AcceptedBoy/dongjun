package com.gdut.dongjun.service.webservice.client;

import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.service.webservice.client.service.WebsiteService;
import com.gdut.dongjun.service.webservice.server.HardwareService;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class WebsiteServiceClient implements InitializingBean, ApplicationContextAware {

    private static List<WebsiteService> websiteList = new ArrayList<>();

    private static ExtendedService extendedService = new ExtendedService();

    private static HardwareService hardwareService;

    private ApplicationContext applicationContext;

    private static final Logger LOG = LoggerFactory.getLogger(WebsiteServiceClient.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static List<String> ipList = new ArrayList<String>() {{
    	//这里是复数服务器，以后可以通过xml文件来配置
    	add("localhost:9091");	
    }};

    @Override
    public void afterPropertiesSet() throws Exception {
        hardwareService = (HardwareService) applicationContext.getBean("hardwareServiceImpl");
        for(String ip : ipList) {
            websiteList.add(
                    JAXRSClientFactory.create(
                            "http://" + ip + "/dongjun-platform/ws/platform",
                            WebsiteService.class,
                            Arrays.asList(JacksonJsonProvider.class,
                                    BinaryDataProvider.class)));
        }
    }

    public ExtendedService getService() {
        return extendedService;
    }




    public static class ExtendedService {
    	
        public void callbackCtxChange() {
            for(WebsiteService websiteService : websiteList) {
                websiteService.callbackCtxChange(
                        hardwareService.getActiveSwitchStatus());
            }
        }

        public void callbackDeviceChange(String switchId, Integer type) {
            for(WebsiteService websiteService : websiteList) {
                websiteService.callbackDeviceChange(switchId, type);
            }
        }

        public void callbackHitchEvent(HitchEventVO event) {
            for(WebsiteService websiteService : websiteList) {
                websiteService.callbackHitchEvent(event);
            }
        }
    }
}
