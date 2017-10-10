package com.gdut.dongjun.core.handler.msg_decoder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.GPRSCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.ModuleHitchEventService;
import com.gdut.dongjun.service.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.service.TemperatureMeasureService;
import com.gdut.dongjun.service.TemperatureModuleService;
import com.gdut.dongjun.service.TemperatureSensorService;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.Standard101Util;
import com.gdut.dongjun.util.StringCommonUtil;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 由于链路建立的判断是第一次总召正常返回，所以对时命令设置在第一次总召结束之后。
 * 
 * @author Gordan_Deng
 * @date 2017年8月7日
 */
public class TemperatureDataReceiver1 extends ChannelInboundHandlerAdapter {

	// GPRS相关
	private static final char[] GPRS_LOGIN = { '0', '0', '2', '5' };
	private static final char[] GPRS_HEART = { '0', '0', '0', 'a' };
	private static final char[] CODE_EB90 = new char[] { 'e', 'b', '9', '0', 'e', 'b', '9', '0', 'e', 'b', '9', '0' };
	private static final String ATTRIBUTE_GPRS_ADDRESS = "GPRS_ADDRESS";

	// 取值字段
	private static final char[] EB_UP = new char[] { 'E', 'B', '9', '0' }; // EB90
	private static final char[] EB_DOWN = new char[] { 'e', 'b', '9', '0' }; // eb90
	private static final char[] CODE_64 = new char[] { '6', '4' }; // 64
	private static final char[] CODE_03 = new char[] { '0', '3' }; // 03
	private static final char[] CODE_1F_UP = new char[] { '1', 'F' }; // 1F
	private static final char[] CODE_1F_DOWN = new char[] { '1', 'f' }; // 1f
	private static final char[] CODE_09 = new char[] { '0', '9' }; // 09
	private static final char[] CODE_01 = new char[] { '0', '1' }; // OO
	private static final char[] CODE_7716 = new char[] { '7', '7', '1', '6' }; // 7716
	private static final char[] CODE_7A16_UP = new char[] { '7', 'A', '1', '6' }; // 7A16
	private static final char[] CODE_7A16_DOWN = new char[] { '7', 'a', '1', '6' }; // 7A16
	private static final char[] CODE_68 = new char[] { '6', '8' };
	private static final char[] CODE_16 = new char[] { '1', '6' };
	private static final char[] CODE_67 = new char[] { '6', '7' };

	private static final int BYTE = 2;
	public static final String ATTRIBUTE_TEMPERATURE_MODULE_IS_REGISTED = "TEMPERATURE_MODULE_IS_REGISTED";
	public static final String ATTRIBUTE_FIRST_CALL = "TEMPERATURE_MODULE_FIRST_CALL";

	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private TemperatureModuleService moduleService;
	@Autowired
	private ModuleHitchEventService moduleHitchEventService;
	@Autowired
	private WebsiteServiceClient websiteService;
	@Autowired
	private TemperatureMeasureHitchEventService hitchEventService;
	@Autowired
	private TemperatureCtxStore ctxStore;
	@Autowired
	private GPRSModuleService gprsService;

