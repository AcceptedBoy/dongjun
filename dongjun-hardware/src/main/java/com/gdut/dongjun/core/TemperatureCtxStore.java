package com.gdut.dongjun.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.GPRSModule;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.TemperatureDeviceService;

/**
 * 存储ctx，存储温度传感器报警阈值，存储GPRS
 * @author Gordan_Deng
 * @date 2017年3月3日
 */
@Component
public class TemperatureCtxStore extends CtxStore {
	
	/**
	 * spring是根据set方法来注入的，对于static类变量，只能通过set方法来实现注入
	 * 否则只能通过ApplicationContext来弄
	 * @param deviceService
	 */
	private static TemperatureDeviceService deviceService;
	private static GPRSModuleService gprsService;

	@Autowired
	public void setDeviceService(TemperatureDeviceService deviceService) {
		TemperatureCtxStore.deviceService = deviceService;
	}
	@Autowired
	private void setGPRSService(GPRSModuleService gprsService) {
		TemperatureCtxStore.gprsService = gprsService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GPRSModule> list = gprsService.selectByParameters(null);
		GPRSList.addAll(list);
	}

	/*
	 * TODO 线程安全？
	 */
	private static final HashMap<String, Double> upperBound = new HashMap<String, Double>();
	
	private static final HashMap<String, Double> lowerBound = new HashMap<String, Double>();
	
	private static final List<GPRSModule> GPRSList = new ArrayList<GPRSModule>();
	
	private static Logger logger = Logger.getLogger(TemperatureCtxStore.class);
	
	public static Double getUpperBoundById(String id) {
		return upperBound.get(id);
	}
	
	public static Double getLowerBoundById(String id) {
		return lowerBound.get(id);
	}
	
	/**
	 * 是否超过设定的阈值。如果没有设置阈值，则返回false。
	 * @return
	 */
	public static boolean isAboveBound(String id, Double value) {
		
		if (!upperBound.containsKey(id) && !lowerBound.containsKey(id)) {
			synchronized (TemperatureCtxStore.class) {
				if (!upperBound.containsKey(id) && !lowerBound.containsKey(id)) {
					setBound(id);
				}
			}
		}
		Double upperTem = getUpperBoundById(id);
		Double lowerTem = getLowerBoundById(id);
		//没下限没上限
		if (null == lowerTem && null == upperTem) {
			return false;
		}
		//有下限没上限
		else if (null != lowerTem && null == upperTem) {
			if (lowerTem > value) {
				return true;
			}
		}
		//没下限有上限
		else if (null == lowerTem && null != upperTem) {
			if (value > upperTem) {
				return true;
			}
		}
		//有下限有上限
		else if (null != lowerTem && null != upperTem) {
			if (value < lowerTem || value > upperTem) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 设置阈值上下限
	 * @param id
	 */
	public static void setBound(String id) {
		if (null == deviceService) {
			logger.info("TemService is null!");
		}
		TemperatureDevice device = deviceService.selectByPrimaryKey(id);
		upperBound.put(id, device.getMaxHitchValue().doubleValue());
		lowerBound.put(id, device.getMinHitchValue().doubleValue());
	}
	
	public static synchronized void addGPRS(String id) {
		GPRSModule gprsModule = gprsService.selectByPrimaryKey(id);
		if (null != gprsModule) {
			GPRSList.add(gprsModule);
		}
	}
	
	public static synchronized void delGPRS(String id) {
		GPRSModule gprsModule = gprsService.selectByPrimaryKey(id);
		if (null != gprsModule) {
			GPRSList.remove(gprsModule);
		}
	}
	
	public static boolean isGPRSExist(String id) {
		GPRSModule gprsModule = gprsService.selectByPrimaryKey(id);
		return GPRSList.contains(gprsModule);
	}
	
	
}

