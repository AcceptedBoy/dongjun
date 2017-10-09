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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.HighVoltageCtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.device_message_engine.impl.HighVoltageSwitchMessageEngine;
import com.gdut.dongjun.core.handler.DataMonitorService;
import com.gdut.dongjun.core.server.impl.HighVoltageServer;
import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.service.HighVoltageCurrentService;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.HighVoltageVoltageService;
import com.gdut.dongjun.service.HistoryHighVoltageCurrentService;
import com.gdut.dongjun.service.HistoryHighVoltageVoltageService;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;
import com.gdut.dongjun.util.LowVoltageDeviceCommandUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.StringCommonUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * TODO 值得关注的一个类 关有设备
 * 
 * @author Sherlock
 * @author Acceptedboy
 */
@Service
@Sharable
public class HighVoltageDataReceiver extends ChannelInboundHandlerAdapter {

	//
	private static final char[] EB_UP = new char[] { 'E', 'B', '9', '0' }; // EB90
	private static final char[] EB_DOWN = new char[] { 'e', 'b', '9', '0' }; // eb90
	private static final char[] CODE_EB90 = new char[] { 'e', 'b', '9', '0', 'e', 'b', '9', '0', 'e', 'b', '9', '0' };
	private static final char[] CODE_64 = new char[] { '6', '4' }; // 64
	private static final char[] CODE_03 = new char[] { '0', '3' }; // 03
	private static final char[] CODE_2E = new char[] { '2', 'e' }; // 2e
	private static final char[] CODE_1F = new char[] { '1', 'f' }; // 1f
	private static final char[] CODE_09 = new char[] { '0', '9' }; // 09
	private static final char[] CODE_81 = new char[] { '8', '1' }; // 81
	private static final char[] CODE_OO = new char[] { '0', '0' }; // OO
	private static final char[] CODE_01 = new char[] { '0', '1' }; // OO
	private static final char[] CODE_47 = new char[] { '4', '7' }; // 47
	private static final char[] CODE_16 = new char[] { '1', '6' }; // 16
	private static final char[] CODE_68 = new char[] { '6', '8' }; // 68

	private static final String STR_00 = "00".intern();
	private static final String STR_01 = "01".intern();
	private static final String STR_02 = "02".intern();

	private static final Logger logger = LoggerFactory.getLogger(HighVoltageDataReceiver.class);

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
	@Autowired
	private HighVoltageSwitchService switchService;
	@Autowired
	private WebsiteServiceClient websiteClient;
	@Autowired
	private HighVoltageCtxStore hvCtxStore;
	@Autowired
	private DataMonitorService monitorService;

	public HighVoltageDataReceiver() {
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

		/*
		 * fix bug-> logger打下的日志不是原生接收的报文
		 *
		 * String data = ((String) msg).replace(" ", ""); logger.info("接收到的报文： "
		 * + data);
		 *
		 */
		String rowMsg = (String) msg;
		logger.info("接收到的报文： " + rowMsg);
		char[] data = CharUtils.removeSpace(rowMsg.toCharArray());

		/*
		 * String hitchEventDesc = "控制回路"; if (data.length == 190) {
		 * hitchEventDesc = "控制回路"
		 *//* getHitchReason(data.substring(140, 160)) *//*
														 * ; }
														 */
		handleSeparatedText(ctx, data);
	}

