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
		// java.net.ConnectException
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
				}
				List<Company> list = companyService.selectByParameters(null);
				for (Company c : list) {
					if (null != c.getIpAddr()) {
						initConnection(c.getIpAddr());
					}
				}
				// 让已经启动的website系统上传InitialParam
				extendedService.initCall();
			}
		}.start();
	}

	/**
	 * 建立和子系统的连接
	 * 
	 * @param ip
	 */
	public void initConnection(String ip) {
		CenterServiceConnection c = new CenterServiceConnection();
		c.setIpAddr(ip);
		c.setWebsiteService(JAXRSClientFactory.create("http://" + ip + ":9091/dongjun-website/ws/website",
				CenterCallWebsiteService.class, Arrays.asList(JacksonJsonProvider.class, BinaryDataProvider.class)));
		c.setHardwareService(JAXRSClientFactory.create("http://" + ip + ":8090/dongjun-hardware/ws/hardware",
				CenterCallHardwareService.class, Arrays.asList(JacksonJsonProvider.class, BinaryDataProvider.class)));
		serviceList.add(c);
	}

	public ExtendedService getService() {
		return extendedService;
	}

	public static class ExtendedService {

		public void updateSwitchAddressAvailable(List<String> addrs, String ipAddr) {
			for (CenterServiceConnection service : serviceList) {
				if (service.getIpAddr().equals(ipAddr)) {
					service.getHardwareService().updateSwitchAddressAvailable(addrs);
					return;
				}
			}
		}

		public void initCall() {
			//	这里需要做一个验证服务器是否可用的判断
			for (CenterServiceConnection service : serviceList) {
				service.getWebsiteService().initCall();
			}
		}
	}

	/**
	 * 拥有跟硬件系统和网站系统的连接
	 * 
	 * @author Gordan_Deng
	 * @date 2017年7月19日
	 */
	private class CenterServiceConnection {
		private String ipAddr;
		private CenterCallHardwareService hardwareService;
		private CenterCallWebsiteService websiteService;
		private boolean available;
		
		public CenterServiceConnection() {
			this.available = false;
		}

		public String getIpAddr() {
			return ipAddr;
		}

		public void setIpAddr(String ipAddr) {
			this.ipAddr = ipAddr;
		}

		public CenterCallHardwareService getHardwareService() {
			return hardwareService;
		}

		public void setHardwareService(CenterCallHardwareService hardwareService) {
			this.hardwareService = hardwareService;
		}

		public CenterCallWebsiteService getWebsiteService() {
			return websiteService;
		}

		public void setWebsiteService(CenterCallWebsiteService websiteService) {
			this.websiteService = websiteService;
		}

		public boolean isAvailable() {
			return available;
		}

		public void setAvailable(boolean available) {
			this.available = available;
		}

	}

}
