package com.gdut.dongjun.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.TemperatureModuleService;

import io.netty.channel.ChannelHandlerContext;

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
	private static TemperatureModuleService moduleService;
	private static GPRSModuleService gprsService;
	@Autowired
	public void setDeviceService(TemperatureModuleService moduleService) {
		TemperatureCtxStore.moduleService = moduleService;
	}
	@Autowired
	private void setGPRSService(GPRSModuleService gprsService) {
		TemperatureCtxStore.gprsService = gprsService;
	}

	/*
	 * TODO 线程安全？
	 */
	private static final HashMap<String, Double> upperBound = new HashMap<String, Double>();
	
	private static final HashMap<String, Double> lowerBound = new HashMap<String, Double>();
	//ChannelHandlerContext和GPRSModule的deviceNumber的键值对
	private static final HashMap<ChannelHandlerContext, String> GPRSMap = new HashMap<ChannelHandlerContext, String>();
	
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
	
	public static void addGPRS(ChannelHandlerContext ctx, String module) {
		
		if (null != GPRSMap.get(ctx)) {
			if (GPRSMap.get(ctx) == module || GPRSMap.get(ctx).equals(module)) {
				return ;
			}
		}

		//清除ctx
		GPRSMap.remove(ctx);
		//清除gprs id
		for (Entry<ChannelHandlerContext, String> entry : GPRSMap.entrySet()) {
			if (entry.getValue() == module || entry.getValue().equals(module)) {
				GPRSMap.remove(entry.getKey());
				break;
			}
		}
		GPRSMap.put(ctx, module);
	}
	
	public static void removeGPRS(ChannelHandlerContext ctx) {
		
		GPRSMap.remove(ctx);
	}
	
	/**
	 * 判断GPRS是否在线
	 * @param gprsId
	 * @return
	 */
	public static List<Integer> isGPRSAlive(List<String> deviceNumbers) {
		List<Integer> results = new ArrayList<Integer>();
		for (String number : deviceNumbers) {
			if (GPRSMap.containsValue(number)) {
				results.add(1);
			} else {
				results.add(0);
			}
		}
		return results;
	}
	
	/**
	 * 根据ctx获取gprsId
	 * @param ctx
	 * @return
	 */
	public static String getGPRSByCtx(ChannelHandlerContext ctx) {
		
		return GPRSMap.get(ctx);
	}
}

