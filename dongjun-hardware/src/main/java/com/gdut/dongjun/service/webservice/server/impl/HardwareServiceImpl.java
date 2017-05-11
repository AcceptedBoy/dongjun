package com.gdut.dongjun.service.webservice.server.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.GPRSCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.core.handler.ChannelHandlerManager;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.core.handler.msg_decoder.ElectronicDataReceiver;
import com.gdut.dongjun.core.handler.msg_decoder.GPRSDataReceiver;
import com.gdut.dongjun.core.handler.msg_decoder.TemperatureDataReceiver;
import com.gdut.dongjun.service.webservice.server.HardwareService;
import com.gdut.dongjun.util.SpringApplicationContextHolder;

import io.netty.channel.ChannelHandlerContext;

@Component
public class HardwareServiceImpl implements HardwareService {

	/**
	 * 总召池，下面方法得到当前可用处理器个数，数量x2-1，就是线程池固有线程数
	 */
	// private static ExecutorService callerPool = Executors.newFixedThreadPool(
	// Runtime.getRuntime().availableProcessors() << 1 - 1);

	@Autowired
	private ChannelHandlerManager handlerManager;
	@Autowired
	private TemperatureCtxStore temCtxStore;
	@Autowired
	private ElectronicCtxStore elecCtxStore;
	
	private static final Logger logger = Logger.getLogger(HardwareServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdut.dongjun.service.rmi.HardwareService#getOnlineAddressById(java.
	 * lang.String)
	 */
//	@Override
//	public String getOnlineAddressById(String id) {
//		SwitchGPRS gprs = ctxStore.get(id);
//		if (gprs != null) {
//			return gprs.getAddress();
//		}
//		return null;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gdut.dongjun.service.rmi.HardwareService#getCtxInstance()
	 */
	@Override
	public List<ChannelInfo> getCtxInstance(Integer type) {
		switch (type) {
		case HitchConst.MODULE_ELECTRICITY : elecCtxStore.getInstance();
		case HitchConst.MODULE_TEMPERATURE : temCtxStore.getInstance();
		default : return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdut.dongjun.service.rmi.HardwareService#getSwitchGPRS(java.lang.
	 * String)
	 */
//	@Override
//	public SwitchGPRS getSwitchGPRS(String id) {
//		return ctxStore.get(id);
//	}

	@Override
	public void changeTemperatureDevice(String id) {
		TemperatureCtxStore.setBound(id);
	}

	@Override
	public List<Integer> getGPRSModuleStatus(List<String> deviceNumbers) {
		return GPRSCtxStore.isGPRSAlive(deviceNumbers);
	}

//	@Autowired
//	private TemperatureCtxStore temStore;
//	@Autowired
//	private ElectronicModuleMessageCreator messageCreator;
	// private static Thread elecThread = null;

	// @Override
	// public void testElec(final String id) {
	// logger.info("开启测试线程，开始总召");
	// if (null != elecThread) {
	// elecThread.interrupt();
	// }
	// elecThread = new Thread() {
	//
	// @Override
	// public void run() {
	// try {
	// while (true) {
	// ChannelHandlerContext ctx = getCtx(id);
	// if (null == ctx) {
	// Thread.sleep(1000 * 60 * 15);
	// }
	// List<String> list = messageCreator.generateTotalCall("010000000000");
	// List<String> list2 = messageCreator.generateTotalCall("000000000001");
	// for (String s : list) {
	// ctx.channel().writeAndFlush(s);
	// Thread.sleep(1000);
	// }
	// for (String s : list2) {
	// ctx.channel().writeAndFlush(s);
	// Thread.sleep(1000);
	// }
	// ctx = null;
	// Thread.sleep(1000 * 60 * 15);
	// }
	// } catch (Exception e) {logger.info(e.getMessage()); }
	// }
	//
	// };
	// elecThread.start();
	// }

//	private ChannelHandlerContext getCtx(String id) {
//		for (SwitchGPRS g : temStore.getInstance()) {
//			if (g.getId().equals(id)) {
//				return g.getCtx();
//			}
//		}
//		return null;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public void changeChannelHandler(String monitorId, Integer type, Integer operate) {
		Class clazz = null;
		switch (type) {
		case 2:
			clazz = ElectronicDataReceiver.class;
			break;
		case 3:
			clazz = TemperatureDataReceiver.class;
			break;
		case 4:
			clazz = GPRSDataReceiver.class;
			break;
		default:
			clazz = null;
			break;
		}
		if (0 == operate) {
			handlerManager.removeHandler(monitorId, clazz);
		} else if (1 == operate) {
			handlerManager.addHandlerAtLast(monitorId, clazz);
		} else {
			return;
		}
	}

	@Override
	public void changeSubmoduleAddress(String moduleId, Integer type) {
		CtxStore ctxStore = null;
		switch (type) {
		case 2: {
			ctxStore = (CtxStore) SpringApplicationContextHolder.getSpringBean(ElectronicDataReceiver.class);
			ChannelInfo info = ctxStore.getByModuleId(moduleId);
			ChannelHandlerContext ctx = info.getCtx();
			ctxStore.remove(moduleId);
			CtxStore.removeCtxAttribute(ctx, ElectronicDataReceiver.ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED);
			break;
		}
		case 3: {
			ctxStore = (CtxStore) SpringApplicationContextHolder.getSpringBean(TemperatureDataReceiver.class);
			ChannelInfo info = ctxStore.getByModuleId(moduleId);
			ChannelHandlerContext ctx = info.getCtx();
			ctxStore.remove(moduleId);
			CtxStore.removeCtxAttribute(ctx, TemperatureDataReceiver.ATTRIBUTE_FIRST_CALL);
			CtxStore.removeCtxAttribute(ctx, TemperatureDataReceiver.ATTRIBUTE_TEMPERATURE_MODULE_IS_REGISTED);
			break;
		}
		case 4:
			break;
		default:
			break;
		}
	}

}
