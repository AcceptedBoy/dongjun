package com.gdut.dongjun.util;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.gdut.dongjun.service.cxf.Hardware;

public class CxfUtil {
	
	public static final String REMOTE_ADDRESS = 
			"http://localhost:8090/ws-server-1.0/api/hello";

	private static JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
	
	static {
		factory.setAddress(REMOTE_ADDRESS);
	    factory.setServiceClass(Hardware.class);
	}
	
	public static Hardware getHardwareClient() {
		return (Hardware)factory.create();
	}
}
