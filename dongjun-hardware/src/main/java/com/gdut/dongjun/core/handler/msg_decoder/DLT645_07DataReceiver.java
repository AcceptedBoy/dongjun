package com.gdut.dongjun.core.handler.msg_decoder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.GPRSCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.domain.po.ElectronicModuleHitchEvent;
import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.service.ElectronicModuleCurrentService;
import com.gdut.dongjun.service.ElectronicModuleHitchEventService;
import com.gdut.dongjun.service.ElectronicModulePowerService;
import com.gdut.dongjun.service.ElectronicModuleService;
import com.gdut.dongjun.service.ElectronicModuleVoltageService;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.ModuleHitchEventService;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.StringCommonUtil;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
@Service
public class DLT645_07DataReceiver extends ChannelInboundHandlerAdapter {

	// TODO 考虑下要不要将ElectronicModule直接塞到Attribute里面
	private static final int BYTE = 2;

	// GPRS相关
	private static final char[] GPRS_LOGIN = { '0', '0', '2', '5' };
	private static final char[] GPRS_HEART = { '0', '0', '0', 'a' };
	private static final String ATTRIBUTE_GPRS_ADDRESS = "GPRS_ADDRESS";

	// 取值字段
	private static final char[] VOLTAGE = { '0', '2', '0', '1' };
	private static final char[] CURRENT = { '0', '2', '0', '2' };
	private static final char[] POWER = { '0', '2', '0', '6' };
	private static final char[] EXCEPTION = { '0', '3' };
	private static final char[] A_PHASE = { '0', '1' };
	private static final char[] B_PHASE = { '0', '2' };
	private static final char[] C_PHASE = { '0', '3' };
	private static final char[] CODE_00 = { '0', '0' };
	private static final char[] DATA_BLOCK_UP = { 'F', 'F' };
	private static final char[] DATA_BLOCK_DOWN = { 'f', 'f' };
	private static final char[] CODE_01 = { '0', '1' };
	private static final char[] CODE_03 = { '0', '3' };
	private static final char[] HITCH_VOLTAGE = {}; // TODO
	private static final char[] EB_UP = new char[] { 'E', 'B', '9', '0' };
	private static final char[] EB_DOWN = new char[] { 'e', 'b', '9', '0' };
	private static final char[] CODE_68 = new char[] { '6', '8' };
	private static final char[] CODE_16 = new char[] { '1', '6' };

	// 报警字段
	private static final char[] SHI_YA = new char[] { '0', '3', '0', '1' };
	private static final char[] QIAN_YA = new char[] { '0', '3', '0', '2' };
	private static final char[] GUO_YA = new char[] { '0', '3', '0', '3' };
	private static final char[] DUAN_XIANG = new char[] { '0', '3', '0', '4' };
	private static final char[] QUAN_SHI_YA = new char[] { '0', '3', '0', '5' };
	private static final char[] SHI_LIU_UP = new char[] { '0', '3', '0', 'B' };
	private static final char[] SHI_LIU_DOWN = new char[] { '0', '3', '0', 'b' };
	private static final char[] GUO_LIU_UP = new char[] { '0', '3', '0', 'C' };
	private static final char[] GUO_LIU_DOWN = new char[] { '0', '3', '0', 'c' };
	private static final char[] DUAN_LIU_UP = new char[] { '0', '3', '0', 'D' };
	private static final char[] DUAN_LIU_DOWN = new char[] { '0', '3', '0', 'd' };
	public static final String ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED = "ELECTRONIC_MODULE_IS_REGISTED";

	@Autowired
	private ElectronicModuleService moduleService;
	@Autowired
	private ElectronicModuleCurrentService currentService;
	@Autowired
	private ElectronicModuleVoltageService voltageService;
	@Autowired
	private ElectronicModulePowerService powerService;
	@Autowired
	private ModuleHitchEventService moduleHitchEventService;
	@Autowired
	private ElectronicCtxStore ctxStore;
	@Autowired
	private ElectronicModuleHitchEventService hitchEventService;
	@Autowired
	private WebsiteServiceClient websiteService;
	@Autowired
	private GPRSModuleService gprsService;

