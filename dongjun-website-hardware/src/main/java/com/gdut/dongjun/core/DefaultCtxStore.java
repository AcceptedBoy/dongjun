package com.gdut.dongjun.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;

/**
 * TODO
 * @author Gordan_Deng
 * @date 2017年2月23日
 */
@Component
public class DefaultCtxStore extends CtxStore {

	private Logger logger = Logger.getLogger(DefaultCtxStore.class);
	
	@Autowired
	public void setWebsiteServiceClient(WebsiteServiceClient webServiceClient) {
		if (null == super.websiteServiceClient) {
			synchronized (HighVoltageCtxStore.class) {
				if (null == super.websiteServiceClient) {
					super.websiteServiceClient = webServiceClient;
				}
			}
		}
	}
	
}
