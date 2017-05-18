package com.gdut.dongjun.core;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.handler.thread.DeviceOfflineTask;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.TemperatureModuleService;

import io.netty.channel.ChannelHandlerContext;

/**
 * 存储ctx，存储温度传感器报警阈值
 * @author Gordan_Deng
 * @date 2017年3月3日
 */
@Component
public class TemperatureCtxStore extends CtxStore {
	
	//TODO
	//保存在线的温度设备的地址
	private static final HashMap<String, DeviceOfflineTask> taskMap = 
			new HashMap<String, DeviceOfflineTask>();
	
	/**
	 * spring是根据set方法来注入的，对于static类变量，只能通过set方法来实现注入
	 * 否则只能通过ApplicationContext来弄
	 * @param deviceService
	 */
	private static TemperatureModuleService moduleService;
	@Autowired
	public void setDeviceService(TemperatureModuleService moduleService) {
		TemperatureCtxStore.moduleService = moduleService;
	}

	/*
	 * TODO 线程安全？
	 */
	private static final HashMap<String, Double> upperBound = new HashMap<String, Double>();
	private static final HashMap<String, Double> lowerBound = new HashMap<String, Double>();
	
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
		TemperatureModule device = moduleService.selectByPrimaryKey(id);
		if (null == device) {
			return ;
		}
		upperBound.remove(id);
		lowerBound.remove(id);
		upperBound.put(id, device.getMaxHitchValue().doubleValue());
		lowerBound.put(id, device.getMinHitchValue().doubleValue());
		logger.info("设备" + device.getName() + "更改上下限，上限为" + device.getMaxHitchValue().doubleValue()
				+ "，下限为" + device.getMinHitchValue().doubleValue());
	}
	
	public static void setDeviceOfflineTask(String id) {
//		taskMap.put(id, new DeviceOnlineTask());
	}
	
	//设备维持在线
	public static void active(String id) {
		taskMap.get(id).active();
		
	}
	
}

