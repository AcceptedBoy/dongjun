package com.gdut.dongjun.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.gdut.dongjun.service.ChangingSwitchExpireTask;
import com.gdut.dongjun.service.ScheduledTaskExecutor;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;

import io.netty.channel.ChannelHandlerContext;

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
 * 
 */
public abstract class CtxStore implements InitializingBean, ApplicationContextAware {
	
	private static Logger logger = Logger.getLogger(CtxStore.class);
	protected static WebsiteServiceClient websiteServiceClient;
	private ApplicationContext applicationContext;
	protected static List<SwitchGPRS> ctxlist = new CopyOnWriteArrayList<>();
	//	这个是存储用户正在执行合分闸操作的设备的map，用户执行合分闸操作就会往这map添加数据，在这期间如果
	//	系统接收到该设备的合分闸报文，就会产生一个“人工合/分闸”的事件
	private static Map<String, ChangingSwitchStatus> changingSwitchMap = new ConcurrentHashMap<>();
	
	protected CtxStore() {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		CtxStore.websiteServiceClient = (WebsiteServiceClient)
				applicationContext.getBean("websiteServiceClient");
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
	public static List<SwitchGPRS> getInstance() {

		return ctxlist;
	}
	
	public ChannelHandlerContext getCtxByAddress(String address) {
		
		for(SwitchGPRS gprs : ctxlist) {
			if(gprs.getAddress() != null && gprs.getAddress().equals(address)) {
				return gprs.getCtx();
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: get
	 * @Description: TODO
	 * @param @param ctx
	 * @param @return
	 * @return SwitchGPRS
	 * @throws
	 */
	public static SwitchGPRS get(ChannelHandlerContext ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}

		if (ctxlist != null) {

			for (SwitchGPRS gprs : ctxlist) {

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
	public static SwitchGPRS get(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}

		if (ctxlist != null) {

			for (SwitchGPRS gprs : ctxlist) {

				if (gprs != null && id.equals(gprs.getId())) {
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
	
	public static SwitchGPRS getByAddress(String address) {
		
		if(ctxlist != null) {
			for(SwitchGPRS gprs : ctxlist) {
				if(null != gprs 
						&& null != gprs.getAddress() 
						&& address.equals(gprs.getAddress())) {
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
	public static String getIdbyAddress(String address) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}
		if (ctxlist != null) {

			for (SwitchGPRS gprs : ctxlist) {

				if (gprs != null && address.equals(gprs.getAddress())) {
					return gprs.getId();
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
	public static void add(SwitchGPRS ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(SwitchGPRS) - start");
		}

		if(ctx != null) {

			//TODO 采用新的
			websiteServiceClient.getService().callbackCtxChange();
			ctxlist.add(ctx);
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
	public static void remove(ChannelHandlerContext ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove(SwitchGPRS) - start");
		}
		String id = null;
		if (ctxlist != null) {

			for (SwitchGPRS gprs : ctxlist) {

				if (gprs != null && ctx.equals(gprs.getCtx())) {
					id = gprs.getId();
					ctxlist.remove(gprs);	
					break;
				}
			}
			websiteServiceClient.getService().callbackCtxChange();// TODO trueChange();
			websiteServiceClient.getService().callbackCtxChangeForVoice(id, 3);
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
	public static void clear() {
		if (logger.isDebugEnabled()) {
			logger.debug("clear() - start");
		}

		ctxlist.clear();
		websiteServiceClient.getService().callbackCtxChange(); // TODO trueChange();
		if (logger.isDebugEnabled()) {
			logger.debug("clear() - end");
		}
	}

	/**
	 * 
	 * @Title: updateSwtichOpen
	 * @Description: TODO
	 * @param @param id
	 * @return void
	 * @throws
	 */
	public static void updateSwtichOpen(String id) {

		SwitchGPRS gprs = get(id);
		if (gprs != null && id.equals(gprs.getId())) {
			gprs.setOpen(true);
			//printCtxStore();
			websiteServiceClient.getService().callbackCtxChange(); // TODO trueChange();
		} else {
			logger.info("ctxlist is empty!");
		}
	}

	/**
	 * 
	 * @Title: isReady
	 * @Description: TODO
	 * @param @param id
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isReady(String id) {

		SwitchGPRS gprs = get(id);
		if (gprs != null && gprs.getId() != null && gprs.getAddress() != null
				&& gprs.getCtx() != null) {
			return true;
		} else {
//			if (gprs == null) {
//				
//				logger.info("gprs == null");
//			}else if (gprs.getId() == null) {
//				
//				logger.info("gprs.getId() == null");
//			}else if (gprs.getAddress() == null) {
//				
//				logger.info("gprs.getAddress() == null");
//			}else if (gprs.getCtx() == null) {
//				
//				logger.info("gprs.getCtx() == null");
//			} 
//			logger.info("not ready");
			return false;
		}
	}

	/**
	 * 
	 * @Title: excute
	 * @Description: TODO
	 * @param @param msg
	 * @return void
	 * @throws
	 */
	public static void excute(String id, String msg) {

		if (logger.isDebugEnabled()) {
			logger.debug("excute(String) - start");
		}

		SwitchGPRS gprs = get(id);
		if (gprs != null && gprs.getCtx() != null) {

			gprs.getCtx().writeAndFlush(msg);
			logger.info("excute " + msg);
		} else {

			logger.info("there is an error accour in excuting !");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("excute(String) - end");
		}
	}

	/**
	 * 在生产环境上作用甚微，日志又占空间，故去除
	 * @Title: printCtxStore
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	@Deprecated
	public static void printCtxStore() {
		if (logger.isDebugEnabled()) {
			logger.debug("printCtxStore() - start");
		}

		logger.info("ctx in the store now:");
		if (ctxlist != null) {
			for (int i = 0; i < ctxlist.size(); i++) {

				logger.info(ctxlist.get(i));
			}
		} else {
			logger.info("CtxStore is empty");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("printCtxStore() - end");
		}
	}

	public static void remove(String id) {
		
		if(id == null) {
			return;
		}
		
		List<SwitchGPRS> list = getInstance();
		for(int length = list.size() - 1, i = length; i >= 0; --i) {
			if(list.get(i).getId() != null && list.get(i).getId().equals(id)) {
				list.remove(i);
			}
		}
		websiteServiceClient.getService().callbackCtxChange();
		websiteServiceClient.getService().callbackCtxChangeForVoice(id, 3); // TODO trueChange();
	}

	@Deprecated
	public static boolean changeOpen(String switchId) {
		
		SwitchGPRS gprs = get(switchId);
		if(gprs != null) {
			gprs.setOpen(gprs.isOpen() == true ? false : true);
			websiteServiceClient.getService().callbackCtxChange(); // TODO trueChange();
			return true;
		} else {
			return false;
		}
	}
	
	public static void addChangingSwitch(String address, Integer type) {
		removeChangingSwitch(address);
		ChangingSwitchStatus s = new ChangingSwitchStatus(address, type);
		changingSwitchMap.put(address, s);
		ScheduledTaskExecutor.submit(s.getTask());
	}
	
	public static Integer selectChangingSwitchStatus(String address) {
		ChangingSwitchStatus s = changingSwitchMap.get(address);
		if (null != s) {
			return s.getType();
		}
		return null;
	}
	
	public static void removeChangingSwitch(String address) {
		ChangingSwitchStatus s = changingSwitchMap.get(address);
		if (null != s) {
			s.getTask().cancel();
		}
		changingSwitchMap.remove(address);
	}
}

class ChangingSwitchStatus {
	
	private Integer type;
	private ChangingSwitchExpireTask task;
	
	public ChangingSwitchStatus(String address, Integer type) {
		this.type = type;
		this.setTask(address);
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public ChangingSwitchExpireTask getTask() {
		return task;
	}
	public void setTask(String address) {
		this.task = new ChangingSwitchExpireTask(address);
	}
	
	
}
