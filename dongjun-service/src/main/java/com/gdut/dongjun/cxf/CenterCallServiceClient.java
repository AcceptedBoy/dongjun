package com.gdut.dongjun.cxf;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.po.Company;
import com.gdut.dongjun.service.CompanyService;

@Service
public class CenterCallServiceClient implements InitializingBean {

	@Autowired
	private CompanyService companyService;
	
	private static List<CenterServiceConnection> serviceList = new CopyOnWriteArrayList<>(); 
	 private static ExtendedService extendedService = new ExtendedService();
	
	 /**
	  * 初始化连接
	  */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<Company> list = companyService.selectByParameters(null);
		for (Company c : list) {
			if (null != c.getIpAddr()) {
				initConnection(c.getIpAddr());
			}
		}
	}
	
	/**
	 * 建立和子系统的连接
	 * @param ip
	 */
	public void initConnection(String ip) {
		CenterServiceConnection c = new CenterServiceConnection();
		c.setIpAddr(ip);
		c.setService(JAXRSClientFactory.create(
                        "http://" + ip + "/dongjun-website/ws/website",
                        CenterCallService.class,
                        Arrays.asList(JacksonJsonProvider.class,
                                BinaryDataProvider.class)));
		serviceList.add(c);
	}
	
	public ExtendedService getService() {
        return extendedService;
    }
	
	public static class ExtendedService {

		public void updateSwitchAddressAvailable(List<String> addrs, String ipAddr) {
			for (CenterServiceConnection service : serviceList) {
				if (service.getIpAddr().equals(ipAddr)) {
					service.getService().updateSwitchAddressAvailable(addrs);
					return ;
				}
			}
		}
    }
	
	private class CenterServiceConnection {
		private String ipAddr;
		private CenterCallService service;
		public String getIpAddr() {
			return ipAddr;
		}
		public void setIpAddr(String ipAddr) {
			this.ipAddr = ipAddr;
		}
		public CenterCallService getService() {
			return service;
		}
		public void setService(CenterCallService service) {
			this.service = service;
		}
	}

	
	
}
