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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.core.handler.thread.HitchEventManager;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.enums.LogConst;
import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.service.TemperatureMeasureService;
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

@Service
@Sharable
public class TemperatureDataReceiver extends ChannelInboundHandlerAdapter implements ApplicationContextAware {
	
	private static final char[] EB_UP = new char[]{'E', 'B', '9', '0'}; //EB90
	private static final char[] EB_DOWN = new char[]{'e', 'b', '9', '0'}; //eb90
    private static final char[] CODE_64 = new char[]{'6', '4'}; //64
    private static final char[] CODE_03 = new char[]{'0', '3'}; //03
    private static final char[] CODE_2E = new char[]{'2', 'e'}; //2e
    private static final char[] CODE_1F_UP = new char[]{'1', 'F'}; //1F
    private static final char[] CODE_1F_DOWN = new char[]{'1', 'f'}; //1f
    private static final char[] CODE_09 = new char[]{'0', '9'}; //09
    private static final char[] CODE_81 = new char[]{'8', '1'}; //81
    private static final char[] CODE_00 = new char[]{'0', '0'}; //OO
    private static final char[] CODE_01 = new char[]{'0', '1'}; //OO
    private static final char[] CODE_47 = new char[]{'4', '7'}; //47
    private static final char[] CODE_16 = new char[]{'1', '6'}; //16
    private static final char[] CODE_68 = new char[]{'6', '8'}; //68
    private static final char[] CODE_7716 = new char[]{'7', '7', '1', '6'}; //7716
    private static final char[] CODE_7A16_UP = new char[]{'7', 'A', '1', '6'}; //7A16
    private static final char[] CODE_7A16_DOWN = new char[]{'7', 'a', '1', '6'}; //7A16

	@Autowired
	private TemperatureDeviceService deviceService;
	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private TemperatureMeasureHistoryService measureHistoryService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private HitchEventManager hitchEventManager;

	private static final Logger logger = LoggerFactory.getLogger(TemperatureDataReceiver.class);
	
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SwitchGPRS gprs = new SwitchGPRS();// 添加ctx到Store中
		gprs.setCtx(ctx);
		if (CtxStore.get(ctx) == null) {
			CtxStore.add(gprs);
		}
//		ctx.channel().writeAndFlush(new TemperatureDeviceCommandUtil().getConnection()); //发送确认报文，新版gprs不用，新版gprs直接上传总召
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SwitchGPRS gprs = CtxStore.get(ctx);
		if (gprs != null) {
			CtxStore.remove(ctx);// 从Store中移除这个context
			if(gprs.getId() != null) {
				TemperatureDevice device = deviceService.selectByPrimaryKey(gprs.getId());
				device.setOnlineTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				deviceService.updateByPrimaryKey(device);
			}
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String rowMsg = (String) msg;
		logger.info("接收到的报文： " + rowMsg);
		char[] data = CharUtils.removeSpace(rowMsg.toCharArray());
		//验证报文合法性，以及做一些注册的工作
		if (check(ctx, data)) {
			handleIdenCode(ctx, data);
		}
//		测试用
//		testHandleCode(ctx, data);
	}
	
	private boolean check(ChannelHandlerContext ctx, char[] data) {
		//登录和心跳包
		if(CharUtils.startWith(data, CODE_00) && (CharUtils.equals(data, 6, 8, CODE_01) || CharUtils.equals(data, 6, 8, CODE_03))) {
			String gprsNumber = CharUtils.newString(data, 12, 20);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= 6; i+=2) {
				String address = gprsNumber.substring(i, i+2);
				sb.append((char)Integer.parseInt(address, 16));
			}
			if (CharUtils.equals(data, 6, 8, CODE_01)) {
				logger.info(sb.toString() + " GPRS模块登录成功");
				return true;
			} else {
				logger.info(sb.toString() + " GPRS模块在线");
				return true;
			}
		}
		/*
		 * TODO 做时间戳 + 设备地址 + 校验和的验证工作，如果不行就记录非法报文数量。
		 * 非法报文数量超过一定程度后封锁该设备地址。
		 * 一旦一天积聚量超过一定程度over。
		 * 一旦某段时间内非法报文数量过多over。
		 */
		return true;
	}
	
