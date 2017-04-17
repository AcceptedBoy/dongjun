package com.gdut.dongjun.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.gdut.dongjun.core.handler.thread.GPRSExpiredTask;

public abstract class GPRSCtxStore extends CtxStore {

	//保存在线的GPRS的地址
	private static final List<String> GPRSList = new CopyOnWriteArrayList<String>();
	//GPRS地址和防止超时任务的键值对
	private static final HashMap<String, GPRSExpiredTask> taskMap = new HashMap<String, GPRSExpiredTask>();

	public static void addGPRS(String gprs) {
		if (GPRSList.contains(gprs)) {
			taskMap.get(gprs).GPRSConnected();
			return ;
		} else {
			GPRSList.add(gprs);
			taskMap.put(gprs, new GPRSExpiredTask(gprs));
		}
	}
	
	public static void removeGPRS(String gprs) {
		GPRSExpiredTask task = taskMap.remove(gprs);
		task.setAvailable(false);
		GPRSList.remove(gprs);
	}
	
//	public static void removeGPRS(ChannelHandlerContext ctx) {
//		GPRSMap.remove(ctx);
//	}
	
	/**
	 * 判断GPRS是否在线 TODO 移动到HarewareService
	 * @param gprsId
	 * @return
	 */
	public static List<Integer> isGPRSAlive(List<String> deviceNumbers) {
		List<Integer> results = new ArrayList<Integer>();
		for (String number : deviceNumbers) {
			if (GPRSList.contains(number)) {
				results.add(1);
			} else {
				results.add(0);
			}
		}
		return results;
	}
	
	/**
	 * 判断GPRS是否在线
	 * @param address
	 * @return
	 */
	public static boolean isGPRSAlive(String address) {
		return GPRSList.contains(address);
	}
	
	/**
	 * 根据ctx获取gprsId
	 * @param ctx
	 * @return
	 */
//	public static String getGPRSByCtx(ChannelHandlerContext ctx) {
//		return GPRSMap.get(ctx);
//	}
	
	/**
	 * 清除该地址的所有连接
	 * 一般用在该GPRS超时
	 * @param address
	 */
//	public static void removeValue(String address) {
//		if (null == address || "".equals(address)) {
//			return ;
//		}
//		for (Entry<ChannelHandlerContext, String> entry : GPRSMap.entrySet()) {
//			if (entry.getValue() == address || entry.getValue().equals(address)) {
//				GPRSMap.remove(entry);
//			}
//		}
//	}
}
