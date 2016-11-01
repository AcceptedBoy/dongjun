package com.gdut.dongjun.core.handler.msg_decoder;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.server.impl.TemperatureServer;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.domain.po.TemperatureMeasure;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;
import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.TemperatureHitchEventService;
import com.gdut.dongjun.service.TemperatureMeasureHistoryService;
import com.gdut.dongjun.service.TemperatureMeasureService;
import com.gdut.dongjun.service.TemperatureSignalService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Service
@Sharable
public class TemperatureDataReceiver extends ChannelInboundHandlerAdapter {

	@Autowired
	private TemperatureDeviceService deviceService;
	@Autowired
	private TemperatureMeasureService measureService;
	@Autowired
	private TemperatureSignalService signalService;
	@Autowired
	private TemperatureMeasureHistoryService measureHistoryService;

	private static final Logger logger = LoggerFactory.getLogger(TemperatureDataReceiver.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SwitchGPRS gprs = new SwitchGPRS();// 添加ctx到Store中
		gprs.setCtx(ctx);
		if (CtxStore.get(ctx) == null) {
			CtxStore.add(gprs);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		CtxStore.remove(ctx);// 从Store中移除这个context
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
		String controlCode = data.substring(14, 16);
		if (data.startsWith("EB90EB90EB90") || data.startsWith("eb90eb90eb90")) {
			/*
			 * 读通信地址并将地址反转,确认连接
			 */
			logger.info("主站接收到连接");
		}

		else if (data.startsWith("1007")) {
			/*
			 * 终端开始连接
			 */
			logger.info("终端开始连接");
			getOnlineAddress(ctx, data);
			return;
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
		 * 当注册的高压开关的地址不为空，说明已经注册过了，不再进行相关操作
		 */
		if (gprs != null && gprs.getAddress() != null) {
			ctx.channel().writeAndFlush(data);
			return;
		}
		String address = data.substring(12, 16);
		gprs.setAddress(address);

		if (gprs != null) {
			List<TemperatureDevice> devices = deviceService
					.selectByParameters(MyBatisMapUtil.warp("address", Integer.parseInt(address, 16)));
			TemperatureDevice device = null;

			if (devices != null && devices.size() != 0) {
				device = devices.get(0);
				String id = device.getId();
				gprs.setId(id);
				if (CtxStore.get(id) != null) {
					CtxStore.remove(id);
					CtxStore.add(gprs);
				}
			}
		} else
			logger.info("this device is not registered!");
		TemperatureServer.totalCall(gprs.getId()); // 连接成功后发送一次总召
		// ctx.channel().writeAndFlush(data); //返回链接确认的报文
	}

	/**
	 * 处理遥测
	 * 
	 * @param ctx
	 * @param data
	 */
	public void handleRemoteMeasure(ChannelHandlerContext ctx, String data) {
		logger.info("温度遥测-------" + data);
		String variable = data.substring(16, 18);
		logger.info("测归一化值-------" + variable);
		String signalAddress = data.substring(26, 30);// 信息对象地址
		String dataList = data.substring(30, data.length() - 4);
		String address = TemperatureDeviceCommandUtil.reverseString(data.substring(10, 14));
		if (variable.equals("92")) {
			/**
			 * 处理全遥测
			 */
			logger.info("解析全遥测-------" + data);
			String[] buffer = new String[dataList.length() / 6];
			for (int i = 0; i < dataList.length(); i += 6) {
				String temp = dataList.substring(i, i + 6);
				buffer[i / 6] = TemperatureDeviceCommandUtil.reverseString(temp);
			}
			String value = parseInt(buffer);
			TemperatureMeasure measure = new TemperatureMeasure(address, new Timestamp(System.currentTimeMillis()), 0,
					value);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("address", address);
			String deviceId = CtxStore.getIdbyAddress(address);
			map.put("deviceId", deviceId);
			measure.setId(deviceId);
			List<TemperatureMeasure> measureList = measureService.selectByParameters(MyBatisMapUtil.warp(map));
			if (measureList.size() == 0 && measureList == null)
				measureService.insert(measure);
			else
				measureService.updateByPrimaryKey(measure);
			measureHistoryService.insert(changeToMeasureHistory(measure));

		} else {
			/**
			 * 处理遥测变化
			 */
			logger.info("解析遥测变位-------" + data);
			String value = TemperatureDeviceCommandUtil.reverseString(dataList);
			String[] newList = new String[1];
			newList[0] = value;
			value = parseInt(newList);
			TemperatureMeasure measure = new TemperatureMeasure(address, new Timestamp(System.currentTimeMillis()),
					changeSignalAddress(signalAddress), value);
			measureService.insert(measure);
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
		int address;
		switch (signalAddress) {
		case "4001":
			address = 1;
			break;
		case "4002":
			address = 2;
			break;
		case "4003":
			address = 3;
			break;
		case "4004":
			address = 4;
			break;
		case "4005":
			address = 5;
			break;
		case "4006":
			address = 6;
			break;
		case "4007":
			address = 7;
			break;
		case "4008":
			address = 8;
			break;
		case "4009":
			address = 9;
			break;
		case "400a":
		case "400A":
			address = 10;
			break;
		case "400b":
		case "400B":
			address = 11;
			break;
		case "400c":
		case "400C":
			address = 12;
			break;
		case "400d":
		case "400D":
			address = 13;
			break;
		case "400e":
		case "400E":
			address = 14;
			break;
		case "400f":
		case "400F":
			address = 15;
			break;
		case "4010":
			address = 16;
			break;
		default:
			return null;
		}
		return address;
	}

	/**
	 * 转换TemperatureMeasure为History
	 * @param measure
	 * @return
	 */
	public TemperatureMeasureHistory changeToMeasureHistory(TemperatureMeasure measure) {
		TemperatureMeasureHistory history = new TemperatureMeasureHistory();
		history.setDate(measure.getDate());
		history.setDeviceId(measure.getDeviceId());
		history.setTag(measure.getTag());
		history.setValue(measure.getValue());
		history.setId(measure.getId());
		return history;
	}

	/**
	 * 把遥信的开关值从字符串变为数组,为了返回给前端的
	 */
	private void string_Array(String dataList) {
		String[] buffer = new String[dataList.length() / 2];
	}

}