	private Logger logger = Logger.getLogger(DLT645_07DataReceiver.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ChannelInfo info = new ChannelInfo();
		info.setCtx(ctx);
		ctxStore.add(info);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctxStore.remove(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String m = (String) msg;
		char[] data = CharUtils.removeSpace(m.toCharArray());
		if (isGPRSRegistered(ctx, data)) {
			if (check(ctx, data)) {
				handleSeparatedText(ctx, data);
			}
		}
	}
	
	/**
	 *	检查报文合法性
	 * @param ctx
	 * @param data
	 * @return
	 */
	private boolean check(ChannelHandlerContext ctx, char[] data) {
		return true;
	}
	
	/****************************** 分解耦合报文 *******************************/
	private void handleSeparatedText(ChannelHandlerContext ctx, char[] data) {
		Integer begin = 0;
		int pointIndex = begin;
		while (true) {
			int pos = StringCommonUtil.getFirstIndexOfEndTag(data, pointIndex, "16");
			if (pos != -1) {
				if (isSeparatedPoint(data, pos)) {
					//	确保开头合乎规范
					if ((begin == 0 && isSeparatedPoint(data, begin)) || 
							begin != 0) {
						// 分割出独立报文段
						char[] dataInfo = CharUtils.subChars(data, begin, pos - begin);
						handleIdenCode(ctx, dataInfo);
					}
					//如果分割点是整段报文的终点，结束
					if (pos == data.length) {
						break;
					}
					begin = pos;	
					pointIndex = begin;
				} else {
					//目标分割点非报文分割点
					pointIndex = pos;
					continue;
				}
			} else {
				break;
			}
		}
	}

	/**
	 * 检查index是否是报文分割点
	 * @param data
	 * @param index
	 * @return
	 */
	private boolean isSeparatedPoint(char[] data, int index) {
		// 如果index是data的终点，返回true
		if (index == data.length) {
			return true;
		}
		// 如果16后面是68xxxxxxxxxxxx68，返回true
		else if (data.length >= index + 16 &&
				CharUtils.equals(CharUtils.subChars(data, index, 2), CODE_68)
				&& CharUtils.equals(CharUtils.subChars(data, index + BYTE * 7, 2), CODE_68)) {
			return true;
		}
		return false;
	}
	
	/****************************** 分解耦合报文END *******************************/
	
	/****************************** GPRS相关 *******************************/
	
	/**
	 * 判断GPRS模块是否已经登录
	 * 
	 * @param ctx
	 * @param data
	 * @return
	 */
	private boolean isGPRSRegistered(ChannelHandlerContext ctx, char[] data) {
		if (CharUtils.startWith(data, GPRS_LOGIN) || CharUtils.startWith(data, GPRS_HEART)) {
			return handleGPRSFrame(ctx, data);
		}
		//	有这个Attribute说明GPRS模块已经注册成功
		if (null != CtxStore.getCtxAttribute(ctx, ATTRIBUTE_GPRS_ADDRESS)) {
			return true;
		}
		return false;
	}

	/**
	 * 处理GPRS帧
	 * 
	 * @param ctx
	 * @param data
	 * @return
	 */
	private boolean handleGPRSFrame(ChannelHandlerContext ctx, char[] data) {
		char[] gprsNumber = CharUtils.subChars(data, 12, 8);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= 6; i += 2) {
			// 示范 30 30 30 31，表示0001地址
			if (sb.length() == 0 && gprsNumber[i + 1] == '0') {
				continue;
			}
			sb.append(gprsNumber[i + 1]);
		}
		String gprsAddress = sb.toString();

		// 判断GPRS是否已在网站上注册
		String gprsId = gprsService.isGPRSAvailable(gprsAddress);
		if (null != gprsId) {
			CtxStore.setCtxAttribute(ctx, ATTRIBUTE_GPRS_ADDRESS, gprsAddress);
			// 更新TemperatureCtxStore的GPRSList
			GPRSCtxStore.addGPRS(gprsAddress);
			if (CharUtils.equals(data, 6, 8, CODE_01)) {
				// GPRS模块登录
				logger.info(gprsAddress + " GPRS模块登录成功");
			} else if (CharUtils.equals(data, 6, 8, CODE_03)) {
				logger.info(gprsAddress + " GPRS模块在线");
			}
			// initChannelInfo(gprsId, ctx);
		} else {
			// 如果网站上没注册gprs，否决此报文
			logger.info("未注册GPRS模块地址" + gprsAddress);
		}
		return false;
	}
	
	/****************************** GPRS相关END *******************************/

