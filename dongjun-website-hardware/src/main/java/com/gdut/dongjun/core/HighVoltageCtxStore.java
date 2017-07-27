package com.gdut.dongjun.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;

/**
 * TODO
 * 等没什么事情干的时候，将CtxStore做成抽象类，分开各个模块各自适应的CtxStore。
 * @author Gordan_Deng
 * @date 2017年2月21日
 */
@Component
public class HighVoltageCtxStore extends CtxStore {
	
	private static final List<HighVoltageStatus> hstalist = new CopyOnWriteArrayList<HighVoltageStatus>();
	private static List<String> availableAddrList = new CopyOnWriteArrayList();
	
	private Logger logger = Logger.getLogger(HighVoltageCtxStore.class);
	
	//延迟加载CtxStore的WebServiceCilent
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
	
	/**
	 * 
	 * @Title: getHighVoltageStatus
	 * @Description: TODO
	 * @param @return
	 * @return List<HighVoltageStatus>
	 * @throws
	 */
	public List<HighVoltageStatus> getHighVoltageStatus() {

		return hstalist;
	}
	
	/**
	 * 
	 * @Title: getStatusbyId
	 * @Description: TODO
	 * @param @param id
	 * @param @return
	 * @return HighVoltageStatus
	 * @throws
	 */
	public HighVoltageStatus getStatusbyId(String id) {

		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}
		if (ctxlist != null) {

			for (HighVoltageStatus gprs : hstalist) {

				if (gprs != null && gprs.getId() != null && gprs.getId().equals(id)) {
					return gprs;
				}

			}
		} else {
			logger.info("ctxlist is empty!");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - end");
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: addStatus
	 * @Description: TODO
	 * @param @param ctx
	 * @return void
	 * @throws
	 */
	public void addStatus(HighVoltageStatus ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(SwitchGPRS) - start");
		}

		hstalist.add(ctx);
		websiteServiceClient.getService().callbackCtxChange();// TODO trueChange();
		
		if (logger.isDebugEnabled()) {
			logger.debug("add(SwitchGPRS) - end");
		}
	}
	
	public boolean isAddrAvailable(String addr) {
		if (availableAddrList.contains(addr)) {
			return true;
		}
		return false;
	}
	
	public synchronized void setAvailableAddrList(List<String> addrs) {
		availableAddrList.clear();
		availableAddrList.addAll(addrs);
	}

	public void removeStatus(HighVoltageStatus s) {
		hstalist.remove(s);
		websiteServiceClient.getService().callbackDeviceChange(s.getId(), 1);
	}
	
	public void removeStatusById(String id) {
		for (HighVoltageStatus s : hstalist) {
			if (null != s.getId() && id.equals(s.getId())) {
				hstalist.remove(s);	
				websiteServiceClient.getService().callbackDeviceChange(s.getId(), 1);
				break;
			}
		}
	}
	
}
