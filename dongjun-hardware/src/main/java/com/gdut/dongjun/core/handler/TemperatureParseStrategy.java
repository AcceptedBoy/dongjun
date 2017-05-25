package com.gdut.dongjun.core.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.service.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.ModuleHitchEventService;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.service.TemperatureMeasureService;
import com.gdut.dongjun.service.TemperatureModuleService;
import com.gdut.dongjun.service.TemperatureSensorService;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * List<Object>返回： 序号 类型 意义 1 String 设备地址 2 String 设备十进制地址
 * 
 * 以后的架构改动就是AbstractDataReceiver调用ParseStrategyAdaptor，然后调用ParseStrategy
 * 
 * @author Gordan_Deng
 * @date 2017年5月11日
 */
@Component
public class TemperatureParseStrategy extends ParseStrategy implements InitializingBean {

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
	private static final int BYTE = 2;
	public static final String ATTRIBUTE_TEMPERATURE_MODULE_IS_REGISTED = "TEMPERATURE_MODULE_IS_REGISTED";
	public static final String ATTRIBUTE_FIRST_CALL = "TEMPERATURE_MODULE_FIRST_CALL";

	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private TemperatureMeasureHistoryService measureHistoryService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private TemperatureModuleService temModuleService;
	@Autowired
	private TemperatureCtxStore ctxStore;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;
	@Autowired
	private ModuleHitchEventService moduleHitchEventService;
	@Autowired
	private WebsiteServiceClient websiteService;
	@Autowired
	private TemperatureMeasureHitchEventService hitchEventService;

//	private Logger logger = Logger.getLogger(TemperatureParseStrategy.class);

	public TemperatureParseStrategy() {
		super("301", Logger.getLogger(TemperatureParseStrategy.class));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.ctxStore = ctxStore;
	}

	@Override
	protected String getDecimalAddress(char[] data) {
		String address = TemperatureDeviceCommandUtil.reverseString(getAddress(data));
		return Integer.parseInt(address) + "";
	}

	@Override
	protected String getAddress(char[] data) {
		return CharUtils.newString(data, 10, 18).intern();
	}

