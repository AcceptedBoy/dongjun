/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.gdut.dongjun.core.handler.msg_decoder;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.device_message_engine.impl.HighVoltageSwitchMessageEngine;
import com.gdut.dongjun.core.server.impl.HighVoltageServer;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.service.*;
import com.gdut.dongjun.util.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高压协议1.3版本 TODO 真的需要重构啊
 * 
 * @author Sherlock
 */
@Service
@Sharable
public class HighVoltageDataReceiver_V1_3 extends ChannelInboundHandlerAdapter {

	@Autowired
	private HighVoltageCurrentService currentService;
	@Autowired
	private HighVoltageVoltageService voltageService;
	@Autowired
	private HistoryHighVoltageCurrentService historyCurrentService;
	@Autowired
	private HistoryHighVoltageVoltageService historyVoltageService;
	@Autowired
	private HighVoltageHitchEventService hitchEventService;

	@Resource(name = "HighVoltageSwitchMessageEngine")
	private HighVoltageSwitchMessageEngine highVoltageEngine;

	private static final Logger logger = LoggerFactory.getLogger(HighVoltageDataReceiver_V1_3.class);

	@Autowired
	private HighVoltageSwitchService switchService;

	public HighVoltageDataReceiver_V1_3() {
		super();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		SwitchGPRS gprs = new SwitchGPRS();// 添加ctx到Store中
		gprs.setCtx(ctx);
		if (CtxStore.get(ctx) == null) {
			CtxStore.add(gprs);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		String data = ((String) msg).replace(" ", "");

		logger.info("接收到的报文： " + data);

		String hitchEventDesc = "控制回路";
		if (data.length() == 190) {
			hitchEventDesc = getHitchReason(data.substring(140, 160));
		}
		handleIdenCode(ctx, data, hitchEventDesc);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		SwitchGPRS gprs = CtxStore.get(ctx);
		if (gprs != null) {
			CtxStore.remove(ctx);// 从Store中移除这个context
			if (gprs.getId() != null) {
				HighVoltageSwitch hvSwitch = switchService.selectByPrimaryKey(gprs.getId());
				hvSwitch.setOnlineTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				switchService.updateByPrimaryKey(hvSwitch);
			}
		}
		CtxStore.printCtxStore();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	private void handleIdenCode(ChannelHandlerContext ctx, String data, String hitchEventDesc) {

		if (data.length() < 16) {
			return;
		}
		
		if (!(data.startsWith("EB90") || data.startsWith("eb90")) && data.startsWith("68")) {
			AttributeKey<Integer> key = AttributeKey.valueOf("isRegisted");
			Attribute<Integer> attr = ctx.attr(key);
			if (null == attr.get()) {
				int address = Integer.parseInt(data.substring(12, 16), 16);
				logger.info(address + "号设备上线");
				getOnlineAddress(ctx, data);
				attr.set(1);
			}
		}

		String infoIdenCode = data.substring(14, 16);

		/*
		 * 将接收到的客户端信息分类处理 读通信地址并将地址反转
		 */
		if ((data.startsWith("EB90") || data.startsWith("eb90"))) {

			getOnlineAddress(ctx, data);
		} else if (infoIdenCode.equals("64")) {

			/*
			 * 总召激活确定
			 */
			logger.info("总召激活已经确定：" + data.substring(10, 14));

			/*
			 * 为应对老机器中将总召，遥控，遥测值都加在同一份数据块而做的解析
			 */
			if (data.length() > 36) {
				getSwitchAllInfo(ctx, data, hitchEventDesc);
			}
		} else if (infoIdenCode.equals("03")) {

			/*
			 * 遥信变位初步确定
			 */
			confirmSignalInitialChange(ctx, data);
		} else if (infoIdenCode.equals("2e")) {

			/*
			 * 遥控预置接收并发送遥控
			 */
			whetherOperateSwitch(data);
		} else if (infoIdenCode.equals("1f")) {

			/*
			 * 遥信变位确定
			 */
			confirmSignalChangeInfo(ctx, data);
		} else if (infoIdenCode.equals("09")) {

			/*
			 * 遥测变化值，或者总召获取全部遥测值
			 */
			changeMesurementInfo(data);
		} else if (infoIdenCode.equals("01")) {

			/*
			 * 遥信总召所有值获取
			 */
			readAllSignal(hitchEventDesc, data);
		} else {
			logger.info("undefine message received!");
			logger.error("接收到的非法数据--------------------" + data);
		}
	}

	/**
	 * 老版本机器中将总召，遥信，遥测值都放在一起，将其一起解析，可以获取某个开关的所有开关信息
	 * 
	 * @param data
	 *            报文
	 */
	private void getSwitchAllInfo(ChannelHandlerContext ctx, String data, String hitchEventDesc) {

		/*
		 * 遥控信息
		 */
		while (data.length() != 0) {
			int index = StringCommonUtil.getFirstIndexOfEndTag(data, "16");
			if (index != -1) {
				String dataInfo = data.substring(0, index);
				handleIdenCode(ctx, dataInfo, hitchEventDesc);
				data = data.substring(index, data.length());
			}
		}
	}

	/**
	 * 在发送遥控预置操作之后，若开关空闲，则可以开始对本开关进行开合闸操作<br>
	 * <br>
	 * 
	 * TODO 现在如果前端发送一个开合闸的命令，那么该命令会同时生成一个预执行命令和执行命令，
	 * 并且拼接起来全部发送给设备，所以这个地方是没有按照协议中写的来进行的，当时也是为了让设备
	 * 能够快速看到效果，即马上开合闸，不然如果报文因为网速的缘故迟迟不来的话，页面上没看到效果就认为 是这个项目的原因。
	 * 因为下面的方法是当接收到预执行命令才去调用方法的，通过报文返回的数据来判断是否要去发出执行命令，
	 * 所以是不会被执行的。如果到时发现了重大的问题，就对发送命令的方法进行重写吧！这个类在这里
	 * 
	 * @see HighVoltageSwitchMessageEngine#generateCloseSwitchMessage(String)
	 * @see HighVoltageSwitchMessageEngine#generateOpenSwitchMessage(String)
	 */
	private void whetherOperateSwitch(String data) {

		/*
		 * 81代表终端空闲；80代表拒绝
		 */
		if (data.length() > 32) {
			if (data.substring(30, 32).equals("81")) {
				String address = data.substring(10, 14);
				SwitchGPRS gprs = CtxStore.getByAddress(address);
				/*
				 * 1合闸；2分闸；
				 */
				if (gprs.getPrepareType() == 1) {
					highVoltageEngine.generateCloseSwitchMessage(address);
				} else {
					highVoltageEngine.generateOpenSwitchMessage(address);
				}
				gprs.setPrepareType(3);
			}
		}
	}

	/**
	 * 所有遥信值的获取
	 * 
	 * @param hitchEventDesc
	 *            报警原因描述
	 * @param data
	 */
	private void readAllSignal(String hitchEventDesc, String data) {

		String address = data.substring(10, 14);
		String id = CtxStore.getIdbyAddress(address);

		if (id != null && address != null) {

			HighVoltageStatus s = CtxStore.getStatusbyId(id);
			SwitchGPRS gprs = CtxStore.get(id);

			if (s == null) {

				s = new HighVoltageStatus();
				s.setId(id);
				CtxStore.addStatus(s);
			}

			s.setGuo_liu_yi_duan(data.substring(30, 32));
			s.setGuo_liu_er_duan(data.substring(32, 34));
			s.setGuo_liu_san_duan(data.substring(34, 36));

			s.setLing_xu_guo_liu_(data.substring(38, 40));

			if (data.substring(40, 42).equals("01") || data.substring(42, 44).equals("01")
					|| data.substring(44, 46).equals("01")) {
				s.setChong_he_zha("01");
			} else {
				s.setChong_he_zha("00");
			}

			s.setPt1_you_ya(data.substring(48, 50));
			s.setPt2_you_ya(data.substring(50, 52));

			s.setPt1_guo_ya(data.substring(52, 54));
			s.setPt2_guo_ya(data.substring(54, 56));

			String new_status = data.substring(30, 32);

			if ("01".equals(s.getStatus()) && "00".equals(new_status)) {

				gprs.setOpen(true);

				HighVoltageHitchEvent event = new HighVoltageHitchEvent();

				event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				event.setHitchPhase("A");
				event.setHitchReason(hitchEventDesc == null ? "未知报警" : hitchEventDesc);
				event.setChangeType(0);
				event.setSolveWay("分闸");
				event.setId(UUIDUtil.getUUID());
				event.setSwitchId(id);
				// event.setSolvePeople();
				hitchEventService.insert(event);

				logger.info("-----------跳闸成功");
			} else if ("01".equals(new_status) && "00".equals(s.getStatus())) {

				gprs.setOpen(false);

				HighVoltageHitchEvent event = new HighVoltageHitchEvent();

				event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				event.setHitchPhase("A");
				event.setHitchReason(hitchEventDesc == null ? "未知报警" : hitchEventDesc);
				event.setChangeType(1);
				event.setSolveWay("合闸");
				event.setId(UUIDUtil.getUUID());
				event.setSwitchId(id);
				hitchEventService.insert(event);
				logger.info("-----------合闸成功");
			}
			s.setStatus(new_status);

			s.setJiao_liu_shi_dian(data.substring(76, 78));

			s.setShou_dong_he_zha(data.substring(78, 80));
			s.setShou_dong_fen_zha(data.substring(80, 82));

			s.setYao_kong_he_zha(data.substring(84, 86));
			s.setYao_kong_fen_zha(data.substring(86, 88));
			s.setYao_kong_fu_gui(data.substring(88, 90));

			logger.info("状态变为-----------" + new_status);

		} else {
			logger.error("there is an error in catching hitch event!");
		}
	}

	/*
	 * public static void main(String[] args) { String str0 =
	 * "68 53 53 68 F4 04 00 01 C8 14 01 04 00 01 00 01 00 00 01 00 00 00 01 00 01 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E2 16 68 0C 0C 68 F4 04 00 64 01 0A 01 04 00 00 00 14 80 16"
	 * .replace(" ", ""); String str1 =
	 * "68 53 53 68 F4 04 00 01 C8 14 01 04 00 01 00 00 00 00 01 00 00 00 01 00 01 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E1 16 68 0C 0C 68 F4 04 00 64 01 0A 01 04 00 00 00 14 80 16"
	 * .replace(" ", ""); System.out.println(str0.substring(30, 32));
	 * System.out.println(str1.substring(30, 32)); }
	 */

	/**
	 * 遥测归一值获取（遥测中的变化数据）
	 * 
	 * @param data
	 */
	private void changeMesurementInfo(String data) {

		if (!data.substring(2, 4).equals("47")) {
			/**
			 * 若data.length=40，测归一值
			 */
			logger.info("测归一值-------" + data);
			// 680e0e68f4680009010301680008400000001a16
			// 68131368f46600090203016600054000000006402700008116
			// 68131368f46600090203016600054000000006402700008116
			// 68131368f4710009020301710006402c000008402a0000c916
			for (int i = 22; i + 14 < data.length(); i += 10) {
				getMessageAddress(data.substring(i + 4, i + 8), data.substring(22, 26), data.substring(i + 8, i + 14));
			}
			return;
		}
		logger.info("解析CV---------" + data);
		String address = data.substring(10, 14);
		String id = CtxStore.getIdbyAddress(address);
		if (id != null) {
			saveCV(id, data);
		} else {
			logger.error("there is an error in saving CV!");
		}
	}

	/**
	 * 遥信值的归一值获取，并且遥信变化是要服务器端发出接收确认的
	 * 
	 * @param ctx
	 * @param data
	 */
	private void confirmSignalChangeInfo(ChannelHandlerContext ctx, String data) {

		for (int i = 26, j = Integer.valueOf(data.substring(16, 18)); j > 0; i += 6, --j) {
			changeState(data.substring(22, 26), data.substring(i, i + 4), data.substring(i + 4, i + 6));
		}
		String resu = new HighVoltageDeviceCommandUtil().confirmChangeAffair(data.substring(10, 14));
		logger.info("遥信变位事件确定---------" + resu);
		ctx.writeAndFlush(resu);
	}

	/**
	 * 遥信值的归一值初始获取，遥信值变化时会分两个流程，第一个流程先发一次，若有收到回复，再发一次，整个流程才算结束。
	 * 
	 * @param ctx
	 * @param data
	 */
	private void confirmSignalInitialChange(ChannelHandlerContext ctx, String data) {

		String resu = new HighVoltageDeviceCommandUtil().confirmChangeAffair(data.substring(10, 14));
		readAllSignal("控制回路", data);
		logger.info("遥信变位确定---------" + resu);
		ctx.writeAndFlush(resu);
	}

	private void changeState(String address, String code, String value) {

		if (code == null || code.length() == 0 || code.length() != 4) {
			return;
		}
		if (code.endsWith("00")) {
			/*
			 * 报文反转
			 */
			code = LowVoltageDeviceCommandUtil.reverseStringBy2(code);
		}
		if (CtxStore.getIdbyAddress(address) == null
				|| CtxStore.getStatusbyId(CtxStore.getIdbyAddress(address)) == null) {
			return;
		}
		if (value.equals("02")) {
			value = "01";
		} else {
			value = "00";
		}
		HighVoltageStatus hvs = CtxStore.getStatusbyId(CtxStore.getIdbyAddress(address));
		switch (code) {
		case "0000":
			hvs.setGuo_liu_yi_duan(value);
			break;
		case "0001":
			hvs.setGuo_liu_er_duan(value);
			break;
		case "0002":
			hvs.setGuo_liu_san_duan(value);
			break;
		case "0004":
			hvs.setLing_xu_guo_liu_(value);
			break;
		case "000A":
			hvs.setPt1_you_ya(value);
			break;
		case "000B":
			hvs.setPt1_guo_ya(value);
			break;
		case "000C":
			hvs.setPt2_guo_ya(value);
			break;
		case "000D":
			hvs.setShou_dong_he_zha(value);
			break;
		case "000E":
			hvs.setShou_dong_fen_zha(value);
			break;
		case "00FB":
			hvs.setYao_kong_he_zha(value);
			break;
		case "00FC":
			hvs.setYao_kong_he_zha(value);
			break;
		case "00FD":
			hvs.setYao_kong_fu_gui(value);
			break;
		}
	}

	private void getMessageAddress(String code, String address, String value) {

		if (code == null || code.length() == 0 || code.length() != 4) {
			return;
		}
		if (code.endsWith("40")) {
			/*
			 * 报文反转
			 */
			code = LowVoltageDeviceCommandUtil.reverseStringBy2(code);
		}
		if (CtxStore.getIdbyAddress(address) == null) {
			return;
		}

		switch (code) {
		case "4001":
			saveVoltageForValue(CtxStore.getIdbyAddress(address), "A",
					HighVoltageDeviceCommandUtil.changToRight(value));
			break;
		case "4006":
			saveCurrentForValue(CtxStore.getIdbyAddress(address), "A",
					HighVoltageDeviceCommandUtil.changToRight(value));
			break;
		case "4007":
			saveCurrentForValue(CtxStore.getIdbyAddress(address), "B",
					HighVoltageDeviceCommandUtil.changToRight(value));
			break;
		case "4008":
			saveCurrentForValue(CtxStore.getIdbyAddress(address), "C",
					HighVoltageDeviceCommandUtil.changToRight(value));
			break;
		default:
			break;
		}
	}

	/**
	 * 获取在线开关的逻辑地址
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
		String address;
		if (data.startsWith("68")) 
			address = data.substring(10, 14);
		else
			address = data.substring(12, 16);
		gprs.setAddress(address);

		address = new HighVoltageDeviceCommandUtil().reverseString(address);

		if (gprs != null) {
			/*
			 * 根据反转后的地址查询得到highvoltageswitch的集合
			 */
			List<HighVoltageSwitch> list = switchService
					.selectByParameters(MyBatisMapUtil.warp("address", Integer.parseInt(address, 16)));
			HighVoltageSwitch s = null;
			if (list != null && list.size() != 0) {

				s = list.get(0);
				String id = s.getId();
				gprs.setId(id);

				/*
				 * 这个地方是开始对bug的一个测试，当开关从跳闸到合闸的时候，无法及时获取，只能 这样替换ctx才能进行状态更新
				 * 
				 * if (CtxStore.get(id) != null) { CtxStore.remove(id);
				 * CtxStore.add(gprs); }
				 * 
				 */
				HighVoltageServer.totalCall(id);
				if (CtxStore.get(id) != null) {
					CtxStore.remove(id);
					CtxStore.add(gprs);
				}
			} else {
				logger.info("this device is not registered!!");
			}
		}
	}

