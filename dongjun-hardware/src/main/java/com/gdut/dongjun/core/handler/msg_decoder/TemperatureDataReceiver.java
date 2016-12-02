package com.gdut.dongjun.core.handler.msg_decoder;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.TemperatureMeasureService;
import com.gdut.dongjun.service.TemperatureSensorService;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;
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
public class TemperatureDataReceiver extends ChannelInboundHandlerAdapter {

	@Autowired
	private TemperatureDeviceService deviceService;
	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private TemperatureMeasureHistoryService measureHistoryService;
	@Autowired
	private TemperatureSensorService sensorService;

	private static final Logger logger = LoggerFactory.getLogger(TemperatureDataReceiver.class);

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
		CtxStore.printCtxStore();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String data = ((String) msg).replaceAll(" ", "");
		logger.info("接收到的报文： " + data);
		handleIdenCode(ctx, data);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	public void handleIdenCode(ChannelHandlerContext ctx, String data) {
		if (data.startsWith("00") && data.substring(6, 8).equals("01")) {
			String gprsNumber = data.substring(12, 20);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= 6; i+=2) {
				String address = gprsNumber.substring(i, i+2);
				sb.append((char)Integer.parseInt(address, 16));
			}
			logger.info(sb.toString() + " GPRS模块登录成功");
			return ;
		}
		if (data.startsWith("00") && data.substring(6, 8).equals("03")) {
			String gprsNumber = data.substring(12, 20);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= 6; i+=2) {
				String address = gprsNumber.substring(i, i+2);
				sb.append((char)Integer.parseInt(address, 16));
			}
			logger.info(sb.toString() + " GPRS模块在线");
			return ;
		}