	private Logger logger = Logger.getLogger(TemperatureDataReceiver1.class);

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
			} else {
				logger.info("非法报文：" + String.valueOf(data));
			}
		} else {
			logger.info("GPRS未登录，丢弃报文：" + String.valueOf(data));
		}
	}

	/**
	 * 检查报文合法性
	 * 
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
					// 确保开头合乎规范
					if ((begin == 0 && isSeparatedPoint(data, begin)) || begin != 0) {
						// 分割出独立报文段
						char[] dataInfo = CharUtils.subChars(data, begin, pos - begin);
						handleIdenCode(ctx, dataInfo);
					}
					// 如果分割点是整段报文的终点，结束
					if (pos == data.length) {
						break;
					}
					begin = pos;
					pointIndex = begin;
				} else {
					// 目标分割点非报文分割点
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
	 * 
	 * @param data
	 * @param index
	 * @return
	 */
	private boolean isSeparatedPoint(char[] data, int index) {
		// 如果index是data的终点，返回true
		if (index == data.length) {
			return true;
		}
		// 如果16后面是68xxxx68，返回true
		else if (data.length >= index + 8 && CharUtils.equals(CharUtils.subChars(data, index, 2), CODE_68)
				&& CharUtils.equals(CharUtils.subChars(data, index + 6, 2), CODE_68)) {
			return true;
		}
		// 如果16后面是eb90eb90eb90xxxx16，返回true
		else if (index + 18 <= data.length && // 防止出现数据丢包导致后续报文不足18个而导致NPE
				CharUtils.equals(CharUtils.subChars(data, index, 12), CODE_EB90)
				&& CharUtils.equals(CharUtils.subChars(data, index + 16, 2), CODE_16)) {
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
		// 有这个Attribute说明GPRS模块已经注册成功
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
		// 报文少于登录包和心跳包，返回
		if (data.length < 22) {
			return;
		}
		logger.info("处理报文：" + String.valueOf(data));

		if (CharUtils.startWith(data, CODE_68)) {
			if (null == CtxStore.getCtxAttribute(ctx, ATTRIBUTE_TEMPERATURE_MODULE_IS_REGISTED)) {
				getOnlineAddress(ctx, data);
				CtxStore.setCtxAttribute(ctx, ATTRIBUTE_TEMPERATURE_MODULE_IS_REGISTED, 1);
			}
		}

		char[] controlCode = ArrayUtils.subarray(data, 18, 20);
		if (CharUtils.startWith(data, EB_UP) || CharUtils.startWith(data, EB_DOWN)) {
			/*
			 * 读通信地址并将地址反转,确认连接
			 */
			logger.info("主站接收到连接");
			getOnlineAddress(ctx, data);
			// 需要返回eb90报文结束登录流程
			StringBuilder sb = new StringBuilder();
			sb.append("eb90eb90eb90").append(CharUtils.newString(data, 12, 16)).append("16");
			ctx.channel().writeAndFlush(sb.toString());
		}

		else if (CharUtils.equals(controlCode, CODE_64) || CharUtils.endsWith(data, CODE_7716)) {
			/*
			 * 终端发送总招激活确认
			 */
		}

		else if (CharUtils.equals(controlCode, CODE_64)
				&& (CharUtils.endsWith(controlCode, CODE_7A16_UP) || CharUtils.endsWith(controlCode, CODE_7A16_DOWN))) {
			/*
			 * 终端确认总召结束
			 */
		}

		else if (CharUtils.equals(controlCode, CODE_09)) {
			/*
			 * 全遥测 + 遥测变位
			 */
			handleRemoteMeasure(ctx, data);
		}

		else if (CharUtils.equals(controlCode, CODE_01)) {
			/*
			 * 全遥信
			 */
			handleRemoteSignal(ctx, data);
		}

		else if (CharUtils.equals(controlCode, CODE_03)) {
			/*
			 * 遥信变位，双点遥信，01为分位，02为合位，03为未知状态
			 */
			handleRemoteSignalChange(ctx, data);
		}

		else if (CharUtils.equals(controlCode, CODE_1F_UP) || CharUtils.equals(controlCode, CODE_1F_DOWN)) {
			/*
			 * 遥信变位事件
			 */
			// logger.info("遥信变位事件");
			// 主站需要发送确认报文
			handleRemoteSignalChangeEvent(ctx, data);
		} else if (CharUtils.startWith(controlCode, CODE_67)) {
			/*
			 * 控制器响应对时命令
			 */
			logger.info("对时确认：" + String.valueOf(data));
		} else {
			logger.warn("非法数据：" + String.valueOf(data));
		}
		return;
	}

	/**
	 * 获取设备地址
	 * 
	 * @param ctx
	 * @param data
	 * @return
	 */
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
		List<TemperatureModule> modules = moduleService
				.selectByParameters(MyBatisMapUtil.warp("device_number", decimalAddress));
		if (null == modules || modules.size() != 1) {
			return null;
		}
		info.setAddress(address);
		info.setDecimalAddress(decimalAddress);
		TemperatureModule m = modules.get(0);
		info.setCompanyId(m.getCompanyId());
		info.setModuleId(m.getId());
		// TODO 通知platform系统

		// TODO 未测试，也应该不会上线
		// ChannelHandlerManager.addCtx(info.getMonitorId(), ctx);
		return info.getModuleId();
	}

	/****************************** 获取地址 *******************************/

	private String getDecimalAddress(char[] data) {
		String address = TemperatureDeviceCommandUtil.reverseString(getAddress(data));
		return Integer.parseInt(address) + "";
	}

	private String getDecimalAddress(String address) {
		return Integer.parseInt(address) + "";
	}

	private String getAddress(char[] data) {
		return CharUtils.newString(data, 10, 18).intern();
	}

	/****************************** 获取地址END *******************************/

	/****************************** 解析数值 *******************************/

	/**
	 * 处理遥测
	 * 
	 * @param ctx
	 * @param data
	 */
	private void handleRemoteMeasure(ChannelHandlerContext ctx, char[] data) {
		// String address = CharUtils.newString(data, 10, 18); // 地址域

		ChannelInfo info = ctxStore.get(ctx);
		String moduleId = info.getModuleId();

		if (CharUtils.equals(data, 20, 22, CODE_01)) {
			// 处理遥测变化
			logger.info("解析温度遥测变位-------" + String.valueOf(data));
			String signalAddress = CharUtils.newString(data, 34, 38);
			String value = CharUtils.newString(data, 38, 38 + 4);
			int tag = changeSignalAddress(signalAddress);
			doSaveMeasure(value, moduleId, tag, ctx);
			StringBuilder sb = new StringBuilder();
			sb.append("eb90eb90eb90" + CharUtils.newString(data, 10, 18) + "16");
			ctx.channel().writeAndFlush(sb.toString()); // 全遥测确认报文，提示控制器发送全遥信
		} else {
			// 处理全遥测
			logger.info("解析温度全遥测-------" + CharUtils.newString(data));
			String[] buffer = new String[16];
			for (int i = 0; i < 16; i++) {
				buffer[i] = CharUtils.newString(data, 52 + 6 * i, 52 + 6 * i + 4);
			}
			doSaveMeasure(buffer, moduleId, ctx);
			StringBuilder sb = new StringBuilder();
			sb.append("eb90eb90eb90" + CharUtils.newString(data, 10, 18) + "16");
			ctx.channel().writeAndFlush(sb.toString()); // 全遥测确认报文，提示控制器发送全遥信
		}
	}

	/**
	 * 处理全遥信，单点遥信，00是分位，01是合位 TODO
	 * 
	 * @param ctx
	 * @param data
	 */
	private void handleRemoteSignal(ChannelHandlerContext ctx, char[] data) {

		if (data.length != (26 + 55 + 2) * BYTE) {
			logger.info("非法全遥信报文，长度不足" + String.valueOf(data));
			return;
		}
		ChannelInfo info = ctxStore.get(ctx);
		String moduleId = info.getModuleId();
		int index = 26 * BYTE;
		for (int i = index; i < data.length - 2 * BYTE; i = i + BYTE) {
			if (i >= index + 39 * BYTE) {
				//	传感器电量报警
				if (data[i + 1] == '1') {
					int tag = (i - index - 39 * BYTE) / 2 + 1;
					TemperatureModule device = moduleService.selectByPrimaryKey(moduleId);
					ModuleHitchEvent moduleHitch = new ModuleHitchEvent();
					moduleHitch.setId(UUIDUtil.getUUID());
					moduleHitch.setCompanyId(device.getCompanyId());
					moduleHitch.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_ELECTRICITY_LACK));
					moduleHitch.setHitchTime(new Date());
					moduleHitch.setModuleId(device.getId());
					moduleHitch.setType(HitchConst.HITCH_ELECTRICITY_LACK);
					moduleHitchEventService.insert(moduleHitch);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("eb90eb90eb90" + CharUtils.newString(data, 10, 18) + "16");
		ctx.channel().writeAndFlush(sb.toString());

		// 每次总召结束后都对时
		String correctTime = doMatchTime(CharUtils.newString(data, 10, 18));
		logger.info("总召对时：" + correctTime);
		ctx.writeAndFlush(correctTime);
	}

	/**
	 * 遥信变位
	 * 
	 * @param ctx
	 * @param data
	 */
	private void handleRemoteSignalChange(ChannelHandlerContext ctx, char[] data) {
		String addr = ctxStore.get(ctx).getAddress();
		logger.info("遥信变位：" + String.valueOf(data));
		String msg = Standard101Util.getConfirmFrame(addr);
		if (logger.isDebugEnabled()) {
			logger.debug("发送到" + addr + "的确认帧" + msg);
		}
		ctx.writeAndFlush(msg);
	}

	public Integer changeSignalAddress(String signalAddress) {
		return Integer.parseInt(signalAddress.substring(2, 4), 16);
	}
	
	/**
	 * 处理遥信变位事件
	 * @param ctx
	 * @param data
	 */
	private void handleRemoteSignalChangeEvent(ChannelHandlerContext ctx, char[] data) {
		logger.info("遥信变位事件：" + String.valueOf(data));
		String addr = ctxStore.get(ctx).getAddress();
		String msg = Standard101Util.getConfirmFrame(addr);
		if (logger.isDebugEnabled()) {
			logger.debug("发送到" + addr + "的确认帧" + msg);
		}
		ctx.writeAndFlush(msg);
		
		for (int i = BYTE * 17; i + BYTE * 12 <= data.length; i += BYTE * 10) {
			getSignalValue(
					CharUtils.newString(data, i, i + BYTE * 2),
                    ctxStore.get(ctx).getModuleId(),
                    CharUtils.newString(data, i + BYTE * 2, i + BYTE * 10));
		}
	}
	
	/**
	 * 获取单点遥信值
	 * @param addr
	 * @param deviceNumber
	 * @param value
	 */
	private void getSignalValue(String addr, String moduleId, String value) {
		addr = TemperatureDeviceCommandUtil.reverseString(addr);
		addr = addr.substring(0, 2);
		Integer tag = null;
		switch (addr) {
		//	传感器电量报警
		case "28" : tag = 1; break;
		case "29" : tag = 2; break;
		case "2a" : tag = 3; break;
		case "2b" : tag = 4; break;
		case "2c" : tag = 5; break;
		case "2d" : tag = 6; break;
		case "2e" : tag = 7; break;
		case "2f" : tag = 8; break;
		case "30" : tag = 9; break;
		case "31" : tag = 10; break;
		case "32" : tag = 11; break;
		case "33" : tag = 12; break;
		case "34" : tag = 13; break;
		case "35" : tag = 14; break;
		case "36" : tag = 15; break;
		case "37" : tag = 16; break;
		}
		if (null != tag) {
			String v = value.substring(0, 2);
			String date = value.substring(2);
			if ("02".equals(v)) {
				TemperatureModule device = moduleService.selectByPrimaryKey(moduleId);
				ModuleHitchEvent moduleHitch = new ModuleHitchEvent();
				moduleHitch.setId(UUIDUtil.getUUID());
				moduleHitch.setCompanyId(device.getCompanyId());
				moduleHitch.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_ELECTRICITY_LACK));
				moduleHitch.setHitchTime(getTime(value));
				moduleHitch.setModuleId(device.getId());
				moduleHitch.setType(HitchConst.HITCH_ELECTRICITY_LACK);
				moduleHitchEventService.insert(moduleHitch);
			}
		}
	}

	/**
	 * 保存遥测变位
	 * 
	 * @param deviceId
	 * @param value
	 */
	private void doSaveMeasure(String value, String deviceId, int tag, ChannelHandlerContext ctx) {
		List<TemperatureSensor> sensorList = sensorService
				.selectByParameters(MyBatisMapUtil.warp("device_id", deviceId));
		isSensorExist(sensorList, tag, deviceId);
		measureService.insert(new TemperatureMeasure(UUIDUtil.getUUID(), deviceId, new Date(), tag,
				String.valueOf(Integer.parseInt(value, 16) * 10)));
//		doSaveMeasure0(value, deviceId, tag);
		if (ctxStore.isAboveBound(deviceId, Double.valueOf("" + Integer.parseInt(value, 16)) / 10)) {
			createHitchEvent(deviceId, value, tag, ctx);
		}
	}

	/**
	 * 保存全遥测
	 * 
	 * @param value
	 * @param deviceId
	 */
	private void doSaveMeasure(String[] value, String deviceId, ChannelHandlerContext ctx) {
		List<TemperatureSensor> sensorList = sensorService
				.selectByParameters(MyBatisMapUtil.warp("device_id", deviceId));

		for (int i = 1; i <= value.length; i++) {
			if (value[i - 1].equals("0000")) {
				continue;
			}
			// 判断传感器是否在数据库中存在，若没有则执行insert
			isSensorExist(sensorList, i, deviceId);
			measureService.insert(new TemperatureMeasure(UUIDUtil.getUUID(), deviceId,
					new Timestamp(System.currentTimeMillis()), i, Integer.parseInt(value[i - 1], 16) * 10 + ""));
//			doSaveMeasure0(value[i - 1], deviceId, i);
			// 插入报警数据
			if (ctxStore.isAboveBound(deviceId, Double.valueOf("" + Integer.parseInt(value[i - 1], 16)) / 10)) {
				createHitchEvent(deviceId, value[i - 1], i, ctx);
			}
		}
	}

	private void createHitchEvent(String deviceId, String value, int tag, ChannelHandlerContext ctx) {
		TemperatureModule device = moduleService.selectByPrimaryKey(deviceId);
		ModuleHitchEvent moduleHitch = new ModuleHitchEvent();
		moduleHitch.setId(UUIDUtil.getUUID());
		moduleHitch.setCompanyId(device.getCompanyId());
		moduleHitch.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_OVER_TEMPERATURE));
		moduleHitch.setHitchTime(new Date());
		moduleHitch.setModuleId(device.getId());
		moduleHitch.setType(HitchConst.HITCH_OVER_TEMPERATURE);
		moduleHitchEventService.insert(moduleHitch);

		// TODO 其实报警事事件应该是时间戳中的时间
		TemperatureMeasureHitchEvent event = new TemperatureMeasureHitchEvent();
		event.setId(UUIDUtil.getUUID());
		event.setValue(new BigDecimal(Double.valueOf("" + Integer.parseInt(value, 16)) / 10));
		event.setTag(tag);
		event.setMaxHitchValue(device.getMaxHitchValue());
		event.setMinHitchValue(device.getMinHitchValue());
		event.setHitchId(moduleHitch.getId());
		hitchEventService.updateByPrimaryKey(event);
		// 把报警信息传到推送到前端
		HitchEventDTO vo = new HitchEventDTO();
		ChannelInfo info = ctxStore.get(ctx);
		vo.setCompanyId(device.getCompanyId());
		vo.setModuleId(info.getModuleId());
		vo.setType(HitchConst.HITCH_OVER_TEMPERATURE);
		vo.setId(moduleHitch.getId());
		vo.setText(moduleHitch.getHitchReason());
		websiteService.getService().callbackHitchEvent(vo);
	}

	/**
	 * 确认传感器受否存在，否则新增传感器实体
	 * 
	 * @param sensorList
	 * @param tag
	 * @param deviceId
	 */
	private void isSensorExist(List<TemperatureSensor> sensorList, int tag, String deviceId) {
		for (TemperatureSensor sensor : sensorList) {
			if (sensor.getTag() == tag) {
				return;
			}
		}
		sensorService.insert(new TemperatureSensor(UUIDUtil.getUUID(), tag, 7, deviceId, tag + "号传感器"));
	}

	/**
	 * 得到对时数据 示范 00211e02180311
	 * 68161668F4123456786701060112345678000000211e02180311f816 地址为12345678
	 * 
	 * @param address
	 * @return
	 */
	private static String doMatchTime(String address) {
		Calendar cal = Calendar.getInstance();
		String second = Integer.toHexString(cal.get(Calendar.SECOND));
		String minute = Integer.toHexString(cal.get(Calendar.MINUTE));
		String hour = Integer.toHexString(cal.get(Calendar.HOUR));
		String day = Integer.toHexString(cal.get(Calendar.DATE));
		String month = Integer.toHexString(cal.get(Calendar.MONTH) + 1);
		String year = cal.get(Calendar.YEAR) + "";
		year = year.substring(2, 4);
		year = Integer.toHexString(Integer.parseInt(year));
		String time = "00" + ((second.length() == 1) ? "0" + second : second)
				+ ((minute.length() == 1) ? "0" + minute : minute) + ((hour.length() == 1) ? "0" + hour : hour)
				+ ((day.length() == 1) ? "0" + day : day) + ((month.length() == 1) ? "0" + month : month)
				+ ((year.length() == 1) ? "0" + year : year);
		TemperatureDeviceCommandUtil td = new TemperatureDeviceCommandUtil(address);
		return td.getTimeCheck(time);
	}

	private boolean checkLength(char[] length, char[] data) {

		Integer len = Integer.parseInt(String.valueOf(length), 16);
		if (null != len && data.length == len) {
			return true;
		}
		return false;
	}

	/**
	 * 校验和是从控制域到信息元素集所有字节的算术和，忽略进位
	 * 
	 * @param data
	 * @return
	 */
	private boolean checkSum(char[] data) {
		if (data.length < 6 * BYTE) {
			return false;
		}
		int index = 4 * BYTE;
		int endIndex = data.length - 2 * BYTE;
		int sum = 0;
		for (; index < endIndex; index += 2) {
			sum += (16 * CharUtils.charToInt(data[index]) + CharUtils.charToInt(data[index + 1]));
		}
		sum &= 0xff; // 忽略进位
		int checkSum = 16 * CharUtils.charToInt(data[data.length - 2 * BYTE])
				+ CharUtils.charToInt(data[data.length - 2 * BYTE + 1]); // 报文中的校验和
		if (sum == checkSum) {
			return true;
		}
		return false;
	}

	// TODO
	private boolean checkTime(char[] time) {
		return true;
	}
	
	/**
	 * 二进制时间转为Date
	 * @param value
	 * @return
	 */
	private Date getTime(String value) {
		String sec = value.substring(0, BYTE * 2);
		String min = value.substring(BYTE * 2, BYTE * 3);
		String hour = value.substring(BYTE * 3, BYTE * 4);
		String day = value.substring(BYTE * 4, BYTE * 5);
		String month = value.substring(BYTE * 5, BYTE * 6);
		String year = value.substring(BYTE * 6, BYTE * 7);
		Calendar c = Calendar.getInstance();

		//	TODO，秒需要确定
		c.set(Calendar.SECOND, Integer.parseInt(sec, 16) / 1000);
		c.set(Calendar.MINUTE, Integer.parseInt(min, 16));
		c.set(Calendar.HOUR, Integer.parseInt(hour, 16));
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day, 16));
		c.set(Calendar.MONTH, Integer.parseInt(month, 16));
		c.set(Calendar.YEAR, Integer.parseInt("20" + Integer.parseInt(year, 16), 16));
		return c.getTime();
	}

	/****************************** 解析数值END *******************************/

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

}