	private String getHitchReason(String string) {

		for (int i = 0; i < string.length(); i = i + 2) {
			if (string.substring(i, i + 2).equals("01")) {
				return com.gdut.dongjun.enums.HighVoltageHitchEvent.getStatement(i / 2 + 1).toString();
			}
		}
		return "未知报警";
	}

	/**
	 * 根据开关的id， 和报文信息来获取该开关的全部电压电流值，调用方法保存
	 */
	public void saveCV(String switchId, String data) {

		data = data.replace(" ", "");
		String ABVoltage = new HighVoltageDeviceCommandUtil().readABPhaseVoltage(data);
		String BCVoltage = new HighVoltageDeviceCommandUtil().readBCPhaseVoltage(data);
		String ACurrent = new HighVoltageDeviceCommandUtil().readAPhaseCurrent(data);
		String BCurrent = new HighVoltageDeviceCommandUtil().readBPhaseCurrent(data);
		String CCurrent = new HighVoltageDeviceCommandUtil().readCPhaseCurrent(data);

		saveCurrentForValue(switchId, "A", ACurrent);
		saveCurrentForValue(switchId, "B", BCurrent);
		saveCurrentForValue(switchId, "C", CCurrent);
		saveVoltageForValue(switchId, "A", ABVoltage);
		saveVoltageForValue(switchId, "B", BCVoltage);
	}

