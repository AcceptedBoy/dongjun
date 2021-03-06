package com.gdut.dongjun.core.handler.msg_decoder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.GPRSCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.core.handler.thread.HitchEventManager;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
//import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.service.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.ModuleHitchEventService;
//import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.TemperatureMeasureService;
import com.gdut.dongjun.service.TemperatureModuleService;
import com.gdut.dongjun.service.TemperatureSensorService;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * GPRS模块一连接系统就会发登录包
 * 登录包解析后会在{@code TemperatureCtxStore}里维护GPRS登录情况
 * 关于温度设备的地址，那边的温度设备是分高字节段和低字节段的，高、低字节段都有两个字节。
 * 低字节段在前，高字节段在后。而字节段里两个字节也是低字节在前，高字节在后。
 * 所以当报文以12 34 56 78发过来的时候，低字节段正确的顺序是34 12，高字节段正确的顺序是78 56。
 * 高在前低在后，拼成正确顺序之后转成10进制，得到真正的逻辑地址。
 * 
 * 消息过大的时候会压爆netty，怎么处理?勉神和面试官说的时候是硬件和netty连接的时候，
 * 中间加一个消息中间件做缓冲。
 * 
 * 还有，报文解析的时候怎么做到当用到新的协议，后台改动最少的代码来使得协议上线。
 * 责任链或许会引起性能问题，但不失为一个好方法
 * @author Gordan_Deng
 * @date 2017年3月23日
 */
@Service
@Sharable
public class TemperatureDataReceiver extends ChannelInboundHandlerAdapter {

	private static final int BYTE =  2;
	
	private static final char[] EB_UP = new char[] { 'E', 'B', '9', '0' }; // EB90
	private static final char[] EB_DOWN = new char[] { 'e', 'b', '9', '0' }; // eb90
	private static final char[] CODE_64 = new char[] { '6', '4' }; // 64
	private static final char[] CODE_03 = new char[] { '0', '3' }; // 03
	// private static final char[] CODE_2E = new char[]{'2', 'e'}; //2e
	private static final char[] CODE_1F_UP = new char[] { '1', 'F' }; // 1F
	private static final char[] CODE_1F_DOWN = new char[] { '1', 'f' }; // 1f
	private static final char[] CODE_09 = new char[] { '0', '9' }; // 09
	// private static final char[] CODE_81 = new char[]{'8', '1'}; //81
	private static final char[] CODE_00 = new char[] { '0', '0' }; // OO
	private static final char[] CODE_01 = new char[] { '0', '1' }; // OO
	// private static final char[] CODE_47 = new char[]{'4', '7'}; //47
	private static final char[] CODE_16 = new char[] { '1', '6' }; // 16
	 private static final char[] CODE_68 = new char[]{'6', '8'}; //68
	private static final char[] CODE_7716 = new char[] { '7', '7', '1', '6' }; // 7716
	private static final char[] CODE_7A16_UP = new char[] { '7', 'A', '1', '6' }; // 7A16
	private static final char[] CODE_7A16_DOWN = new char[] { '7', 'a', '1', '6' }; // 7A16

	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private TemperatureMeasureHistoryService measureHistoryService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private HitchEventManager hitchEventManager;
	@Autowired
	private GPRSModuleService gprsService;
	@Autowired
	private TemperatureModuleService temModuleService;
	@Autowired
	private TemperatureCtxStore ctxStore;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;	
	@Autowired
	private ModuleHitchEventService moduleHitchEventService;

