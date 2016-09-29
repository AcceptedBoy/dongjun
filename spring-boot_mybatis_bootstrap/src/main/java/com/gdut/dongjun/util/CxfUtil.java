package com.gdut.dongjun.util;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.gdut.dongjun.service.cxf.Hardware;

public class CxfUtil {

	private static JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
	//private static Hardware client = null; 
	
	static {
		factory.setAddress("http://localhost:8090/ws-server-1.0/api/hello");
	    factory.setServiceClass(Hardware.class);
	}
	
	public static Hardware getHardwareClient() {
		return (Hardware)factory.create();
	}
	
    
	/*public synchronized static Hardware getClient() {
		
		if(client == null) {
			synchronized(client) {
				if(null == client) {
					client = (Hardware) factory.create();
				}
			}
		}
		return client;
	}*/
}