//	private void testHandleCode(ChannelHandlerContext ctx, char[] data) {
//		String deviceId = "1";
//		Double value = Double.valueOf("666.6");
//		if (TemperatureCtxStore.isAboveBound(deviceId, value/10)) {
//			TemperatureDevice device = deviceService.selectByPrimaryKey(deviceId);
//			TemperatureMeasureHitchEvent event = new TemperatureMeasureHitchEvent(
//					UUIDUtil.getUUID(), deviceId, new BigDecimal(value / 10), 6, "监测温度超过所设阈值", 
//					TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), device.getGroupId(), 
//					new Date(), new Date(), device.getMaxHitchValue(), device.getMinHitchValue()
//					);
//			
//			//把报警事件塞进线程池
//			hitchEventManager.addHitchEvent(event);
//		}
//		ctx.channel().writeAndFlush("data receive success!");
//	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private void handleIdenCode(ChannelHandlerContext ctx, char[] data) {
		
		if (!(CharUtils.startWith(data, EB_UP) || CharUtils.startWith(data, EB_DOWN)) && CharUtils.endsWith(data, CODE_16)) {
			AttributeKey<Integer> key = AttributeKey.valueOf("isRegisted");
			Attribute<Integer> attr = ctx.attr(key);
			if (null == attr.get()) {
				getOnlineAddress(ctx, data);
				if (null != CtxStore.get(ctx).getId()) {
					attr.set(1);
				}
			}
		}

		if (data.length < 20) {
			logger.info("接收到的非法数据--------------------" + data);
			return ;
		}
		char[] controlCode = ArrayUtils.subarray(data, 18, 20);
		System.out.println(controlCode);
		if (CharUtils.startWith(data, EB_UP) || CharUtils.startWith(data, EB_DOWN)) {
			/*
			 * 读通信地址并将地址反转,确认连接
			 */
			logger.info("主站接收到连接");
			getOnlineAddress(ctx, data);
			//需要返回eb90报文结束登录流程
			StringBuilder sb = new StringBuilder();
			sb.append("EB90EB90EB90").append(CharUtils.newString(data, 12, 16)).append("16");
			ctx.channel().writeAndFlush(sb.toString());
		}

		else if (CharUtils.equals(controlCode, CODE_64) || CharUtils.endsWith(data, CODE_7716)) {
			/*
			 * 终端发送总招激活确认
			 */
			logger.info("终端确认总召激活");
		}

		else if (CharUtils.equals(controlCode, CODE_64) && (CharUtils.endsWith(controlCode, CODE_7A16_UP) || CharUtils.endsWith(controlCode, CODE_7A16_DOWN))) {
			/*
			 * 终端确认总召结束
			 */
			logger.info("终端确认总召结束");
		}

		else if (CharUtils.equals(controlCode, CODE_09)) {
			/*
			 * 终端发送总招激活确认
			 */
//			logger.info("接受来自终端的遥测数据");
			// 遥测TODO 还有遥测变位事件，需要看变结构限定词来确定处理方法。遥测变位事件不需要主站确认
			handleRemoteMeasure(ctx, data);
		}

		else if (CharUtils.equals(controlCode, CODE_01)) {
			/*
			 * 遥信变化值，遥信总召所有值获取
			 */
//			logger.info("接受来自终端的单点遥信数据");
//			handleRemoteSignal(ctx, data);
		}

		else if (CharUtils.equals(controlCode, CODE_03)) {
			/*
			 * 遥信变位信息（就像报告它接下来要发送遥信变位事件）
			 */
//			logger.info("接受来自终端的双点遥信数据");
			// 双点遥信TODO 主站需要发送确认报文 然后接受遥信变位事件
//			handleRemoteSignal(ctx, data);
		}

		else if (CharUtils.equals(controlCode, CODE_1F_UP) || CharUtils.equals(controlCode, CODE_1F_DOWN)) {
			/*
			 * 遥信变位事件
			 */
//			logger.info("遥信变位事件");
			// 主站需要发送确认报文
//			handleRemoteSignalChange(ctx, data);
		} else {
			logger.warn("接收到的非法数据--------------------" + data);
		}
		
	}

	/**
	 * 获取当前连接的设备地址
	 * 
	 * @param ctx
	 * @param data
	 */
	private void getOnlineAddress(ChannelHandlerContext ctx, char[] data) {
		
		SwitchGPRS gprs = CtxStore.get(ctx);
		/*
		 * 当注册的温度开关的地址不为空，说明已经注册过了，不再进行相关操作
		 */
		if(gprs != null && gprs.getAddress() != null) {
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
			List<TemperatureDevice> list = deviceService
					.selectByParameters(MyBatisMapUtil.warp("device_number", Integer.parseInt(address, 16)));
			
			if (list != null && list.size() != 0) {

				TemperatureDevice device = list.get(0);
				String id = device.getId();
				gprs.setId(id);
				
				if(CtxStore.get(id) != null) {
					CtxStore.remove(id);
					CtxStore.add(gprs);
				}
			} else {
				logger.warn("this device is not registered!!");
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
		String address = CharUtils.newString(data, 10, 18); //地址域
	
		//第一次总召对时
		 AttributeKey<Integer> key = AttributeKey.valueOf("FirstTotalCall");
		 Attribute<Integer> attr = ctx.attr(key);
		 if (null == attr.get()) {
				getOnlineAddress(ctx, data);
				attr.set(1);
				//发送对时命令，第一次总召的时间其实是不对的
				ctx.writeAndFlush(doMatchTime(CharUtils.newString(data, 10, 18)));
		 }
		 
		String deviceId = CtxStore.getIdbyAddress(address);
		if (null == deviceId || "".equals(deviceId)) {
			return ;
		}
		if (CharUtils.equals(data, 20, 22, CODE_01)) {
			//处理遥测变化
			logger.info("解析温度遥测变位-------" + data);
			String signalAddress = CharUtils.newString(data, 34, 38);
			String value = CharUtils.newString(data, 38, 38 + 4);
			int tag = changeSignalAddress(signalAddress);
			doSaveMeasure(value, deviceId, tag);
		} else {
			//处理全遥测
			logger.info("解析温度全遥测-------" + CharUtils.newString(data));
			String[] buffer = new String[16];
			for (int i = 0; i < 16; i++) {
				buffer[i] = CharUtils.newString(data, 52 + 6 * i, 52 + 6 * i + 4);
			}
			doSaveMeasure(buffer, deviceId);
			StringBuilder sb = new StringBuilder();
			sb.append("eb90eb90eb90" + CharUtils.newString(data, 10, 18) + "16");
			ctx.channel().writeAndFlush(sb.toString());	  //全遥测确认报文，提示控制器发送全遥信
		}
	}

	/**
	 * 处理全遥信 之后直接把所有值存到数据库中
	 * 
	 * @param ctx
	 * @param data
	 */
	public void handleRemoteSignal(ChannelHandlerContext ctx, String data) {
		String signalAddress = data.substring(26, 30);// 信息对象地址
		String dataList = data.substring(30, data.length() - 4); // 所有的遥信数据

	}

	/**
	 * 遥信变位
	 * 
	 * @param ctx
	 * @param data
	 */
	public void handleRemoteSignalChange(ChannelHandlerContext ctx, String data) {
		String signalAddress = data.substring(26, 30);// 信息对象地址
		String dataList = data.substring(30, 32); // 单个的遥信数据
	}

	public String parseInt(String[] data) {
		StringBuilder value = null;
		for (int i = 0; i < data.length; i++) {
			value.append(Integer.parseInt(data[i], 16) + " ");
		}
		return value.toString();
	}

	/**
	 * 遥测的值解析，包括总招时的遥测值和单次的温度变化遥测值
	 * 
	 * @param data
	 */
	private void changeMesurementInfo(String data) {

		/*
		 * 遥测变位信息事件（对于遥测，只有这一个报文）
		 */
		if (!data.substring(16, 18).equals("01")) {
			logger.info("温度变化-------" + data);
			// TODO
			/**
			 * 若data.length=40，测归一值
			 */
			logger.info("测归一值-------" + data);
			for (int i = 22; i + 14 < data.length(); i += 10) {
			}

		}
	}

	/**
	 * 所有遥信值的获取
	 * 
	 * @param data
	 */
	private void readAllSignal(String data) {
		// TODO
	}

	/*
	 * 确实遥信
	 */
	private void affirmSignal(ChannelHandlerContext ctx, String data) {
		String resu = new TemperatureDeviceCommandUtil(data.substring(10, 14)).confirmRemoteSignalChannge();
		logger.info("遥信变位确定---------" + resu);
		ctx.writeAndFlush(resu);
	}

	/**
	 * 遥信事件的处理，返回确认遥信
	 * 
	 * @param ctx
	 * @param data
	 */
	private void confirmSignalChangeInfo(ChannelHandlerContext ctx, String data) {

		for (int i = 26, j = Integer.valueOf(data.substring(16, 18)); j > 0; i += 6, --j) {
			// TODO 遥信值的处理
			// changeState(data.substring(22, 26), data.substring(i, i + 4),
			// data.substring(i + 4, i + 6));
		}
		// 返回确认遥信事件
		String resu = new TemperatureDeviceCommandUtil(data.substring(10, 14)).confirmRemoteSignalChangeEvent();
		logger.info("遥信变位事件确定---------" + resu);
		ctx.writeAndFlush(resu);
	}

	public Integer changeSignalAddress(String signalAddress) {
		return Integer.parseInt(signalAddress.substring(2, 4), 16);
	}

	/**
	 * 保存遥测变位
	 * @param deviceId
	 * @param value
	 */
	private void doSaveMeasure(String value, String deviceId, int tag) {
		measureHistoryService.insert(new TemperatureMeasureHistory(UUIDUtil.getUUID(), deviceId, new Timestamp(System.currentTimeMillis()), tag, Integer.parseInt(value, 16)*10 + ""));
		doSaveMeasure0(value, deviceId, tag);
	}
	
	public void doSaveMeasure(String[] value, String deviceId) {
		List<TemperatureSensor> sensorList = sensorService.selectByParameters(MyBatisMapUtil.warp("device_id", deviceId));
		
		for (int i = 1; i <= value.length; i++) {
			if (value[i-1].equals("0000")) {
				continue;
			}
			isSensorExist(sensorList, i, deviceId);
			measureHistoryService.insert(
					new TemperatureMeasureHistory(UUIDUtil.getUUID(), deviceId, new Timestamp(System.currentTimeMillis()), i, Integer.parseInt(value[i-1], 16)*10 + ""));
			doSaveMeasure0(value[i-1], deviceId, i);
			//插入报警数据
			if (TemperatureCtxStore.isAboveBound(deviceId, Double.valueOf(value[i - 1])/10)) {
				TemperatureDevice device = deviceService.selectByPrimaryKey(deviceId);
				//TODO 其实报警事事件应该是时间戳中的时间
				TemperatureMeasureHitchEvent event = new TemperatureMeasureHitchEvent(
						UUIDUtil.getUUID(), device.getId(), new BigDecimal(Double.valueOf(value[i - 1]) / 10),
						i, "监测温度超过所设阈值", TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"),
						device.getGroupId(), new Date(), new Date(), device.getMaxHitchValue(), device.getMinHitchValue()
						);
				//把报警事件塞进线程池
				hitchEventManager.addHitchEvent(event);
			}
		}
	}
	
	public void isSensorExist(List<TemperatureSensor> sensorList, int tag, String deviceId) {
		for (TemperatureSensor sensor : sensorList) {
			if (sensor.getTag() == tag) {
				return ;
			}
		}
		sensorService.insert(new TemperatureSensor(UUIDUtil.getUUID(), tag, deviceId, tag + "号传感器"));
	}
	
	public void doSaveMeasure0(String value, String deviceId, int tag) {

		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("device_id", deviceId);
		queryMap.put("tag", tag);
		List<TemperatureMeasure> list = measureService.selectByParameters(MyBatisMapUtil.warp(queryMap));
		if (list.size() == 0) {
			measureService.insert(new TemperatureMeasure(UUIDUtil.getUUID(), deviceId, new Timestamp(System.currentTimeMillis()), tag, Integer.parseInt(value, 16)*10 + ""));
		}
		else {
			TemperatureMeasure measure = list.get(0);
			measure.setDate(new Timestamp(System.currentTimeMillis()));
			measure.setValue("" + Integer.parseInt(value, 16)*10);
			measureService.updateByPrimaryKey(measure);
		}
	}
	
	public String doMatchTime(String address) {
		Calendar cal = Calendar.getInstance();
		String second = Integer.toHexString(cal.get(Calendar.SECOND));
		String minute = Integer.toHexString(cal.get(Calendar.MINUTE));
		String hour = Integer.toHexString(cal.get(Calendar.HOUR));
		String day = Integer.toHexString(cal.get(Calendar.DATE));
		String month = Integer.toHexString(cal.get(Calendar.MONTH) + 1);
		String year = cal.get(Calendar.YEAR) + "";
		year = year.substring(2, 4);
		year = Integer.toHexString(Integer.parseInt(year));
		String time = "00" + 
				((second.length() == 1) ? "0" + second : second) + 
				((minute.length() == 1) ? "0" + minute : minute) + 
				((hour.length() == 1) ? "0" + hour : hour) + 
				((day.length() == 1) ? "0" + day : day) + 
				((month.length() == 1) ? "0" + month : month) + 
				((year.length() == 1) ? "0" + year : year);
		TemperatureDeviceCommandUtil td = new TemperatureDeviceCommandUtil(address);
		return td.getTimeCheck(time);
	}


}