	private void handleIdenCode(ChannelHandlerContext ctx, char[] data) {
		// 登录包、心跳包，或长度过少的报文，忽略
		if (data.length < 10) {
			logger.info("忽略报文" + String.valueOf(data));
			return;
		}
		logger.info("处理报文：" + String.valueOf(data));
		
		//	第一次解析报文需要注册设备地址
		if (null == CtxStore.getCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED)) {
			getOnlineAddress(ctx, data);
			CtxStore.setCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED, 1);
		}
		
		char[] identification = CharUtils.subChars(data, BYTE * 10, BYTE * 4);

		// 电压
		if (CharUtils.startWith(identification, VOLTAGE)) {
			saveVoltage(ctx, data);
		}
		// 电流
		else if (CharUtils.startWith(identification, CURRENT)) {
			saveCurrent(ctx, data);
		}
		// 功率
		else if (CharUtils.startWith(identification, POWER)) {
			savePower(ctx, data);
		}
		// 异常
		else if (CharUtils.startWith(identification, EXCEPTION)) {
			handleException(ctx, data);
		} else {
			logger.info("非法报文：" + String.valueOf(data));
		}
		return;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	private String getOnlineAddress(ChannelHandlerContext ctx, char[] data) {
		ChannelInfo info = ctxStore.get(ctx);

		if (null != info && null != info.getAddress()) {
			return info.getModuleId();
		}
		// 如果十进制地址和数据库中的十进制地址一样，认为是同一个设备
		String address = getAddress(data);
		String decimalAddress = getDecimalAddress(address);
		if (null != ctxStore.getChannelInfoByDecimalAddress(decimalAddress)) {
			// TODO，已经有设备登录，报警
			return null;
		}
		List<ElectronicModule> modules = moduleService
				.selectByParameters(MyBatisMapUtil.warp("device_number", decimalAddress));
		if (null == modules || modules.size() != 1) {
			return null;
		}
		info.setAddress(address);
		info.setDecimalAddress(decimalAddress);
		ElectronicModule m = modules.get(0);
		info.setCompanyId(m.getCompanyId());
		info.setModuleId(m.getId());
		// TODO 通知platform系统

		// TODO 未测试，也应该不会上线
		// ChannelHandlerManager.addCtx(info.getMonitorId(), ctx);
		return info.getModuleId();
	}

	/****************************** 获取地址 *******************************/

	/**
	 * 获取设备地址
	 * 
	 * @param data
	 * @return
	 */
	private String getAddress(char[] data) {
		return CharUtils.newString(data, BYTE * 1, BYTE * 7);
	}

	/**
	 * 获取设备十进制地址
	 * 
	 * @param data
	 * @return
	 */
	private String getDecimalAddress(char[] data) {
		return getDecimalAddress(getAddress(data));
	}

	/**
	 * 获取设备十进制地址
	 * 
	 * @param data
	 * @return
	 */
	private String getDecimalAddress(String address) {
		int i = 0;
		for (;; i++) {
			char ch = address.charAt(i);
			if (!('a' == ch || 'A' == ch)) {
				break;
			}
		}
		address = TemperatureDeviceCommandUtil.reverseString(address.substring(i, address.length()));
		return Integer.parseInt(address) + "";
	}

	/****************************** 获取地址END *******************************/

	/****************************** 解析数值 *******************************/
	private void saveVoltage(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = String.valueOf(address);
		deviceNumber = Integer.parseInt(deviceNumber) + "";
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 2);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		ElectronicModuleVoltage voltage = new ElectronicModuleVoltage();
		voltage.setSubmoduleId(ctxStore.getModuleIdbyAddress(deviceNumber));
		voltage.setId(UUIDUtil.getUUID());
		voltage.setGmtCreate(new Date());
		voltage.setGmtModified(new Date());

		voltage.setTime(new Date()); // TODO
		voltage.setValue(val);
		if (CharUtils.equals(data, BYTE * 12, BYTE * 13, A_PHASE)) {
			voltage.setPhase("A");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, B_PHASE)) {
			voltage.setPhase("B");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, C_PHASE)) {
			voltage.setPhase("C");
		}
		voltageService.updateByPrimaryKey(voltage);
	}

	private void saveCurrent(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = String.valueOf(address);
		deviceNumber = Integer.parseInt(deviceNumber) + "";
		// ElectronicModule module =
		// moduleService.selectByDeviceNumber(deviceNumber);
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 3);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		ElectronicModuleCurrent current = new ElectronicModuleCurrent();
		current.setId(UUIDUtil.getUUID());
		current.setGmtCreate(new Date());
		current.setGmtModified(new Date());
		current.setSubmoduleId(ctxStore.getModuleIdbyAddress(deviceNumber));
		current.setTime(new Date());// TODO
		current.setValue(val);
		if (CharUtils.equals(data, BYTE * 12, BYTE * 13, A_PHASE)) {
			current.setPhase("A");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, B_PHASE)) {
			current.setPhase("B");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, C_PHASE)) {
			current.setPhase("C");
		}
		currentService.updateByPrimaryKey(current);
	}

	private void savePower(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = String.valueOf(address);
		deviceNumber = Integer.parseInt(deviceNumber) + "";
		// ElectronicModule module =
		// moduleService.selectByDeviceNumber(deviceNumber);
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 3);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		ElectronicModulePower power = new ElectronicModulePower();
		power.setId(UUIDUtil.getUUID());
		power.setGmtCreate(new Date());
		power.setGmtModified(new Date());
		power.setSubmoduleId(ctxStore.getModuleIdbyAddress(deviceNumber));
		power.setTime(new Date());// TODO
		power.setValue(val);
		if (CharUtils.equals(data, BYTE * 12, BYTE * 13, A_PHASE)) {
			power.setPhase("A");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, B_PHASE)) {
			power.setPhase("B");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, C_PHASE)) {
			power.setPhase("C");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, CODE_00)) {
			power.setPhase("D");
		}
		powerService.updateByPrimaryKey(power);
	}

	private void handleException(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = String.valueOf(address);
		deviceNumber = Integer.parseInt(deviceNumber) + "";
		ElectronicModule module = moduleService.selectByDeviceNumber(deviceNumber);
		String submoduleId = module.getId();
//		String monitorId = submodules.get(0).getDataMonitorId();
		ModuleHitchEvent moduleEvent = new ModuleHitchEvent();
		moduleEvent.setId(UUIDUtil.getUUID());
//		moduleEvent.setGroupId(module.getGroupId());
		moduleEvent.setHitchTime(new Date());
		moduleEvent.setModuleId(module.getId());
		moduleEvent.setCompanyId(module.getCompanyId());
//		moduleEvent.setMonitorId(monitorId);
		char[] hitchReason = CharUtils.subChars(data, BYTE * 14, BYTE * 2);
		// 失压 201
		if (CharUtils.equals(hitchReason, SHI_YA)) {
			moduleEvent.setType(HitchConst.HITCH_QUAN_SHI_YA);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_SHI_YA));
		}
		// 欠压 202
		else if (CharUtils.equals(hitchReason, QIAN_YA)) {
			moduleEvent.setType(HitchConst.HITCH_QIAN_YA);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_QIAN_YA));
		}
		// 过压 203
		else if (CharUtils.equals(hitchReason, GUO_YA)) {
			moduleEvent.setType(HitchConst.HITCH_GUO_YA);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_GUO_YA));
		}
		// 断相 204
		else if (CharUtils.equals(hitchReason, DUAN_XIANG)) {
			moduleEvent.setType(HitchConst.HITCH_DUAN_XIANG);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_DUAN_XIANG));
		}
		// 全失压 205
		else if (CharUtils.equals(hitchReason, QUAN_SHI_YA)) {
			moduleEvent.setType(HitchConst.HITCH_QUAN_SHI_YA);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_QUAN_SHI_YA));
		}
		// 失流 206
		else if (CharUtils.equals(hitchReason, SHI_LIU_UP) || CharUtils.equals(hitchReason, SHI_LIU_DOWN)) {
			moduleEvent.setType(HitchConst.HITCH_SHI_LIU);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_SHI_LIU));
		}
		// 过流 207
		else if (CharUtils.equals(hitchReason, GUO_LIU_UP) || CharUtils.equals(hitchReason, GUO_LIU_DOWN)) {
			moduleEvent.setType(HitchConst.HITCH_GUO_LIU);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_GUO_LIU));
		}
		// 断流 208
		else if (CharUtils.equals(hitchReason, DUAN_LIU_UP) || CharUtils.equals(hitchReason, DUAN_LIU_DOWN)) {
			moduleEvent.setType(HitchConst.HITCH_DUAN_LIU);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_DUAN_LIU));
		}
		// 未知报警 200
		else {
			moduleEvent.setType(200);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_UNKNOWN));
		}
		moduleHitchEventService.updateByPrimaryKey(moduleEvent);
		ElectronicModuleHitchEvent event = new ElectronicModuleHitchEvent();
		event.setId(UUIDUtil.getUUID());
		event.setHitchId(moduleEvent.getId());
		// TODO
		hitchEventService.updateByPrimaryKey(event);
		HitchEventDTO dto = new HitchEventDTO();
		ChannelInfo info = ctxStore.get(ctx);
		dto.setId(moduleEvent.getId());
		dto.setCompanyId(info.getCompanyId());
		dto.setModuleId(info.getModuleId());
		dto.setType(moduleEvent.getType());
		dto.setText(moduleEvent.getHitchReason());
		websiteService.getService().callbackHitchEvent(dto);
	}

	/****************************** 解析数值END *******************************/

}
