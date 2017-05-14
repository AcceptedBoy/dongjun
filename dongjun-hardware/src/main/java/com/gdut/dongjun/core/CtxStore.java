package com.gdut.dongjun.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @Title: ClientList.java
 * @Package com.gdut.dongjun.service.impl
 * @Description: 应用单例模式，用一个List管理设备与服务器之间的连接SwitchGPRS,包含Id,address,ctx等属性
 * @author Sherlock-lee
 * @date 2015年8月11日 下午4:30:06
 * @version V1.0
 * @see {@link SwitchGPRS}
 * 
 * @update: 2017.2.23 Gordan_Deng	把CtxStore存储设备状态的功能进行抽象
 * @update: 2017.5.10 Gordan_Deng 将SwitchGPRS换成ChannelInfo
 */
public abstract class CtxStore implements InitializingBean, ApplicationContextAware {
	
	private static Logger logger = Logger.getLogger(CtxStore.class);

	protected volatile static WebsiteServiceClient websiteServiceClient;

	private ApplicationContext applicationContext;
	
//	protected List<SwitchGPRS> ctxlist;
	
	protected List<ChannelInfo> ctxList;
	
	protected CtxStore() {
		ctxList = new CopyOnWriteArrayList<>();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (CtxStore.websiteServiceClient == null) {
			CtxStore.websiteServiceClient = (WebsiteServiceClient)
					applicationContext.getBean("websiteServiceClient");
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	//*****************************
	//  异步操作不需要这些标志位和方法了
	//*****************************
	@Deprecated
	private static volatile boolean changeInfo = false;

	@Deprecated
	public static void trueChange() {
		changeInfo = true;
	}

	@Deprecated
	public static void falseChange() {
		changeInfo = false;
	}

	@Deprecated
	public static boolean whetherChangeInfo() {
		return changeInfo;
	}

	/**
	 * 
	 * @Title: getInstance
	 * @Description: TODO
	 * @param @return
	 * @return List<SwitchGPRS>
	 * @throws
	 */
//	public List<SwitchGPRS> getInstance() {
//
//		return ctxlist;
//	}
//	
//	public ChannelHandlerContext getCtxByAddress(String address) {
//		
//		for(SwitchGPRS gprs : ctxlist) {
//			if(gprs.getAddress() != null && gprs.getAddress().equals(address)) {
//				return gprs.getCtx();
//			}
//		}
//		return null;
//	}

	/**
	 * 
	 * @Title: get
	 * @Description: TODO
	 * @param @param ctx
	 * @param @return
	 * @return SwitchGPRS
	 * @throws
	 */
//	public SwitchGPRS get(ChannelHandlerContext ctx) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("remove(SwitchGPRS) - start");
//		}
//
//		if (ctxlist != null) {
//
//			for (SwitchGPRS gprs : ctxlist) {
//
//				if (gprs != null && ctx.equals(gprs.getCtx())) {
//					return gprs;
//				}
//			}
//		} else {
//			logger.info("ctxlist is empty!");
//		}
//		if (logger.isDebugEnabled()) {
//			logger.debug("remove(SwitchGPRS) - end");
//		}
//		return null;
//	}

	/**
	 * 
	 * @Title: get
	 * @Description: TODO
	 * @param @param id
	 * @param @return
	 * @return SwitchGPRS
	 * @throws
	 */
//	public SwitchGPRS get(String id) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("remove(SwitchGPRS) - start");
//		}
//
//		if (ctxlist != null) {
//
//			for (SwitchGPRS gprs : ctxlist) {
//
//				if (gprs != null && id.equals(gprs.getId())) {
//					return gprs;
//				}
//
//			}
//		} else {
//			logger.info("ctxlist is empty!");
//		}
//		if (logger.isDebugEnabled()) {
//			logger.debug("remove(SwitchGPRS) - end");
//		}
//		return null;
//	}
	
//	public SwitchGPRS getByAddress(String address) {
//		
//		if(ctxlist != null) {
//			for(SwitchGPRS gprs : ctxlist) {
//				if(gprs != null && address.equals(gprs.getAddress())) {
//					return gprs;
//				}
//			}
//		}
//		return null;
//	}
	
	/**
	 * 
	 * @Title: get
	 * @Description: TODO
	 * @param @param address
	 * @param @return
	 * @return SwitchGPRS
	 * @throws
	 */
//	public String getIdbyAddress(String address) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("remove(SwitchGPRS) - start");
//		}
//		if (ctxlist != null) {
//
//			for (SwitchGPRS gprs : ctxlist) {
//
//				if (gprs != null && address.equals(gprs.getAddress())) {
//					return gprs.getId();
//				}
//
//			}
//		} else {
//			logger.info("ctxlist is empty!");
//		}
//		if (logger.isDebugEnabled()) {
//			logger.debug("remove(SwitchGPRS) - end");
//		}
//		return null;
//	}

	/**
	 * 
	 * @Title: add
	 * @Description: TODO
	 * @param @param ctx
	 * @return void
	 * @throws
	 */
//	public void add(SwitchGPRS ctx) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("add(SwitchGPRS) - start");
//		}
//
//		if(ctx != null) {
//
//			//TODO 采用新的
////			websiteServiceClient.getService().callbackCtxChange();
//			ctxlist.add(ctx);
//		}
//		//printCtxStore();
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("add(SwitchGPRS) - end");
//		}
//	}

	/**
	 * 
	 * @Title: remove
	 * @Description: TODO
	 * @param @param ctx
	 * @return void
	 * @throws
	 */
//	public void remove(ChannelHandlerContext ctx) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("remove(SwitchGPRS) - start");
//		}
//
//		if (ctxlist != null) {
//
//			for (SwitchGPRS gprs : ctxlist) {
//
//				if (gprs != null && ctx.equals(gprs.getCtx())) {
//
//					ctxlist.remove(gprs);	
//				}
//			}
////			websiteServiceClient.getService().callbackCtxChange();// TODO trueChange();
//		} else {
//			logger.info("ctxlist is empty, no gprs has bean remove!");
//		}
//		if (logger.isDebugEnabled()) {
//			logger.debug("remove(SwitchGPRS) - end");
//		}
//	}

	/**
	 * 
	 * @Title: clear
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
//	public void clear() {
//		if (logger.isDebugEnabled()) {
//			logger.debug("clear() - start");
//		}
//
//		ctxlist.clear();
////		websiteServiceClient.getService().callbackCtxChange(); // TODO trueChange();
//		if (logger.isDebugEnabled()) {
//			logger.debug("clear() - end");
//		}
//	}

	/**
	 * 
	 * @Title: updateSwtichOpen
	 * @Description: TODO
	 * @param @param id
	 * @return void
	 * @throws
	 */
//	public void updateSwtichOpen(String id) {
//
//		SwitchGPRS gprs = get(id);
//		if (gprs != null && id.equals(gprs.getId())) {
//			gprs.setOpen(true);
//			//printCtxStore();
////			websiteServiceClient.getService().callbackCtxChange(); // TODO trueChange();
//		} else {
//			logger.info("ctxlist is empty!");
//		}
//	}

	/**
	 * 
	 * @Title: isReady
	 * @Description: TODO
	 * @param @param id
	 * @param @return
	 * @return boolean
	 * @throws
	 */
//	public boolean isReady(String id) {
//
//		SwitchGPRS gprs = get(id);
//		if (gprs != null && gprs.getId() != null && gprs.getAddress() != null
//				&& gprs.getCtx() != null) {
//			return true;
//		} else {
//			
////			if (gprs == null) {
////				
////				logger.info("gprs == null");
////			}else if (gprs.getId() == null) {
////				
////				logger.info("gprs.getId() == null");
////			}else if (gprs.getAddress() == null) {
////				
////				logger.info("gprs.getAddress() == null");
////			}else if (gprs.getCtx() == null) {
////				
////				logger.info("gprs.getCtx() == null");
////			} 
////			logger.info("not ready");
//			return false;
//		}
//	}

	/**
	 * 
	 * @Title: excute
	 * @Description: TODO
	 * @param @param msg
	 * @return void
	 * @throws
	 */
//	public void excute(String id, String msg) {
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("excute(String) - start");
//		}
//
//		SwitchGPRS gprs = get(id);
//		if (gprs != null && gprs.getCtx() != null) {
//
//			gprs.getCtx().writeAndFlush(msg);
//			logger.info("excute " + msg);
//		} else {
//
//			logger.info("there is an error accour in excuting !");
//		}
//		if (logger.isDebugEnabled()) {
//			logger.debug("excute(String) - end");
//		}
//	}

	/**
	 * 在生产环境上作用甚微，日志又占空间，故去除
	 * @Title: printCtxStore
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
//	@Deprecated
//	public void printCtxStore() {
//		if (logger.isDebugEnabled()) {
//			logger.debug("printCtxStore() - start");
//		}
//
//		logger.info("ctx in the store now:");
//		if (ctxlist != null) {
//			for (int i = 0; i < ctxlist.size(); i++) {
//
//				logger.info(ctxlist.get(i));
//			}
//		} else {
//			logger.info("CtxStore is empty");
//		}
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("printCtxStore() - end");
//		}
//	}

//	public void remove(String id) {
//		
//		if(id == null) {
//			return;
//		}
//		
//		List<SwitchGPRS> list = getInstance();
//		for(int length = list.size() - 1, i = length; i >= 0; --i) {
//			if(list.get(i).getId() != null && list.get(i).getId().equals(id)) {
//				list.remove(i);
//			}
//		}
////		websiteServiceClient.getService().callbackCtxChange(); // TODO trueChange();
//	}

//	public boolean changeOpen(String switchId) {
//		
//		SwitchGPRS gprs = get(switchId);
//		if(gprs != null) {
//			gprs.setOpen(gprs.isOpen() == true ? false : true);
////			websiteServiceClient.getService().callbackCtxChange(); // TODO trueChange();
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	/**
	 * 设置ctx的Attribute
	 * @param ctx
	 * @param name
	 * @param obj
	 */
	public static void setCtxAttribute(ChannelHandlerContext ctx, String name, Object obj) {
		AttributeKey<Object> key = AttributeKey.valueOf(name);
		Attribute<Object> attr = ctx.attr(key);
		attr.set(obj);
	}
	
	/**
	 * 得到Ctx的Attribute
	 * @param ctx
	 * @param name
	 * @param obj
	 */
	public static Object getCtxAttribute(ChannelHandlerContext ctx, String name) {
		AttributeKey<Object> key = AttributeKey.valueOf(name);
		Attribute<Object> attr = ctx.attr(key);
		return attr.get();
	}
	
	/**
	 * 去除ctx的Attribute
	 * @param ctx
	 * @param name
	 */
	public static Object removeCtxAttribute(ChannelHandlerContext ctx, String name) {
		AttributeKey<Object> key = AttributeKey.valueOf(name);
		Attribute<Object> attr = ctx.attr(key);
		Object temp = attr.get();
		attr.remove();
		return temp;
	}
	
	public List<ChannelInfo> getInstance() {
		return ctxList;
	}
	
	public ChannelHandlerContext getCtxByAddress(String address) {
		
		for(ChannelInfo gprs : ctxList) {
			if(gprs.getAddress() != null && gprs.getAddress().equals(address)) {
				return gprs.getCtx();
			}
		}
		return null;
	}
	
	public ChannelInfo get(ChannelHandlerContext ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}

		if (ctxList != null) {

			for (ChannelInfo gprs : ctxList) {

				if (gprs != null && ctx.equals(gprs.getCtx())) {
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
	 * @Title: get
	 * @Description: TODO
	 * @param @param id
	 * @param @return
	 * @return SwitchGPRS
	 * @throws
	 */
	public ChannelInfo get(String moduleId) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}

		if (ctxList != null) {

			for (ChannelInfo gprs : ctxList) {
				
				if (null != gprs && moduleId.equals(gprs.getModuleId())) {
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
	
	public ChannelInfo getByAddress(String address) {
		
		if(ctxList != null) {
			for(ChannelInfo gprs : ctxList) {
				if(gprs != null && address.equals(gprs.getAddress())) {
					return gprs;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: get
	 * @Description: TODO
	 * @param @param address
	 * @param @return
	 * @return SwitchGPRS
	 * @throws
	 */
	public String getModuleIdbyAddress(String address) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}
		if (ctxList != null) {

			for (ChannelInfo gprs : ctxList) {

				if (gprs != null && address.equals(gprs.getAddress())) {
					return gprs.getModuleId();
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
	 * @Title: add
	 * @Description: TODO
	 * @param @param ctx
	 * @return void
	 * @throws
	 */
	public void add(ChannelInfo ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(SwitchGPRS) - start");
		}

		if(ctx != null) {

			//TODO 采用新的
//			websiteServiceClient.getService().callbackCtxChange();
			ctxList.add(ctx);
		}
		//printCtxStore();

		if (logger.isDebugEnabled()) {
			logger.debug("add(SwitchGPRS) - end");
		}
	}

	/**
	 * 
	 * @Title: remove
	 * @Description: TODO
	 * @param @param ctx
	 * @return void
	 * @throws
	 */
	public void remove(ChannelHandlerContext ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}

		if (ctxList != null) {

			for (ChannelInfo gprs : ctxList) {

				if (gprs != null && ctx.equals(gprs.getCtx())) {

					ctxList.remove(gprs);	
				}
			}
//			websiteServiceClient.getService().callbackCtxChange();// TODO trueChange();
		} else {
			logger.info("ctxlist is empty, no gprs has bean remove!");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - end");
		}
	}

	/**
	 * 
	 * @Title: clear
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void clear() {
		if (logger.isDebugEnabled()) {
			logger.debug("clear() - start");
		}

		ctxList.clear();
//		websiteServiceClient.getService().callbackCtxChange(); // TODO trueChange();
		if (logger.isDebugEnabled()) {
			logger.debug("clear() - end");
		}
	}
	
	public void remove(String moduleId) {
		
		if(null == moduleId) {
			return;
		}
		
		List<ChannelInfo> list = getInstance();
		for(int length = list.size() - 1, i = length; i >= 0; --i) {
			if(list.get(i).getModuleId() != null && list.get(i).getModuleId().equals(moduleId)) {
				list.remove(i);
			}
		}
	}
	
	public ChannelInfo get(Channel channel) {
		if (null == channel) {
			return null;
		}
		List<ChannelInfo> list = getInstance();
		for (ChannelInfo info : list) {
			if (info.getCtx().pipeline().channel() == channel) {
				return info;
			}
		}
		return null;
	}
	
	public ChannelInfo getByModuleId(String moduleId) {
		if (null == moduleId) {
			return null;
		}
		List<ChannelInfo> list = getInstance();
		for (ChannelInfo info : list) {
			if (info.getModuleId().equals(moduleId)) {
				return info;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: get
	 * @Description: TODO
	 * @param @param address
	 * @param @return
	 * @return SwitchGPRS
	 * @throws
	 */
	public ChannelInfo getChannelInfoByDecimalAddress(String decimalAddress) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}
		if (ctxList != null) {

			for (ChannelInfo gprs : ctxList) {

				if (gprs != null && decimalAddress.equals(gprs.getDecimalAddress())) {
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
	
}