	@Override
	protected boolean check(ChannelHandlerContext ctx, char[] data) {

		/*
		 * TODO 做时间戳 + 设备地址 + 校验和的验证工作，如果不行就记录非法报文数量。 非法报文数量超过一定程度后封锁该设备地址。
		 * 一旦一天积聚量超过一定程度over。 一旦某段时间内非法报文数量过多over。
		 */
		if (!checkSum(data)) {
			return false;
		}
		// 一般是原来的Channel接上来了，但是原来的地址错误，然后在网站改了地址。这个时候不会设置Attribute，一切合理。
		// 还有一种是原来是可以正常工作的，然后人工改动了地址，然后调用hardwareService清空了ChannelInfo，
		// 但是原来的ChannelHandlerContext的Attribute还残留着，在这里还会导致报文通过。于是ChannelInfo没有ctx和address，
		// 导致解析过程出错。现在的方法是在HardwareService手动删除Attribute。但是这样只能public
		// AtributeName，不划算。
		Integer isRegisted = (Integer) CtxStore.getCtxAttribute(ctx, ATTRIBUTE_TEMPERATURE_MODULE_IS_REGISTED);
		if (null == isRegisted) {
			if (null != getOnlineAddress(ctx, data)) {
				CtxStore.setCtxAttribute(ctx, ATTRIBUTE_TEMPERATURE_MODULE_IS_REGISTED, new Integer(1));
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Object parseInternal(ChannelHandlerContext ctx, char[] data) {
		// 报文少于登录包和心跳包，返回
		if (data.length < 20) {
			logger.info("接收到的非法数据--------------------" + String.valueOf(data));
			return null;
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
			 * 遥信变位信息（就像报告它接下来要发送遥信变位事件）
			 */
			// logger.info("接受来自 的双点遥信数据");
			// 双点遥信TODO 主站需要发送确认报文 然后接受遥信变位事件
			// handleRemoteSignal(ctx, data);
		}

		else if (CharUtils.equals(controlCode, CODE_1F_UP) || CharUtils.equals(controlCode, CODE_1F_DOWN)) {
			/*
			 * 遥信变位事件
			 */
			// logger.info("遥信变位事件");
			// 主站需要发送确认报文
			handleRemoteSignalChange(ctx, data);
		} else {
			logger.warn("接收到的非法数据--------------------" + String.valueOf(data));
		}
		return null;
	}

	/**
	 * 处理遥测
	 * 
	 * @param ctx
	 * @param data
	 */
	private void handleRemoteMeasure(ChannelHandlerContext ctx, char[] data) {
//		String address = CharUtils.newString(data, 10, 18); // 地址域

		// 第一次总召对时
		Integer call = (Integer) CtxStore.getCtxAttribute(ctx, ATTRIBUTE_FIRST_CALL);
		if (null == call) {
			CtxStore.setCtxAttribute(ctx, ATTRIBUTE_FIRST_CALL, new Integer(1));
			String correctTime = doMatchTime(CharUtils.newString(data, 10, 18));
			ctx.writeAndFlush(correctTime);
		}

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
	 * 处理全遥信
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
				if (data[i + 1] == '1') {
					int tag = (i - index - 39 * BYTE) / 2 + 1;
					TemperatureModule device = temModuleService.selectByPrimaryKey(moduleId);
					List<DataMonitorSubmodule> submoduleList = submoduleService
							.selectByParameters(MyBatisMapUtil.warp("module_id", device.getId()));
					if (null == submoduleList || 1 != submoduleList.size()) {
						logger.info("该温度子模块没有注册");
						return;
					}
					// 以后要改一下这里 TODO
					DataMonitorSubmodule submodule = submoduleList.get(0);
					ModuleHitchEvent moduleHitch = new ModuleHitchEvent();
					moduleHitch.setId(UUIDUtil.getUUID());
					moduleHitch.setGroupId(device.getGroupId());
					moduleHitch.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_ELECTRICITY_LACK));
					moduleHitch.setHitchTime(new Date());
					moduleHitch.setModuleId(device.getId());
					moduleHitch.setMonitorId(submodule.getDataMonitorId());
					moduleHitch.setType(HitchConst.HITCH_ELECTRICITY_LACK);
					moduleHitchEventService.updateByPrimaryKey(moduleHitch);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("eb90eb90eb90" + CharUtils.newString(data, 10, 18) + "16");
		ctx.channel().writeAndFlush(sb.toString());
	}

	/**
	 * 遥信变位 TODO
	 * 
	 * @param ctx
	 * @param data
	 */
	private void handleRemoteSignalChange(ChannelHandlerContext ctx, char[] data) {
		StringBuilder sb = new StringBuilder();
		sb.append("eb90eb90eb90" + CharUtils.newString(data, 10, 18) + "16");
		ctx.channel().writeAndFlush(sb.toString()); // 全遥测确认报文，提示控制器发送全遥信
	}

	public Integer changeSignalAddress(String signalAddress) {
		return Integer.parseInt(signalAddress.substring(2, 4), 16);
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
		measureHistoryService.insert(new TemperatureMeasureHistory(UUIDUtil.getUUID(), deviceId,
				new Timestamp(System.currentTimeMillis()), tag, Integer.parseInt(value, 16) * 10 + ""));
		doSaveMeasure0(value, deviceId, tag);
		if (ctxStore.isAboveBound(deviceId, 
				Double.valueOf("" + Integer.parseInt(value, 16)) / 10)) {
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
			//判断传感器是否在数据库中存在，若没有则执行insert
			isSensorExist(sensorList, i, deviceId);
			measureHistoryService.insert(new TemperatureMeasureHistory(UUIDUtil.getUUID(), deviceId,
					new Timestamp(System.currentTimeMillis()), i, Integer.parseInt(value[i - 1], 16) * 10 + ""));
			doSaveMeasure0(value[i - 1], deviceId, i);
			// 插入报警数据
			if (ctxStore.isAboveBound(deviceId,
					Double.valueOf("" + Integer.parseInt(value[i - 1], 16)) / 10)) {
				createHitchEvent(deviceId, value[i - 1], i, ctx);
			}
		}
	}

	private void createHitchEvent(String deviceId, String value, int tag, ChannelHandlerContext ctx) {
		TemperatureModule device = temModuleService.selectByPrimaryKey(deviceId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("module_id", device.getId());
		map.put("module_type", 3);
		List<DataMonitorSubmodule> submoduleList = submoduleService
				.selectByParameters(MyBatisMapUtil.warp(map));
		if (null == submoduleList || 1 != submoduleList.size()) {
			logger.info("该温度子模块没有注册");
			return;
		}
		DataMonitorSubmodule submodule = submoduleList.get(0);
		ModuleHitchEvent moduleHitch = new ModuleHitchEvent();
		moduleHitch.setId(UUIDUtil.getUUID());
		moduleHitch.setGroupId(device.getGroupId());
		moduleHitch.setHitchReason(HitchConst.getHitchReason(HitchConst.HITCH_OVER_TEMPERATURE));
		moduleHitch.setHitchTime(new Date());
		moduleHitch.setModuleId(device.getId());
		moduleHitch.setMonitorId(submodule.getDataMonitorId());
		moduleHitch.setType(HitchConst.HITCH_OVER_TEMPERATURE);
		moduleHitchEventService.updateByPrimaryKey(moduleHitch);

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
		vo.setGroupId(info.getGroupId());
		vo.setModuleId(info.getModuleId());
		vo.setMonitorId(info.getMonitorId());
		vo.setType(HitchConst.HITCH_OVER_TEMPERATURE);
		vo.setId(moduleHitch.getId());
		websiteService.getService().callbackHitchEvent(vo);
//		hitchEventManager.addHitchEvent(event);
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
	 * 保存遥测数据
	 * 
	 * @param value
	 * @param deviceId
	 * @param tag
	 */
	private void doSaveMeasure0(String value, String deviceId, int tag) {

		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("device_id", deviceId);
		queryMap.put("tag", tag);
		List<TemperatureMeasure> list = measureService.selectByParameters(MyBatisMapUtil.warp(queryMap));
		if (list.size() == 0) {
			measureService.insert(new TemperatureMeasure(UUIDUtil.getUUID(), deviceId,
					new Timestamp(System.currentTimeMillis()), tag, Integer.parseInt(value, 16) * 10 + ""));
		} else {
			TemperatureMeasure measure = list.get(0);
			measure.setDate(new Timestamp(System.currentTimeMillis()));
			measure.setValue("" + Integer.parseInt(value, 16) * 10);
			measureService.updateByPrimaryKey(measure);
		}
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

	@Override
	public Object clearCache(ChannelHandlerContext ctx) {
		CtxStore.removeCtxAttribute(ctx, ATTRIBUTE_TEMPERATURE_MODULE_IS_REGISTED);
		CtxStore.removeCtxAttribute(ctx, ATTRIBUTE_FIRST_CALL);
		return null;
	}

}