	private void handleSeparatedText(ChannelHandlerContext ctx, char[] data) {
		Integer begin = 0;
		int pointIndex = begin;
		while (true) {

			int pos = StringCommonUtil.getFirstIndexOfEndTag(data, pointIndex, "16");
			if (pos != -1) {
				if (isSeparatedPoint(data, pos)) {
					// 分割出独立报文段
					char[] dataInfo = CharUtils.subChars(data, begin, pos - begin);
					handleIdenCode(ctx, dataInfo);
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
			// 获取报文分割点
			// int pos = getSeparatedPoint(data, begin);
			// pos为-1报文异常
			// if (pos == -1) {
			// break;
			// }
			// pos为-2目标分割点非报文分割点
			// else if (pos == -2) {
			// continue;
			// }
		}
	}

	/**
	 * 得到报文分割点
	 * 
	 * @param data
	 * @param begin
	 * @return
	 */
	private int getSeparatedPoint(char[] data, Integer begin) {
		int index = StringCommonUtil.getFirstIndexOfEndTag(data, begin, "16");
		if (index != -1) {
			if (isSeparatedPoint(data, index)) {
				return index;
			} else {
				// 目标分割点非报文分割点
				begin = index;
				return -2;
			}
		}
		return -1;
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
		// 如果16后面是68xx68，返回true
		else if (CharUtils.equals(CharUtils.subChars(data, index, 2), CODE_68)
				&& CharUtils.equals(CharUtils.subChars(data, index + 6, 2), CODE_68)) {
			return true;
		}
		// 如果16后面是eb90eb90eb90xxxx16，返回true
		else if (index + 12 < data.length && // 防止出现数据丢包导致后续报文不足12个而导致NPE
				CharUtils.equals(CharUtils.subChars(data, index, 12), CODE_EB90)
				&& CharUtils.equals(CharUtils.subChars(data, index + 16, 2), CODE_16)) {
			return true;
		}
		return false;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		SwitchGPRS gprs = CtxStore.get(ctx);
		if (null != gprs.getAddress()) {
			logger.info("高压设备 " + gprs.getAddress() + "下线");
		}
		if (gprs != null) {
			CtxStore.remove(ctx);// 从Store中移除这个context
			if (gprs.getId() != null) {
				HighVoltageSwitch hvSwitch = switchService.selectByPrimaryKey(gprs.getId());
				hvSwitch.setOnlineTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				switchService.updateByPrimaryKey(hvSwitch);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	private void handleIdenCode(ChannelHandlerContext ctx, char[] data) {

		/*
		 * 这里做一个设备的注册，以免系统重启后设备不再发送eb90报文，出现设备不上线的情况
		 */
		if (!(CharUtils.startWith(data, EB_UP) || CharUtils.startWith(data, EB_DOWN))
				&& CharUtils.endsWith(data, CODE_16)) {
			AttributeKey<Integer> key = AttributeKey.valueOf("isRegisted");
			Attribute<Integer> attr = ctx.attr(key);
			if (null == attr.get()) {
				getOnlineAddress(ctx, data);
				attr.set(1);
			}
		} else if (CharUtils.startWith(data, EB_UP) || CharUtils.startWith(data, EB_DOWN)) {
			getOnlineAddress(ctx, data);
			return;
		}
		// 未续费的设备的报文不接受
		SwitchGPRS gprs = CtxStore.get(ctx);
		// TODO 未开启的功能
		// if (!hvCtxStore.isAddrAvailable(gprs.getAddress())) {
		// logger.info("未续费的设备报文：" + String.valueOf(data));
		// return;
		// }

		logger.info("处理报文：" + String.valueOf(data));
		char[] infoIdenCode = ArrayUtils.subarray(data, 14, 16);

		/*
		 * 将接收到的客户端信息分类处理 读通信地址并将地址反转
		 */
		if (CharUtils.equals(infoIdenCode, CODE_64)) {

			/*
			 * 总召激活确定
			 */
			logger.info("总召激活已经确定：" + CharUtils.newString(data, 10, 14));

			/*
			 * 为应对老机器中将总召，遥控，遥测值都加在同一份数据块而做的解析
			 */
			// if (data.length > 36 && isMultiedCode(data)) {
			// getSwitchAllInfo(ctx, data);
			// }
		} else if (CharUtils.equals(infoIdenCode, CODE_03)) {

			/*
			 * 遥信变位初步确定
			 */
			confirmSignalInitialChange(ctx, data);
		} else if (CharUtils.equals(infoIdenCode, CODE_2E)) {

			/*
			 * 遥控预置接收并发送遥控
			 */
			whetherOperateSwitch(data);
		} else if (CharUtils.equals(infoIdenCode, CODE_1F)) {

			/*
			 * 遥信变位确定
			 */
			confirmSignalChangeInfo(ctx, data);
		} else if (CharUtils.equals(infoIdenCode, CODE_09)) {

			/*
			 * 遥测变化值，或者总召获取全部遥测值
			 */
			changeMesurementInfo(data);
		} else if (CharUtils.equals(infoIdenCode, CODE_01)) {

			/*
			 * 遥信总召所有值获取
			 */
			readAllSignal(data);
		} else if (CharUtils.equals(infoIdenCode, CODE_68)) {
			/*
			 * 设备心跳报文 68 0D 0D 68 F4 01 00 68 01 07 01 01 00 00 00 AA 55 66 16
			 */
			logger.info("回复心跳报文" + String.valueOf(data));
		} else {
			logger.error("接收到的非法数据--------------------" + String.valueOf(data));
		}
		// 如果设备在被监视，通过回调函数把报文传回website系统。
		String address = CtxStore.get(ctx).getAddress();
		if (monitorService.isMonitored(address)) {
			websiteClient.getService().callbackTextArrived(address, String.valueOf(data));
		}
	}

	/**
	 * 老版本机器中将总召，遥信，遥测值都放在一起，将其一起解析，可以获取某个开关的所有开关信息
	 * 
	 * @param data
	 *            报文
	 */
	private void getSwitchAllInfo(ChannelHandlerContext ctx, char[] data) {

		/*
		 * 遥控信息
		 */
		int begin = 0;
		while (data.length != 0) {
			int index = StringCommonUtil.getFirstIndexOfEndTag(data, begin, "16");
			if (index != -1) {
				// 判断Index所指的地方是不是两段报文的分割点
				if (isEndOfMessage(data, index)) {
					handleIdenCode(ctx, ArrayUtils.subarray(data, 0, index));
					data = ArrayUtils.subarray(data, index, data.length);
				} else {
					begin = index;
				}
			} else {
				// permit the unsolved str like "681680" that could cause
				// endless loop.
				break;
			}
		}
	}

	/**
	 * 判断Index所指的地方是不是两段报文的分割点
	 * 
	 * @param data
	 * @param index
	 * @return
	 */
	private boolean isEndOfMessage(char[] data, int index) {
		// 如果index是data的终点，返回true
		if (index == data.length) {
			return true;
		}
		// 如果16后面是68xx68，返回true
		else if (CharUtils.equals(CharUtils.subChars(data, index, 2), CODE_68)
				&& CharUtils.equals(CharUtils.subChars(data, index + 6, 2), CODE_68)) {
			return true;
		}
		return false;
	}

	// /**
	// * 判断一段报文是不是由几段报文拼凑而成
	// *
	// * @param data
	// * @return
	// */
	// private boolean isMultiedCode(char[] data) {
	// for (int i = 0; i < data.length - 1; i++) {
	// // 如果是16开头
	// if (data[i] == '1' && data[i + 1] == '6') {
	// // 如果16在报文结尾，返回false
	// if (data.length == i + 2) {
	// return false;
	// }
	// //如果不是1668xx68的结构，返回false
	// if (i + 9 >= data.length) {
	// return false;
	// }
	// // 如果的确是1668xx68的结构，返回true
	// if (data[i + 2] == '6'
	// && data[i + 3] == '8'
	// && data[i + 8] == '6'
	// && data[i + 9] == '8') {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

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
	 * @update：意思是@see标注的两个方法已经同时发送预置命令和执行命令，不按协议来的。
	 * 之后可以改成@see的方法只发送预置命令， 在这个方法接收到预置命令返回报文，再发送执行报文。
	 * 
	 * 																	@see
	 *                                                                    HighVoltageSwitchMessageEngine
	 *                                                                    #
	 *                                                                    generateCloseSwitchMessage
	 *                                                                    (
	 *                                                                    String)
	 * @see HighVoltageSwitchMessageEngine#generateOpenSwitchMessage(String)
	 */
	private void whetherOperateSwitch(char[] data) {

		/*
		 * 81代表终端空闲；80代表拒绝；
		 */
		if (data.length > 32) {
			// 81是预置分闸返回报文，82是预置合闸返回报文
			if (CharUtils.equals(data, 30, 32, CODE_81)) {
				String address = CharUtils.newString(data, 10, 14).intern();
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
	private void readAllSignal(char[] data) {
		logger.info("接收全遥信报文：" + String.valueOf(data));
		String address = CharUtils.newString(data, 10, 14).intern();
		String id = CtxStore.getIdbyAddress(address);

		if (id != null && address != null) {

			HighVoltageStatus s = hvCtxStore.getStatusbyId(id);
			SwitchGPRS gprs = CtxStore.get(id);

			if (s == null) {

				s = new HighVoltageStatus();
				s.setId(id);
				hvCtxStore.addStatus(s);
			}

			s.setGuo_liu_yi_duan(getStr0Or01(data, 30, 32));
			s.setGuo_liu_er_duan(getStr0Or01(data, 32, 34));
			s.setGuo_liu_san_duan(getStr0Or01(data, 34, 36));

			s.setLing_xu_guo_liu_(getStr0Or01(data, 38, 40));

			if (CharUtils.equals(data, 40, 42, CODE_01) || CharUtils.equals(data, 42, 44, CODE_01)
					|| CharUtils.equals(data, 44, 46, CODE_01)) {
				s.setChong_he_zha(STR_01);
			} else {
				s.setChong_he_zha(STR_00);
			}

			s.setPt1_you_ya(getStr0Or01(data, 48, 50));
			s.setPt2_you_ya(getStr0Or01(data, 50, 52));

			s.setPt1_guo_ya(getStr0Or01(data, 52, 54));
			s.setPt2_guo_ya(getStr0Or01(data, 54, 56));

			String new_status = getStr0Or01(data, 66, 68);

			if ("01".equals(s.getStatus()) && "00".equals(new_status)) {
				// open为true表示跳闸成功
				gprs.setOpen(true);

				HighVoltageHitchEvent event = new HighVoltageHitchEvent();

				event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				event.setHitchPhase("A");
				event.setHitchReason("控制分闸");
				event.setChangeType(0);
				event.setSolveWay("分闸");
				event.setId(UUIDUtil.getUUID());
				event.setSwitchId(id);
				// event.setSolvePeople();
				hitchEventService.insert(event);

				logger.info("-----------跳闸成功");
				websiteClient.getService().callbackCtxChangeForVoice(id, 0);
			} else if ("01".equals(new_status) && "00".equals(s.getStatus())) {

				gprs.setOpen(false);

				HighVoltageHitchEvent event = new HighVoltageHitchEvent();

				event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				event.setHitchPhase("A");
				event.setHitchReason("控制合闸");
				event.setChangeType(1);
				event.setSolveWay("合闸");
				event.setId(UUIDUtil.getUUID());
				event.setSwitchId(id);
				hitchEventService.insert(event);
				logger.info("-----------合闸成功");
				websiteClient.getService().callbackCtxChangeForVoice(id, 1);
			}
			s.setStatus(new_status);

			s.setJiao_liu_shi_dian(getStr0Or01(data, 76, 78));

			s.setShou_dong_he_zha(getStr0Or01(data, 78, 80));
			s.setShou_dong_fen_zha(getStr0Or01(data, 80, 82));

			s.setYao_kong_he_zha(getStr0Or01(data, 84, 86));
			s.setYao_kong_fen_zha(getStr0Or01(data, 86, 88));
			s.setYao_kong_fu_gui(getStr0Or01(data, 88, 90));

			logger.info("状态变为-----------" + new_status);
			websiteClient.getService().callbackDeviceChange(id, 1);
			websiteClient.getService().callbackCtxChange();

		} else {
			logger.error("there is an error in catching hitch event!");
		}
	}

	/**
	 * 这个方法是对{@code data.substring(76, 78)}的优化，因为substring还需要切割字符串产生新的对象，
	 * 所以为了减少对象的生成，采取下面的方法，不会产生新对象，{@code 01}和{@code 00}都是可重用的final 常量池对象
	 * 
	 * @param data
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	private String getStr0Or01(char[] data, int beginIndex, int endIndex) {
		return CharUtils.equals(data, beginIndex, endIndex, CODE_01) ? STR_01 : STR_00;
	}

	/**
	 * 遥测归一值获取（遥测中的变化数据）
	 * 
	 * @param data
	 */
	private void changeMesurementInfo(char[] data) {

		if (!CharUtils.equals(data, 2, 4, CODE_47)) {
			/*
			 * 若data.length=40，遥测变位，测归一值
			 */
			logger.info("接收遥测变化值（测归一值）-------" + String.valueOf(data));
			// 680e0e68f4680009010301680008400000001a16
			// 68131368f46600090203016600054000000006402700008116
			// 68131368f46600090203016600054000000006402700008116
			for (int i = 22; i + 14 < data.length; i += 10) {
				getMessageAddress(CharUtils.newString(data, i + 4, i + 8), CharUtils.newString(data, 22, 26),
						CharUtils.newString(data, i + 8, i + 14));
			}
			String address = CharUtils.newString(data, 10, 14);
			String id = CtxStore.getIdbyAddress(address);
			websiteClient.getService().callbackDeviceChange(id, 1);
			return;
		}
		// 全遥测
		logger.info("接收全遥测（解析CV）---------" + String.valueOf(data));
		String address = CharUtils.newString(data, 10, 14);
		String id = CtxStore.getIdbyAddress(address);
		if (id != null) {
			saveCV(id, String.valueOf(data));
			websiteClient.getService().callbackDeviceChange(id, 1);
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
	private void confirmSignalChangeInfo(ChannelHandlerContext ctx, char[] data) {

		// >>>>>>>>>>>>> 修正bug:不能显示合闸分分闸：双点遥信
		/*
		 * for(int i = 26, j = Integer.valueOf(data.substring(16, 18)); j > 0; i
		 * += 6, --j) { changeState(data.substring(22, 26), data.substring(i, i
		 * + 4), data.substring(i + 4, i + 6)); }
		 */
		// 测试报文：68 13 13 68 F3 01 00 1F 01 03 01 01 00 05 00 01 A3 D7 06 0D B3
		// 03 0A 70 16
		logger.info("接收遥信变位事件---------" + String.valueOf(data));
		String address = CharUtils.newString(data, 10, 14).intern();
		HighVoltageStatus s = hvCtxStore.getStatusbyId(CtxStore.getIdbyAddress(address));
		if (CharUtils.equals(data, 30, 32, CODE_01)) {
			// 遥信 TODO
		} else {

		}
		String resu = new HighVoltageDeviceCommandUtil().confirmChangeAffair(address);
		logger.info("发送遥信变位事件确定---------" + resu);
		ctx.writeAndFlush(resu, ctx.voidPromise());
	}

	/**
	 * 遥信值的归一值初始获取，遥信值变化时会分两个流程，第一个流程先发一次，若有收到回复，再发一次，整个流程才算结束。
	 * 
	 * @param ctx
	 * @param data
	 */
	private void confirmSignalInitialChange(ChannelHandlerContext ctx, char[] data) {

		logger.info("接收遥信初步变位事件---------" + String.valueOf(data));

		// logger.info("遥信变位：" + String.valueOf(data));
		// //在这里更改遥信值
		// String iden = String.valueOf(CharUtils.subChars(data, 2 * 13, 2));
		// String value = String.valueOf(CharUtils.subChars(data, 2 * 15, 2));
		// HighVoltageStatus s =
		// hvCtxStore.getStatusbyId(CtxStore.get(ctx).getId());
		// switch (iden) {
		// case "01" :
		// //合闸分闸判断位
		// s.setStatus(value); break;
		// default : break;
		// }

		String resu = new HighVoltageDeviceCommandUtil().confirmChangeAffair(CharUtils.newString(data, 10, 14));
		logger.info("发送遥信变位确定---------" + resu);
		ctx.writeAndFlush(resu, ctx.voidPromise());
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
				|| hvCtxStore.getStatusbyId(CtxStore.getIdbyAddress(address)) == null) {
			return;
		}
		value = getDualPointStr(value);
		// if (value.equals("02")) {
		// value = STR_01;
		// } else {
		// value = STR_00;
		// }
		HighVoltageStatus hvs = hvCtxStore.getStatusbyId(CtxStore.getIdbyAddress(address));
		switch (code) {
		case "0000":
			hvs.setGuo_liu_yi_duan(value.intern());
			break;
		case "0001":
			hvs.setGuo_liu_er_duan(value.intern());
			break;
		case "0002":
			hvs.setGuo_liu_san_duan(value.intern());
			break;
		case "0004":
			hvs.setLing_xu_guo_liu_(value.intern());
			break;
		case "000A":
			hvs.setPt1_you_ya(value.intern());
			break;
		case "000B":
			hvs.setPt1_guo_ya(value.intern());
			break;
		case "000C":
			hvs.setPt2_guo_ya(value.intern());
			break;
		case "000D":
			hvs.setShou_dong_he_zha(value.intern());
			break;
		case "000E":
			hvs.setShou_dong_fen_zha(value.intern());
			break;
		case "00FB":
			hvs.setYao_kong_he_zha(value.intern());
			break;
		case "00FC":
			hvs.setYao_kong_he_zha(value.intern());
			break;
		case "00FD":
			hvs.setYao_kong_fu_gui(value.intern());
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
	private void getOnlineAddress(ChannelHandlerContext ctx, char[] data) {

		SwitchGPRS gprs = CtxStore.get(ctx);
		/*
		 * 当注册的高压开关的地址不为空，说明已经注册过了，不再进行相关操作
		 */
		if (gprs != null && gprs.getAddress() != null) {
			if (CharUtils.startWith(data, EB_UP) || CharUtils.startWith(data, EB_DOWN)) {
				ctx.channel().writeAndFlush(data);
			}
			return;
		}
		String address;
		if (CharUtils.startWith(data, CODE_68)) {
			address = CharUtils.newString(data, 10, 14).intern();
		} else
			address = CharUtils.newString(data, 12, 16).intern();
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
				websiteClient.getService().callbackDeviceChange(id, 1);
				/*
				 * 这个地方是开始对bug的一个测试，当开关从跳闸到合闸的时候，无法及时获取，只能 这样替换ctx才能进行状态更新
				 * 
				 * if (CtxStore.get(id) != null) { CtxStore.remove(id);
				 * CtxStore.add(gprs); }
				 * 
				 */
				if (CharUtils.startWith(data, CODE_68)) { // 发送eb90
					ctx.channel().writeAndFlush(CharUtils.newString(data).intern()); // intern的操作会从字符串池中查看有没有字符串跟data的字符串相符，有则返回它的引用
				}
				HighVoltageServer.totalCall(id);
//				 if (CtxStore.get(id) != null) {
//					 CtxStore.remove(id);
//					 CtxStore.add(gprs);
//				 }
				websiteClient.getService().callbackCtxChangeForVoice(id, 2);
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
		Map<String, Object> map = new HashMap<>(4, 1);
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
		Map<String, Object> map = new HashMap<>(4, 1);
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

	/**
	 * 双点遥信值变单点遥信值
	 * 
	 * @param value
	 * @return
	 */
	private String getDualPointStr(String value) {
		switch (value) {
		case "01":
			// 分位
			return STR_00;
		case "02":
			// 合位
			return STR_01;
		case "03":
			return STR_02;
		default:
			return STR_02;
		}
	}

}