//		if (data.startsWith("1007")) {
//			logger.info("终端开始连接");
//			return;
//		}
		if (data.length() < 20) {
			logger.info("undefine message received!");
			logger.error("接收到的非法数据--------------------" + data);
			return ;
		}
		String controlCode = data.substring(18, 20);
		if (data.startsWith("EB90EB90EB90") || data.startsWith("eb90eb90eb90")) {
			/*
			 * 读通信地址并将地址反转,确认连接
			 */
			logger.info("主站接收到连接");
			getOnlineAddress(ctx, data);
			ctx.channel().writeAndFlush("EB90EB90EB90" + data.substring(12, 16) + "16");
		}

		else if (controlCode.equals("64") && data.endsWith("7716")) {
			/*
			 * 终端发送总招激活确认
			 */
			logger.info("终端确认总召激活");
		}

		else if (controlCode.equals("64") && (data.endsWith("7A16") || data.endsWith("7a16"))) {
			/*
			 * 终端确认总召结束
			 */
			logger.info("终端确认总召结束");
		}

		else if (controlCode.equals("09")) {
			/*
			 * 终端发送总招激活确认
			 */
			logger.info("接受来自终端的遥测数据");
			// 遥测TODO 还有遥测变位事件，需要看变结构限定词来确定处理方法。遥测变位事件不需要主站确认
			handleRemoteMeasure(ctx, data);
		}

		else if (controlCode.equals("01")) {
			/*
			 * 遥信变化值，遥信总召所有值获取
			 */
			logger.info("接受来自终端的单点遥信数据");
			handleRemoteSignal(ctx, data);
		}

		else if (controlCode.equals("03")) {
			/*
			 * 遥信变位信息（就像报告它接下来要发送遥信变位事件）
			 */
			logger.info("接受来自终端的双点遥信数据");
			// 双点遥信TODO 主站需要发送确认报文 然后接受遥信变位事件
			handleRemoteSignal(ctx, data);
		}

		else if (controlCode.equals("1F") || controlCode.endsWith("1f")) {
			/*
			 * 遥信变位事件
			 */
			logger.info("遥信变位事件");
			// 主站需要发送确认报文
			handleRemoteSignalChange(ctx, data);
		} else {
			logger.info("undefine message received!");
			logger.error("接收到的非法数据--------------------" + data);
		}
	}

	/**
	 * 获取当前连接的设备地址
	 * 
	 * @param ctx
	 * @param data
	 */
	private void getOnlineAddress(ChannelHandlerContext ctx, String data) {
		
		SwitchGPRS gprs = CtxStore.get(ctx);
		/*
		 * 当注册的温度开关的地址不为空，说明已经注册过了，不再进行相关操作
		 */
		if(gprs != null && gprs.getAddress() != null) {
			ctx.channel().writeAndFlush(data);
			return;
		}
		String address = data.substring(10, 18);
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
				
//				TemperatureServer.totalCall(id);  //新版控制器自动上传总召报文，不用我们再发送总召报文
				if(CtxStore.get(id) != null) {
					CtxStore.remove(id);
					CtxStore.add(gprs);
				}
			} else {
				logger.info("this device is not registered!!");
			}
		}
	}

	/**
	 * 处理遥测
	 * 
	 * @param ctx
	 * @param data
	 */
	public void handleRemoteMeasure(ChannelHandlerContext ctx, String data) {
		logger.info("温度遥测-------" + data);
		String variable = data.substring(20, 22);
		logger.info("测归一化值-------" + variable);
		String address = data.substring(10, 18);  //地址域
		//因为现在温度协议是直接总召报文上来，所以要在全遥测的时候检测ctx是否注册过
		 AttributeKey<Integer> key = AttributeKey.valueOf("isRegisted");
		 Attribute<Integer> attr = ctx.attr(key);
		 if (null == attr.get()) {
			 logger.info("设备上线！！！！！！！！！");
				getOnlineAddress(ctx, data);
				attr.set(1);
		 }
		 
		String deviceId = CtxStore.getIdbyAddress(address);
		if (null == deviceId || "".equals(deviceId)) {
			return ;
		}
//		String deviceId = "0100";
		if (variable.equals("01")) {
			/**
			 * 处理遥测变化
			 */
			logger.info("解析遥测变位-------" + data);
			String signalAddress = data.substring(34, 38);
			String value = data.substring(38, 38+6);
			value = value.substring(0, 4);
			int tag = changeSignalAddress(signalAddress);
			doSaveMeasure(value, deviceId, tag);
		} else {
			/**
			 * 处理全遥测
			 */
			logger.info("解析全遥测-------" + data);
			String dataList = data.substring(52, 16*6 + 52);  //信息元素集
			String[] buffer = new String[dataList.length() / 6];
			for (int i = 0; i < dataList.length(); i += 6) {
				String temp = dataList.substring(i, i + 6);
				buffer[i/6] = temp.substring(0, 4);
			}
			doSaveMeasure(buffer, deviceId);
			ctx.channel().writeAndFlush("eb90eb90eb90" + data.substring(10, 18) + "16");	  //全遥测确认报文，提示控制器发送全遥信
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

	// private void test(ChannelHandlerContext ctx, String data) {
	// strBuffer.append(data);
	// if (data.endsWith("16")) {
	// logger.info("接收到完整数据了" + strBuffer.toString());
	// }
	// }

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
				// getMessageAddress(data.substring(i + 4, i + 8),
				// data.substring(22, 26),
				// data.substring(i + 8, i + 14));
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
//		int address;
//		switch (signalAddress) {
//		case "4001":
//			address = 1;
//			break;
//		case "4002":
//			address = 2;
//			break;
//		case "4003":
//			address = 3;
//			break;
//		case "4004":
//			address = 4;
//			break;
//		case "4005":
//			address = 5;
//			break;
//		case "4006":
//			address = 6;
//			break;
//		case "4007":
//			address = 7;
//			break;
//		case "4008":
//			address = 8;
//			break;
//		case "4009":
//			address = 9;
//			break;
//		case "400a":
//		case "400A":
//			address = 10;
//			break;
//		case "400b":
//		case "400B":
//			address = 11;
//			break;
//		case "400c":
//		case "400C":
//			address = 12;
//			break;
//		case "400d":
//		case "400D":
//			address = 13;
//			break;
//		case "400e":
//		case "400E":
//			address = 14;
//			break;
//		case "400f":
//		case "400F":
//			address = 15;
//			break;
//		case "4010":
//			address = 16;
//			break;
//		default:
//			return null;
//		}
//		return address;
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
			if (value.equals("0000"))
				continue;
			isSensorExist(sensorList, i, deviceId);
			measureHistoryService.insert(
					new TemperatureMeasureHistory(UUIDUtil.getUUID(), deviceId, new Timestamp(System.currentTimeMillis()), i, Integer.parseInt(value[i-1], 16)*10 + ""));
			doSaveMeasure0(value[i-1], deviceId, i);
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
}