	private static final Logger logger = LoggerFactory.getLogger(TemperatureDataReceiver.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SwitchGPRS gprs = new SwitchGPRS();// 添加ctx到Store中
		gprs.setCtx(ctx);
		if (ctxStore.get(ctx) == null) {
			ctxStore.add(gprs);
			// 建立ChannelHandlerContext与GPRSModule的联系，建立通道时未获知GPRSModule的消息
//			TemperatureCtxStore.addGPRS(ctx, null);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		AttributeKey<String> key = AttributeKey.valueOf("GPRSAddress");
		Attribute<String> attr = ctx.attr(key);
		String gprsAddress = attr.get();
		logger.info("GPRS" + gprsAddress + "设备失去联络");
		SwitchGPRS gprs = ctxStore.get(ctx);
		if (gprs != null) {
			// 从CtxStore中取出当前ChannelHandlerContext
			ctxStore.remove(ctx);
			GPRSCtxStore.removeGPRS(gprsAddress);
			if (gprs.getId() != null) {
				//TODO ???有意义吗现在
//				TemperatureModule module = temModuleService.selectByPrimaryKey(gprs.getId());
//				TemperatureDevice device = deviceService.selectByPrimaryKey(gprs.getId());
//				device.setOnlineTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
//				deviceService.updateByPrimaryKey(device);
			}
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String rowMsg = (String) msg;
		logger.info("接收到的报文： " + rowMsg);
		char[] data = CharUtils.removeSpace(rowMsg.toCharArray());
		// 验证报文合法性，以及做一些注册的工作
		if (check(ctx, data)) {
			handleIdenCode(ctx, data);
		} else {
			logger.info("验证失败：" + rowMsg);
		}
	}

	/**
	 * 报文合法性验证
	 * @param ctx
	 * @param data
	 * @return
	 */
	private boolean check(ChannelHandlerContext ctx, char[] data) {
		
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
				
				AttributeKey<String> key = AttributeKey.valueOf("GPRSAddress");
				Attribute<String> attr = ctx.attr(key);
				attr.set(gprsAddress);
				//更新TemperatureCtxStore的GPRSList
				TemperatureCtxStore.addGPRS(gprsAddress);
				if (CharUtils.equals(data, 6, 8, CODE_01)) {
					//GPRS模块登录
					logger.info(gprsAddress + " GPRS模块登录成功");
				} else if (CharUtils.equals(data, 6, 8, CODE_03)) {
					logger.info(gprsAddress + " GPRS模块在线");
				}
			} else {
				//如果网站上没注册gprs，否决此报文
				logger.info("未注册GPRS模块地址" + gprsAddress);
				return false;
			}
		}
		
		//若GPRS模块注册不成功，报文不通过
		AttributeKey<String> key = AttributeKey.valueOf("GPRSAddress");
		Attribute<String> attr = ctx.attr(key);
		if ((null == attr.get() || "".equals(attr.get()))
				|| !GPRSCtxStore.isGPRSAlive(gprsAddress)) {
			return false;
		}
		/*
		 * TODO 做时间戳 + 设备地址 + 校验和的验证工作，如果不行就记录非法报文数量。 非法报文数量超过一定程度后封锁该设备地址。
		 * 一旦一天积聚量超过一定程度over。 一旦某段时间内非法报文数量过多over。
		 */
		return true;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private void handleIdenCode(ChannelHandlerContext ctx, char[] data) {
		//GPRS登录包、心跳包
		if (data.length == 20 && CharUtils.startWith(data, CODE_00)) {
			return ;
		}

		//68开头16结尾的报文
		if (!(CharUtils.startWith(data, EB_UP) || CharUtils.startWith(data, EB_DOWN))
				&& CharUtils.endsWith(data, CODE_16) && CharUtils.startWith(data, CODE_68)) {
			AttributeKey<Integer> key = AttributeKey.valueOf("isRegisted");
			Attribute<Integer> attr = ctx.attr(key);
			if (null == attr.get()) {
				getOnlineAddress(ctx, data);
				if (null != ctxStore.get(ctx).getId()) {
					attr.set(1);
				}
			}
		}

		//报文少于登录包和心跳包，返回
		if (data.length < 20) {
			logger.info("接收到的非法数据--------------------" + String.valueOf(data));
			return;
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
	}

	/**
	 * 获取当前连接的设备地址
	 * 
	 * @param ctx
	 * @param data
	 */
	private void getOnlineAddress(ChannelHandlerContext ctx, char[] data) {

		SwitchGPRS gprs = ctxStore.get(ctx);
		/*
		 * 当注册的温度开关的地址不为空，说明已经注册过了，不再进行相关操作
		 */
		if (gprs != null && gprs.getAddress() != null) {
			ctx.channel().writeAndFlush(data);
			return;
		}
		String address = CharUtils.newString(data, 10, 18).intern();
		gprs.setAddress(address);

		address = TemperatureDeviceCommandUtil.reverseString(address);

		if (gprs != null) {
			/*
			 * 根据反转后的地址查询得到TemperatureDevice的集合
			 */
			List<TemperatureModule> list = temModuleService
					.selectByParameters(MyBatisMapUtil.warp("device_number", Integer.parseInt(address, 16)));

			if (list != null && list.size() != 0) {
				TemperatureModule module = list.get(0);
				String id = module.getId();
				gprs.setId(id);

				if (ctxStore.get(id) != null) {
					ctxStore.remove(id);
					ctxStore.add(gprs);
				}
			} else {
				logger.warn("当前设备未进行注册");
			}
		}
	}

	/**
	 * 处理遥测
	 * 
	 * @param ctx
	 * @param data
	 */
	public void handleRemoteMeasure(ChannelHandlerContext ctx, char[] data) {
		String address = CharUtils.newString(data, 10, 18); // 地址域

		// 第一次总召对时
		AttributeKey<Integer> key = AttributeKey.valueOf("FirstTotalCall");
		Attribute<Integer> attr = ctx.attr(key);
		if (null == attr.get()) {
			attr.set(1);
			// 发送对时命令，第一次总召的时间其实是不对的
			String correctTime = doMatchTime(CharUtils.newString(data, 10, 18));
			ctx.writeAndFlush(correctTime);
		}

		String deviceId = ctxStore.getIdbyAddress(address);
		if (null == deviceId || "".equals(deviceId)) {
			return;
		}

		if (CharUtils.equals(data, 20, 22, CODE_01)) {
			// 处理遥测变化
			logger.info("解析温度遥测变位-------" + String.valueOf(data));
			String signalAddress = CharUtils.newString(data, 34, 38);
			String value = CharUtils.newString(data, 38, 38 + 4);
			int tag = changeSignalAddress(signalAddress);
			doSaveMeasure(value, deviceId, tag);
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
			doSaveMeasure(buffer, deviceId);
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
			return ;
		}
		String deviceId = ctxStore.get(ctx).getId();
		int index = 26 * BYTE;
		for (int i = index; i < data.length - 2 * BYTE; i = i + BYTE) {
			if (i >= index + 39 * BYTE) {
				if (data[i + 1] == '1') {
					int tag = (i - index - 39 * BYTE) / 2 + 1;
					TemperatureModule device = temModuleService.selectByPrimaryKey(deviceId);
					List<DataMonitorSubmodule> submoduleList = submoduleService.selectByParameters(MyBatisMapUtil.warp("module_id", device.getId()));
					if (null == submoduleList || 1 != submoduleList.size()) {
						logger.info("该温度子模块没有注册");
						return ;
					}
					//以后要改一下这里 TODO
					DataMonitorSubmodule submodule = submoduleList.get(0);
					ModuleHitchEvent moduleHitch = new ModuleHitchEvent();
					moduleHitch.setId(UUIDUtil.getUUID());
					moduleHitch.setGroupId(device.getGroupId());
					moduleHitch.setHitchReason(HitchConst.HITCH_ELECTRICITY_LACK);
					moduleHitch.setHitchTime(new Date());
					moduleHitch.setModuleId(device.getId());
					moduleHitch.setMonitorId(submodule.getDataMonitorId());
					moduleHitch.setType(310);
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
	private void doSaveMeasure(String value, String deviceId, int tag) {
		List<TemperatureSensor> sensorList = sensorService
				.selectByParameters(MyBatisMapUtil.warp("device_id", deviceId));
		isSensorExist(sensorList, tag, deviceId);
		measureHistoryService.insert(new TemperatureMeasureHistory(UUIDUtil.getUUID(), deviceId,
				new Timestamp(System.currentTimeMillis()), tag, Integer.parseInt(value, 16) * 10 + ""));
		doSaveMeasure0(value, deviceId, tag);
	}

	/**
	 * 保存全遥测
	 * 
	 * @param value
	 * @param deviceId
	 */
	private void doSaveMeasure(String[] value, String deviceId) {
		List<TemperatureSensor> sensorList = sensorService
				.selectByParameters(MyBatisMapUtil.warp("device_id", deviceId));

		for (int i = 1; i <= value.length; i++) {
			if (value[i - 1].equals("0000")) {
				continue;
			}
			//这函数好难看，待更改 TODO
			isSensorExist(sensorList, i, deviceId);
			measureHistoryService.insert(new TemperatureMeasureHistory(UUIDUtil.getUUID(), deviceId,
					new Timestamp(System.currentTimeMillis()), i, Integer.parseInt(value[i - 1], 16) * 10 + ""));
			doSaveMeasure0(value[i - 1], deviceId, i);
			// 插入报警数据
			if (TemperatureCtxStore.isAboveBound(deviceId, Double.valueOf("" + Integer.parseInt(value[i - 1], 16)) / 10)) {
				TemperatureModule device = temModuleService.selectByPrimaryKey(deviceId);
				List<DataMonitorSubmodule> submoduleList = submoduleService.selectByParameters(MyBatisMapUtil.warp("module_id", device.getId()));
				if (null == submoduleList || 1 != submoduleList.size()) {
					logger.info("该温度子模块没有注册");
					return ;
				}
				DataMonitorSubmodule submodule = submoduleList.get(0);
				ModuleHitchEvent moduleHitch = new ModuleHitchEvent();
				moduleHitch.setId(UUIDUtil.getUUID());
				moduleHitch.setGroupId(device.getGroupId());
				moduleHitch.setHitchReason(HitchConst.HITCH_OVER_TEMPERATURE);
				moduleHitch.setHitchTime(new Date());
				moduleHitch.setModuleId(device.getId());
				moduleHitch.setMonitorId(submodule.getDataMonitorId());
				moduleHitch.setType(301);
				moduleHitchEventService.updateByPrimaryKey(moduleHitch);
				
				// TODO 其实报警事事件应该是时间戳中的时间
				TemperatureMeasureHitchEvent event = new TemperatureMeasureHitchEvent();
				event.setId(UUIDUtil.getUUID());
				event.setMonitorId(submodule.getDataMonitorId());
				event.setValue(new BigDecimal(Double.valueOf("" + Integer.parseInt(value[i - 1], 16)) / 10));
				event.setTag(i);
				event.setGroupId(device.getGroupId());
				event.setMaxHitchValue(device.getMaxHitchValue());
				event.setMinHitchValue(device.getMinHitchValue());
				event.setType(301);
				event.setHitchId(moduleHitch.getId());
				// 把报警事件塞进线程池
				hitchEventManager.addHitchEvent(event);
			}
		}
	}

	/**
	 * 确认传感器受否存在，否则新增传感器实体
	 * 
	 * @param sensorList
	 * @param tag
	 * @param deviceId
	 */
	public void isSensorExist(List<TemperatureSensor> sensorList, int tag, String deviceId) {
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
	public void doSaveMeasure0(String value, String deviceId, int tag) {

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
	 * 得到对时数据
	 * 示范 00211e02180311
	 * 68161668F4123456786701060112345678000000211e02180311f816  地址为12345678
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
	
	//TODO
	private boolean checkJIAOYANHE(char[] jiaoyanhe, char[] data) {
		return true;
	}
	
	//TODO
	private boolean checkTime(char[] time) {
		return true;
	}

}
