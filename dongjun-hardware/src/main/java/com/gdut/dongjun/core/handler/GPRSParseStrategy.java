package com.gdut.dongjun.core.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.GPRSCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.ElectronicModuleService;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.TemperatureModuleService;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;

import io.netty.channel.ChannelHandlerContext;

@Component
public class GPRSParseStrategy extends ParseStrategy {
	
	private static final char[] CODE_00 = new char[] { '0', '0' };
	private static final char[] CODE_01 = new char[] { '0', '1' };
	private static final char[] CODE_03 = new char[] { '0', '3' };
	private static final String ATTRIBUTE_GPRS_ADDRESS = "GPRS_ADDRESS";
	
	@Autowired
	private GPRSModuleService gprsService;
	@Autowired
	private TemperatureCtxStore temStore;
	@Autowired
	private ElectronicCtxStore elecStore;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;
	@Autowired
	private ElectronicModuleService elecModuleService;
	@Autowired
	private TemperatureModuleService temModuleService;
	
	public GPRSParseStrategy() {
		super("401", Logger.getLogger(GPRSParseStrategy.class));
	}

	@Override
	protected boolean check(ChannelHandlerContext ctx, char[] data) {
		return true;
	}

	@Override
	protected String getAddress(char[] data) {
		return null;
	}

	@Override
	protected String getDecimalAddress(char[] data) {
		return null;
	}

	@Override
	protected Object parseInternal(ChannelHandlerContext ctx, char[] data) {
		String gprsAddress = null;
		// 登录和心跳包
		if (CharUtils.startWith(data, CODE_00)
				&& (CharUtils.equals(data, 6, 8, CODE_01) || CharUtils.equals(data, 6, 8, CODE_03))) {
			char[] gprsNumber = CharUtils.subChars(data, 12, 8);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= 6; i += 2) {
				//示范 30 30 30 31，表示0001地址
				if (sb.length() == 0 && gprsNumber[i + 1] == '0') {
					continue;
				}
				sb.append(gprsNumber[i + 1]);
			}
			//去除开头的0
			gprsAddress = sb.toString();
			
			//判断GPRS是否已在网站上注册
			String gprsId = gprsService.isGPRSAvailable(gprsAddress);
			if (null != gprsId) {
				CtxStore.setCtxAttribute(ctx, ATTRIBUTE_GPRS_ADDRESS, gprsAddress);
				//更新TemperatureCtxStore的GPRSList
				GPRSCtxStore.addGPRS(gprsAddress);
				if (CharUtils.equals(data, 6, 8, CODE_01)) {
					//GPRS模块登录
					logger.info(gprsAddress + " GPRS模块登录成功");
				} else if (CharUtils.equals(data, 6, 8, CODE_03)) {
					logger.info(gprsAddress + " GPRS模块在线");
				}
				
				initChannelInfo(gprsId, ctx);
			} else {
				//如果网站上没注册gprs，否决此报文
				logger.info("未注册GPRS模块地址" + gprsAddress);
			}
		}
		return null;
	}
	
	/**
	 * 初始化各子模块的ChannelInfo，并塞进CtxStore。
	 * 初始化的ChannelInfo拥有的ctx是属于GPRSDataReceiver的，但没有address
	 * ChannelInfo的address和ctx还是要到各个子模块解析类里面自己设置
	 * @param gprsId
	 * @param ctx
	 */
	public void initChannelInfo(String gprsId, ChannelHandlerContext ctx) {
		//TODO 事先要添加GPRS到DataMonitorSubmodule
		String monitorId = submoduleService.selectMonitorIdByModuleId(gprsId);
		List<DataMonitorSubmodule> submodules = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
		for (DataMonitorSubmodule submodule : submodules) {
			switch (submodule.getModuleType()) {
			case HitchConst.MODULE_ELECTRICITY : {
				//ChannelInfo创建后，有可能通过HarewareService的changeSubmoduleAddress方法销毁，进而在这里再次初始化
				ChannelInfo preInfo = elecStore.get(submodule.getModuleId());
				//创建ChannelInfo
				if (null == preInfo) {
					ElectronicModule module = elecModuleService.selectByPrimaryKey(submodule.getModuleId());
					preInfo = new ChannelInfo(submodule.getModuleId(), submodule.getDataMonitorId(), module.getGroupId(),
							module.getDeviceNumber(), ctx);
					elecStore.add(preInfo);
				}
				break;
			}
			case HitchConst.MODULE_TEMPERATURE : {
				ChannelInfo preInfo = temStore.get(submodule.getModuleId());
				//创建ChannelInfo
				if (null == preInfo) {
					TemperatureModule module = temModuleService.selectByPrimaryKey(submodule.getModuleId());
					preInfo = new ChannelInfo(submodule.getModuleId(), submodule.getDataMonitorId(), module.getGroupId(),
							module.getDeviceNumber(), ctx);
					temStore.add(preInfo);
				}
				break;
			}
			case HitchConst.MODULE_GPRS : break;
			default : break;
			}
		}
	}

	@Override
	public Object clearCache(ChannelHandlerContext ctx) {
		return CtxStore.removeCtxAttribute(ctx, ATTRIBUTE_GPRS_ADDRESS);
	}
	
	public boolean isEnabledPassed(ChannelHandlerContext ctx) {
		String address = (String)CtxStore.getCtxAttribute(ctx, ATTRIBUTE_GPRS_ADDRESS);
		if ((null == address || "".equals(address))
				|| !GPRSCtxStore.isGPRSAlive(address)) {
			logger.info("该GPRS尚未注册");
			return false;
		} else {
			return true;
		}
	}

}