	/**
	 * 保存电压的一个相值
	 */
	private void saveVoltageForValue(String switchId, String phase, String value) {

		Date date = new Date();
		Map<String, Object> map = new HashMap<>(3);
		map.put("switch_id", switchId);
		map.put("phase", phase);
		List<HighVoltageVoltage> list = voltageService.selectByParameters(MyBatisMapUtil.warp(map));
		if (list != null && list.size() != 0) {
			HighVoltageVoltage c1 = list.get(0);
			c1.setTime(date);
			c1.setValue(Integer.parseInt(value));
			voltageService.updateByPrimaryKey(c1);
			c1.setId(UUIDUtil.getUUID());
			historyVoltageService.insert(c1.changeToHistory());
		} else {
			HighVoltageVoltage c1 = new HighVoltageVoltage();
			c1.setId(UUIDUtil.getUUID());
			c1.setTime(date);
			c1.setSwitchId(switchId);
			c1.setValue(Integer.parseInt(value));
			c1.setPhase(phase);
			voltageService.insert(c1);
			historyVoltageService.insert(c1.changeToHistory());
		}
	}

	/**
	 * 保存电流的一个相值
	 */
	private void saveCurrentForValue(String switchId, String phase, String value) {

		Date date = new Date();
		Map<String, Object> map = new HashMap<>(3);
		map.put("switch_id", switchId);
		map.put("phase", phase);
		List<HighVoltageCurrent> list = currentService.selectByParameters(MyBatisMapUtil.warp(map));
		if (list != null && list.size() != 0) {
			HighVoltageCurrent c1 = list.get(0);
			c1.setTime(date);
			c1.setValue(Integer.parseInt(value));
			currentService.updateByPrimaryKey(c1);
			c1.setId(UUIDUtil.getUUID());
			historyCurrentService.insert(c1.changeToHistory());
		} else {
			HighVoltageCurrent c1 = new HighVoltageCurrent();
			c1.setId(UUIDUtil.getUUID());
			c1.setTime(date);
			c1.setSwitchId(switchId);
			c1.setValue(Integer.parseInt(value));
			c1.setPhase(phase);
			currentService.insert(c1);
			historyCurrentService.insert(c1.changeToHistory());
		}
	}
}