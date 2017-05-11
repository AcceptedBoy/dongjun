package com.gdut.dongjun.core.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.util.SpringApplicationContextHolder;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 实现动态增删Channel的ChannelHandler，未测试
 * @author Gordan_Deng
 * @date 2017年5月11日
 */
@Component
public class ChannelHandlerManager {

	private static final Map<ChannelInfo, ChannelHandlerContext> channelInfoMap = 
			new HashMap<ChannelInfo, ChannelHandlerContext>();
	
	private static final Map<String, ChannelHandlerContext> ctxMap = 
			new HashMap<String, ChannelHandlerContext>();
	
	private static final Map<Class<?>, String> handlerNameMap = 
			new HashMap<Class<?>, String>();
	
	private static final String NAME_POST = "#0";

	private static final Logger logger = Logger.getLogger(ChannelHandlerManager.class);
	
	/**
	 * 得到类名 + #0的字符串
	 * 例子
	 * 输入：TemperatureDataReceiver.class
	 * 返回：TemperatureDataReceiver#0
	 * @param clazz
	 * @return
	 */
	private String generateClassName(Class<?> clazz) {
		String totalName = clazz.getName();
		String newName = totalName.substring(totalName.lastIndexOf('.') + 1);
		return newName + NAME_POST;
	}
	
	public <T extends ChannelHandler> void addHandlerAtLast(String monitorId, Class<T> clazz) {
		ChannelHandlerContext ctx = ctxMap.get(monitorId);
		if (null == ctx) {
			return ;
		}
		addHandlerAtLast(ctx, clazz);
	}
	
	public <T extends ChannelHandler> void addHandlerAtLast(ChannelHandlerContext ctx, Class<T> clazz) {
		Object newHandler = SpringApplicationContextHolder.getSpringBean(clazz);
		ctx.pipeline().addLast((ChannelHandler)newHandler);
		logger.info(ctx + "添加ChannelHandler-" + generateClassName(clazz));
	}
	
	public <T extends ChannelHandler> void removeHandler(String monitorId, Class<T> clazz) { 
		ChannelHandlerContext ctx = ctxMap.get(monitorId);
		if (null == ctx || null == clazz) {
			return ;
		}
		removeHandler(ctx, clazz);
	}
	
	public <T extends ChannelHandler> void removeHandler(ChannelHandlerContext ctx, Class<T> clazz) { 
		String handlerName = generateClassName(clazz);
		ctx.pipeline().remove(handlerName);
		logger.info(ctx + "去除ChannelHandler-" + handlerName);
	}
	
	public static void addCtx(String monitorId, ChannelHandlerContext ctx) {
		ctxMap.remove(monitorId);
		ctxMap.put(monitorId, ctx);
	}
	
	public static void removeCtx(String monitorId) {
		ctxMap.remove(monitorId);
	}
	
	public static void removeCtx(ChannelHandlerContext ctx) {
		if (null == ctx) {
			return ;
		}
		String del = null;
		for (Entry<String, ChannelHandlerContext> entry : ctxMap.entrySet()) {
			if (entry.getValue() == ctx) {
				del = entry.getKey();
				break;
			}
		}
		ctxMap.remove(del);
	}
	
}
