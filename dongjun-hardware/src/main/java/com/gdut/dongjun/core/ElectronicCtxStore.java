package com.gdut.dongjun.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;

@Component
public class ElectronicCtxStore extends CtxStore {

	@Autowired
	public void setWebsiteServiceClient(WebsiteServiceClient webServiceClient) {
		if (null == GPRSCtxStore.websiteServiceClient) {
			synchronized (ElectronicCtxStore.class) {
				if (null == GPRSCtxStore.websiteServiceClient) {
					GPRSCtxStore.websiteServiceClient = webServiceClient;
				}
			}
		}
	}
	
	
	
	
}
