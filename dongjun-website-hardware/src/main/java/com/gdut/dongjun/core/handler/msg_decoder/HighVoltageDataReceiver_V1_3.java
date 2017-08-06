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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.HighVoltageCtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.device_message_engine.impl.HighVoltageSwitchMessageEngine;
import com.gdut.dongjun.core.handler.DataMonitorService;
import com.gdut.dongjun.core.handler.HitchConst;
import com.gdut.dongjun.core.server.impl.HighVoltageServer_V1_3;
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
import com.gdut.dongjun.util.Standard101Util;
import com.gdut.dongjun.util.StringCommonUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;
import com.sun.xml.bind.api.impl.NameConverter.Standard;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 高压协议1.3版本 TODO 真的需要重构啊
 * eb90->链路建立过程（看文档）->对时->总召
 * @author Sherlock
 */
@Service
@Sharable
public class HighVoltageDataReceiver_V1_3 extends ChannelInboundHandlerAdapter {
	
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
	private static final char[] CODE_3b = new char[] { '3', 'b' }; // 3b
	private static final char[] CODE_16 = new char[] { '1', '6' }; // 16
	private static final char[] CODE_68 = new char[] { '6', '8' }; // 68
	private static final char[] CODE_67 = new char[] { '6', '7' };
	private static final char[] CODE_108B = new char[] { '1', '0', '8', 'b' };
	private static final char[] CODE_1080 = new char[] { '1', '0', '8', '0' };
	private static int BYTE = 2;
    private static final String STR_00 = "00".intern();
    private static final String STR_01 = "01".intern();
    private static final String STR_02 = "02".intern();
//    private static final String STR_03 = "03".intern();

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
	@Autowired
	private HighVoltageCtxStore hvCtxStore;
	@Resource(name = "HighVoltageSwitchMessageEngine")
	private HighVoltageSwitchMessageEngine highVoltageEngine;
	@Autowired
	private HighVoltageSwitchService switchService;
	@Autowired
	private WebsiteServiceClient websiteClient;
	@Autowired
	private DataMonitorService monitorService;

	private static final Logger logger = LoggerFactory.getLogger(HighVoltageDataReceiver_V1_3.class);

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

		// String data = ((String) msg).replace(" ", "");
		String message = (String) msg;
		char[] data = CharUtils.removeSpace(message.toCharArray());
		logger.info("接收到的报文： " + String.valueOf(data));

		String hitchEventDesc = "控制回路";
		// TODO
		// if (data.length() == 190) {
		// hitchEventDesc = getHitchReason(data.substring(140, 160));
		// }
		if (handleRegis(ctx, data)) {
			return ;
		}
		handleSeparatedText(ctx, data, hitchEventDesc);
//		handleIdenCode(ctx, data, hitchEventDesc);
	}
	
	private boolean handleRegis(ChannelHandlerContext ctx, char[] data) {
		//终端响应链路状态
		if (CharUtils.startWith(data, CODE_108B)) {
			// 主站复位远方链路
			String addr = CtxStore.get(ctx).getAddress();
			logger.info("来自" + addr + "的请求链路应答" + String.valueOf(data));
			String msg = Standard101Util.getResetLinkRequest(addr);
			ctx.writeAndFlush(msg);
			logger.info("发送到" + addr + "的复位链路报文" + String.valueOf(msg));
			return true;
		} else if (CharUtils.startWith(data, CODE_1080)) {
			// 终端确认复位远方链路
			String addr = CtxStore.get(ctx).getAddress();
			logger.info("来自" + addr + "的链路确认报文" + String.valueOf(data));
			//	确认链路报文
			ctx.writeAndFlush(Standard101Util.getConfirmFrame(CtxStore.get(ctx).getAddress()));
			//	链路建立过程完成
			CtxStore.get(ctx).setLinked(true);
			AttributeKey<Integer> key = AttributeKey.valueOf("firstCall");
			Attribute<Integer> attr = ctx.attr(key);
			if (null == attr.get()) {
				HighVoltageDeviceCommandUtil ut = new HighVoltageDeviceCommandUtil();
				String msg = ut.checkTime(addr);
				ctx.writeAndFlush(msg);
				logger.info("发送到" + addr + "的对时报文" + msg);
				//	链路建立成功发出总召
				HighVoltageServer_V1_3.totalCall(CtxStore.get(ctx).getId());
				attr.set(1);
			}
			return true;
		}
		return false;
	}
	
	private void handleSeparatedText(ChannelHandlerContext ctx, char[] data, String hitchEventDesc) {
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
						handleIdenCode(ctx, dataInfo, hitchEventDesc);
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
	 * 得到报文分割点
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
				//目标分割点非报文分割点
				begin = index;
				return -2;
			}
		}
		return -1;
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
		// 如果16后面是68xxxx68，返回true
		else if (data.length >= index + 8 &&
				CharUtils.equals(CharUtils.subChars(data, index, 2), CODE_68)
				&& CharUtils.equals(CharUtils.subChars(data, index + 6, 2), CODE_68)) {
			return true;
		}
		// 如果16后面是eb90eb90eb90xxxx16，返回true
		else if (
				index + 18 <= data.length &&	//防止出现数据丢包导致后续报文不足18个而导致NPE
				CharUtils.equals(CharUtils.subChars(data, index, 12), CODE_EB90)
				&& CharUtils.equals(CharUtils.subChars(data, index + 16, 2), CODE_16)) {
			return true;
		}
		return false;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		SwitchGPRS gprs = CtxStore.get(ctx);
		logger.info("高压设备1.3 " + gprs.getAddress() + "下线");
		if (gprs != null) {
			// 从Store中移除这个context
			CtxStore.remove(ctx);
//			// 清空Attribute
//			AttributeKey<Integer> key = AttributeKey.valueOf("isRegisted");
//			Attribute<Integer> attr = ctx.attr(key);
//			attr.set(null);
			// 记录最后上线时间
			if (gprs.getId() != null) {
				HighVoltageSwitch hvSwitch = switchService.selectByPrimaryKey(gprs.getId());
				hvSwitch.setOnlineTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				switchService.updateByPrimaryKey(hvSwitch);
			}
			// 清空状态
			hvCtxStore.removeStatusById(gprs.getId());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	private void handleIdenCode(ChannelHandlerContext ctx, char[] data, String hitchEventDesc) {
		
		//	少于可变帧长帧格式的9个字节，认为是非有效报文
		if (data.length < BYTE * 9) {
			return ;
		}
		
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
			logger.info("处理报文：" + String.valueOf(data));
			getOnlineAddress(ctx, data);
			ctx.writeAndFlush(String.valueOf(data));
			AttributeKey<Integer> key = AttributeKey.valueOf("linkRequest");
			Attribute<Integer> attr = ctx.attr(key);
			if (null == attr.get()) {
				// 主站发起链路请求
				String msg = Standard101Util.getLinkRequest(CtxStore.get(ctx).getAddress());
				ctx.writeAndFlush(msg);
				logger.info("发送到" + CtxStore.get(ctx).getAddress() + "的请求链路报文");
				attr.set(1);
			}
			return ;
		}
		// 未续费的设备的报文不接受
//		SwitchGPRS gprs = CtxStore.get(ctx);
//		if (!hvCtxStore.isAddrAvailable(gprs.getAddress())) {
//			logger.info("未续费的设备报文：" + String.valueOf(data));
//			return;
//		}
		
		char[] infoIdenCode = CharUtils.subChars(data, BYTE * 7, BYTE);

		/*
		 * 将接收到的客户端信息分类处理 读通信地址并将地址反转
		 */
		if (CharUtils.equals(infoIdenCode, CODE_64)) {
			/*
			 * 总召激活确定
			 */
			logger.info("总召激活已经确定：" + CharUtils.newString(data, BYTE * 5, BYTE * 7).intern());

			/*
			 * 为应对老机器中将总召，遥控，遥测值都加在同一份数据块而做的解析
			 */
			if (data.length > 36 && isMultiedCode(data)) {
				getSwitchAllInfo(ctx, data, hitchEventDesc);
			}
		} else if (CharUtils.equals(infoIdenCode, CODE_03)) {

			/*
			 * 遥信变位初步确定，双点遥信
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
			changeMesurementInfo(ctx, data);
		} else if (CharUtils.equals(infoIdenCode, CODE_01)) {

			/*
			 * 遥信总召所有值获取，单点遥信
			 */
			readAllSignal(hitchEventDesc, data);
		} else if (CharUtils.equals(infoIdenCode, CODE_68)) { 
			/*
			 * 设备心跳报文  68 0D 0D 68 F4 01 00 68 01 07 01 01 00 00 00 AA 55 66 16
			 */
			confirmHeart(ctx, data);
//			SwitchGPRS gprs1 = CtxStore.get(ctx);
//			if (null == gprs1 || null == gprs1.getAddress() || "".equals(gprs1.getAddress())) {
//				// 如果设备还没有留下地址就发送全域总召
//				HighVoltageDeviceCommandUtil ut = new HighVoltageDeviceCommandUtil();
////				ctx.writeAndFlush(ut.anonTotalCall());
////				ctx.writeAndFlush(msg)
//				AttributeKey<Integer> key = AttributeKey.valueOf("isRegisted");
//				Attribute<Integer> attr = ctx.attr(key);
//				attr.set(null);
//			}
		} else if (CharUtils.equals(infoIdenCode, CODE_67)) {
			// 终端响应对时命令
			logger.info("终端响应对时命令：" + String.valueOf(data));
		}
		else {
			logger.error("接收到的非法数据--------------------" + String.valueOf(data));
		}
		//如果设备在被监视，通过回调函数把报文传回website系统。
		String address = CtxStore.get(ctx).getAddress();
		if (monitorService.isMonitored(address)) {
			websiteClient.getService().callbackTextArrived(CtxStore.getIdbyAddress(address), String.valueOf(data));
		}
	}

	/**
	 * 处理心跳报文
	 * @param ctx
	 * @param data
	 */
	private void confirmHeart(ChannelHandlerContext ctx, char[] data) {
		logger.info("接收心跳报文：" + String.valueOf(data));
		SwitchGPRS g = CtxStore.get(ctx);
		//	如果没完成链路建立过程，就重新发链路建立请求
		if (!g.isLinked()) {
			String msg = Standard101Util.getLinkRequest(g.getAddress());
			g.getCtx().writeAndFlush(msg);
		}
	}

	/**
	 * 老版本机器中将总召，遥信，遥测值都放在一起，将其一起解析，可以获取某个开关的所有开关信息
	 * 
	 * @param data
	 *            报文
	 */
	private void getSwitchAllInfo(ChannelHandlerContext ctx, char[] data, String hitchEventDesc) {

		int begin = 0;
		while (data.length != 0) {
			int index = StringCommonUtil.getFirstIndexOfEndTag(data, begin, "16");
			if (index != -1) {
				//判断Index所指的地方是不是两段报文的分割点
				if (isEndOfMessage(data, index)) {
					char[] dataInfo = CharUtils.subChars(data, 0, index);
					handleIdenCode(ctx, dataInfo, hitchEventDesc);
					data = CharUtils.subChars(data, index, data.length - index);
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
	
	/**
	 * 判断一段报文是不是由几段报文拼凑而成
	 * 
	 * @param data
	 * @return
	 */
	private boolean isMultiedCode(char[] data) {
		for (int i = 0; i < data.length - 1; i++) {
			// 如果是16开头
			if (data[i] == '1' && data[i + 1] == '6') {
				// 如果16在报文结尾，返回false
				if (data.length == i + 2) {
					return false;
				}
				//如果不是1668xx68的结构，返回false
				if (i + 9 >= data.length) {
					return false;
				}
				// 如果的确是1668xx68的结构，返回true
				if (data[i + 2] == '6' 
						&& data[i + 3] == '8' 
						&& data[i + 8] == '6' 
						&& data[i + 9] == '8') {
					return true;
				}
			}
		}
		return false;
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
	private void whetherOperateSwitch(char[] data) {

		/*
		 * 81代表终端空闲；80代表拒绝
		 */
		if (data.length > 32) {
			if (CharUtils.equals(CharUtils.subChars(data, 30, 32), CODE_81)) {
				String address = CharUtils.newString(data, BYTE * 5, BYTE * 7).intern();
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
	 * 68535368f4010001c8140101000100010000010000000100000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000000000000000000000d916
	 * 
	 * @param hitchEventDesc
	 *            报警原因描述
	 * @param data
	 */
	private void readAllSignal(String hitchEventDesc, char[] data) {

		String address = CharUtils.newString(data, BYTE * 5, BYTE * 7).intern();
		String id = CtxStore.getIdbyAddress(address);

		if (id != null && address != null) {

			HighVoltageStatus s = hvCtxStore.getStatusbyId(id);
			SwitchGPRS gprs = CtxStore.get(id);
			if (s == null) {

				s = new HighVoltageStatus();
				s.setId(id);
				hvCtxStore.addStatus(s);
			}

			s.setGuo_liu_yi_duan(getStr0Or01(data, 30 + 35 * 2, 30 + 36 * 2));
			s.setGuo_liu_er_duan(getStr0Or01(data, 30 + 36 * 2, 30 + 37 * 2));
			s.setGuo_liu_san_duan(getStr0Or01(data, 30 + 37 * 2, 30 + 38 * 2));
			if (STR_01.equals(s.getGuo_liu_yi_duan()) ||
					STR_01.equals(s.getGuo_liu_er_duan())  ||
					STR_01.equals(s.getGuo_liu_san_duan())) {
				createHitchEvent(id, "过流");
//				hitchReason.append("过流 ");
			}

			String ling_xu_yi_duan = getStr0Or01(data, 30 + 38 * 2, 30 + 39 * 2);
			String ling_xu_er_duan = getStr0Or01(data, 30 + 39 * 2, 30 + 40 * 2);
			String ling_xu_san_duan = getStr0Or01(data, 30 + 40 * 2, 30 + 41 * 2);
			if (STR_01.equals(ling_xu_yi_duan) ||
					STR_01.equals(ling_xu_er_duan)  ||
					STR_01.equals(ling_xu_san_duan)) {
				createHitchEvent(id, "零序过流");
//				hitchReason.append("零序过流 ");
				s.setLing_xu_guo_liu_(STR_01);
			} else {
				s.setLing_xu_guo_liu_(STR_00);
			}
			
			if (CharUtils.equals(data, 40, 42, CODE_01) || CharUtils.equals(data, 42, 44, CODE_01)
					|| CharUtils.equals(data, 44, 46, CODE_01)) {
				s.setChong_he_zha(STR_01);
			} else {
				s.setChong_he_zha(STR_00);
			}

			s.setPt1_you_ya(getStr0Or01(data, 30 + 3 * 2, 30 + 4 * 2));
			s.setPt2_you_ya(getStr0Or01(data, 30 + 7 * 2, 30 + 8 * 2));

			s.setPt1_guo_ya(getStr0Or01(data, 30 + 6 * 2, 30 + 7 * 2));
			s.setPt2_guo_ya(getStr0Or01(data, 30 + 10 * 2, 30 + 11 * 2));
			

			s.setShou_dong_he_zha(getStr0Or01(data, 30 + 20 * 2, 30 + 21 * 2));
			if (STR_01.equals(s.getShou_dong_he_zha())) {
				createHitchEvent(id, "手动合闸");
			}
			s.setShou_dong_fen_zha(getStr0Or01(data, 30 + 21 * 2, 30 + 22 * 2));
			if (STR_01.equals(s.getShou_dong_fen_zha())) {
				createHitchEvent(id, "手动分闸");
			}
			if (STR_01.equals(getStr0Or01(data, 30 + 23 * 2, 30 + 24 * 2))) {
				createHitchEvent(id, "遥控器合闸");
			}
			if (STR_01.equals(getStr0Or01(data, 30 + 24 * 2, 30 + 25 * 2))) {
				createHitchEvent(id, "遥控器分闸");
			}
			s.setYao_kong_he_zha(getStr0Or01(data, 30 + 31 * 2, 30 + 32 * 2));
			if (STR_01.equals(s.getYao_kong_he_zha())) {
				createHitchEvent(id, "遥控合闸");
			}
			s.setYao_kong_fen_zha(getStr0Or01(data, 30 + 32 * 2, 30 + 33 * 2));
			if (STR_01.equals(s.getYao_kong_fen_zha())) {
				createHitchEvent(id, "遥控分闸");
			}
			s.setYao_kong_fu_gui(getStr0Or01(data, 30 + 33 * 2, 30 + 34 * 2));
			if (STR_01.equals(s.getYao_kong_fu_gui())) {
				createHitchEvent(id, "遥控复归");
			}
			if (STR_01.equals(getStr0Or01(data, 30 + 54 * 2, 30 + 55 * 2))) {
				createHitchEvent(id, "快速分闸");
			}
			if (STR_01.equals(getStr0Or01(data, 30 + 58 * 2, 30 + 59 * 2))) {
				createHitchEvent(id, "超温跳闸");
			}
			
			//这里和原来的版本不一样
			String new_status = getStr0Or01(data, 30, 32);
			if ("01".equals(s.getStatus()) && "00".equals(new_status)) {

				gprs.setOpen(true);

				HighVoltageHitchEvent event = new HighVoltageHitchEvent();

				event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				Integer code = CtxStore.selectChangingSwitchStatus(address);
				if (null != code && 0 == code) {
					event.setHitchReason("人工分闸");
					CtxStore.removeChangingSwitch(address);
				} else {
					event.setHitchReason("非人工分闸");
				}
				event.setChangeType(0);
				event.setSolveWay("分闸");
				event.setId(UUIDUtil.getUUID());
				event.setSwitchId(id);
				hitchEventService.insert(event);

				logger.info(address + "-----------跳闸成功");
			} else if ("01".equals(new_status) && "00".equals(s.getStatus())) {

				gprs.setOpen(false);

				HighVoltageHitchEvent event = new HighVoltageHitchEvent();

				event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
				Integer code = CtxStore.selectChangingSwitchStatus(address);
				if (null != code && 1 == code) {
					event.setHitchReason("人工合闸");
					CtxStore.removeChangingSwitch(address);
				} else {
					event.setHitchReason("非人工合闸");
				}
				event.setChangeType(1);
				event.setSolveWay("合闸");
				event.setId(UUIDUtil.getUUID());
				event.setSwitchId(id);
				hitchEventService.insert(event);
				logger.info(address + "-----------合闸成功");
			}
			s.setStatus(new_status);
			// TODO
			s.setJiao_liu_shi_dian(STR_00);
			logger.info("状态变为-----------" + new_status);
			websiteClient.getService().callbackDeviceChange(id, 1);
			websiteClient.getService().callbackCtxChange();
		} else {
			logger.error("there is an error in catching hitch event!");
		}
	}

	/**
	 * 遥测归一值获取（遥测中的变化数据）
	 * 
	 * @param data
	 */
	private void changeMesurementInfo(ChannelHandlerContext ctx, char[] data) {
		
		if (!CharUtils.equals(CharUtils.subChars(data, BYTE, BYTE), CODE_3b)) {
			//遥测变化，格式是 2字节地址 3字节值 2字节地址 3字节值。。。。。
			logger.info("测归一值-------" + String.valueOf(data));
			// 680e0e68f4680009010301680008400000001a16
			// 680e0e68f4ca0009010301ca000a40fbff00da16
			// 680e0e68f4c90009010301c9000a40f0ff00cd16
			// 68131368f46600090203016600054000000006402700008116
			// 68131368f46600090203016600054000000006402700008116
			// 68131368f4710009020301710006402c000008402a0000c916
			// 683b3b68f4660009901401660001404f2c00000000f80400000000280500000000000000000000b61600ebff001b0000000000851300220000140000000000f216 
			// 68131368f46c0009020301 6c000a4074ff000c40baff009d16
			for (int i = 22; i + 14 < data.length; i += 10) {
				getMessageAddress(
						CharUtils.newString(data, i + 4, i + 8),
                        CharUtils.newString(data, 22, 26),
                        CharUtils.newString(data, i + 8, i + 14));
			}
			return;
		}
		logger.info("解析CV---------" + String.valueOf(data));
		String address = CharUtils.newString(data, 10, 14).intern();
		String id = CtxStore.getIdbyAddress(address);
		if (id != null) {
			saveCV(id, String.valueOf(data));
			//	确认链路报文
			ctx.writeAndFlush(Standard101Util.getConfirmFrame(CtxStore.get(ctx).getAddress()));
			//	通知页面设备变化
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

//		for (int i = 26, j = Integer.valueOf(CharUtils.newString(data, 16, 18)); j > 0; i += 6, --j) {
//			changeState(
//					CharUtils.newString(data, 22, 26), 
//					CharUtils.newString(data, i, i + 4),
//					CharUtils.newString(data, i + 4, i + 6));
//		}
//		String resu = new HighVoltageDeviceCommandUtil().confirmChangeAffair(
//				CharUtils.newString(data, BYTE * 5, BYTE * 7));
		String addr = CtxStore.get(ctx).getAddress();
		logger.info("来自" + addr + "遥信变位事件" + String.valueOf(data));
		String resu = Standard101Util.getConfirmFrame(addr);
		logger.info("遥信变位事件确定---------" + resu);
		ctx.writeAndFlush(resu);
	}

	/**
	 * 遥信值的归一值初始获取，遥信值变化时会分两个流程，第一个流程先发一次，若有收到回复，再发一次，整个流程才算结束。
	 * 由于两个流程都带有遥信值，现在第一个流程的遥信值，但考虑安全性，以后取第二个，因为有时间戳
	 * 双点遥信10是合位，01是分位，11是不确定。
	 * TODO 先设置一个标志位，如果有过流一段发生，而且监测到断路器变为分位（一般来回变化），就报警
	 * @param ctx
	 * @param data
	 */
	private void confirmSignalInitialChange(ChannelHandlerContext ctx, char[] data) {
		logger.info("遥信变位：" + String.valueOf(data));
		String iden = String.valueOf(CharUtils.subChars(data, 2 * 13, 2));
		String value = String.valueOf(CharUtils.subChars(data, 2 * 15, 2));
		String address = String.valueOf(CharUtils.subChars(data, 2 * 5, 4));
		HighVoltageStatus s = hvCtxStore.getStatusbyId(CtxStore.get(ctx).getId());
		if (s == null) {
			s = new HighVoltageStatus();
			s.setId(CtxStore.get(ctx).getId());
			hvCtxStore.addStatus(s);
		}
		switch (iden) {
		case "01" :
			//合闸分闸判断位，设备变色
			s.setStatus(getDualPointStr(value));
			Integer code = CtxStore.selectChangingSwitchStatus(address);
			if (null != code && code == Integer.parseInt(s.getStatus())) {
				if (code == 0) {
					createHitchEvent(s.getId(), "人工分闸");
				} else if (code == 1) {
					createHitchEvent(s.getId(), "人工合闸");
				}
				CtxStore.removeChangingSwitch(address);
			}
			websiteClient.getService().callbackCtxChange(); break;
		case "04" : 
			// PT1有压
			s.setPt1_you_ya(getDualPointStr(value)); break;
		case "07" : 
			// PT1过压
			s.setPt1_guo_ya(getDualPointStr(value)); break;
		case "08" : 
			// PT2有压
			s.setPt2_you_ya(getDualPointStr(value)); break;
		case "0b" : 
			// PT2过压
			s.setPt2_guo_ya(getDualPointStr(value)); break;
		case "15" : 
			//	手动合闸
			s.setShou_dong_he_zha(getDualPointStr(value));
			hitchJudgement(ctx, value, iden); break;
		case "16" : 
			//	手动分闸
			s.setShou_dong_fen_zha(getDualPointStr(value)); 
			hitchJudgement(ctx, value, iden); break;
		case "18" : 
			// 遥控器合闸
			hitchJudgement(ctx, value, iden); break;
		case "19" : 
			// 遥控器分闸
			hitchJudgement(ctx, value, iden); break;
		case "1a" : 
			//	遥控器复归
			s.setYao_kong_fu_gui(getDualPointStr(value));  break;
		case "20" : 
			// 遥控合闸
			hitchJudgement(ctx, value, iden); break;
		case "21" : 
			// 遥控分闸
			hitchJudgement(ctx, value, iden); break;
		case "22" : 
			// 遥控复归
			hitchJudgement(ctx, value, iden); break;
		case "24" : 
			//	过流一段
			s.setGuo_liu_yi_duan(getDualPointStr(value)); 
			hitchJudgement(ctx, value, iden); break;
		case "25" : 
			//	过流二段
			s.setGuo_liu_er_duan(getDualPointStr(value)); 
			hitchJudgement(ctx, value, iden); break;
		case "26" : 
			//	过流三段
			s.setGuo_liu_san_duan(getDualPointStr(value)); 
			hitchJudgement(ctx, value, iden); break;
		case "27" : 
			//	零序一段
			s.setLing_xu_guo_liu_(getDualPointStr(value));
			hitchJudgement(ctx, value, iden); break;
		case "28" : 
			//	零序二段
			s.setLing_xu_guo_liu_(getDualPointStr(value));
			hitchJudgement(ctx, value, iden); break;
		case "29" : 
			//	零序三段
			s.setLing_xu_guo_liu_(getDualPointStr(value));
			hitchJudgement(ctx, value, iden); break;
		case "37" : 
			//	快速分闸
			hitchJudgement(ctx, value, iden); break;
		case "3b" : 
			//	超温跳闸
			hitchJudgement(ctx, value, iden); break;
		default : break;
		}
		websiteClient.getService().callbackDeviceChange(CtxStore.get(ctx).getId(), 1);
		String resu = Standard101Util.getConfirmFrame(address);
		logger.info("遥信变位确定---------" + resu);
		ctx.writeAndFlush(resu);
	}

	/**
	 * 遥信变位事件流程，有遥信值
	 * @param address
	 * @param code
	 * @param value
	 */
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
		if (value.equals("02")) {
			value = "01";
		} else {
			value = "00";
		}
	}

	/**
	 * 获取遥测变化值
	 * @param code
	 * @param address
	 * @param value
	 */
	private void getMessageAddress(String code, String address, String value) {
//		System.out.println("code  " + code + "    address  " + address + "  value  " + value);
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
		case "4001" : //UAB
			saveVoltageForValue(CtxStore.getIdbyAddress(address), "A",
					HighVoltageDeviceCommandUtil.changToRight(value));
			break;
		case "4002" :  //UBC
			saveVoltageForValue(CtxStore.getIdbyAddress(address), "B",
					HighVoltageDeviceCommandUtil.changToRight(value));
			break;
		case "4003" : //IA
			saveCurrentForValue(CtxStore.getIdbyAddress(address), "A",
					HighVoltageDeviceCommandUtil.changToRight(value));
			break;
		case "4004" : //IB
			saveCurrentForValue(CtxStore.getIdbyAddress(address), "B",
					HighVoltageDeviceCommandUtil.changToRight(value));
			break;
		case "4005" : //IC
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
			ctx.channel().writeAndFlush(data);
			return;
		}
		String address;
		if (CharUtils.startWith(data, CODE_68)) {
			// 普通报文
			address = CharUtils.newString(data, BYTE * 5, BYTE * 7).intern();
		} else {
			// eb90eb90eb90710016
			address = CharUtils.newString(data, BYTE * 6, BYTE * 8).intern();
		}
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
				/*
				 * 这里的做法忽视了链路建立过程
				 */
//				HighVoltageServer.totalCall(id);
				if (CtxStore.get(id) != null) {
					CtxStore.remove(id);
					CtxStore.add(gprs);
				}
				logger.info("高压设备1.3 " + address + "上线");
				//	设备上线变黄色
				websiteClient.getService().callbackCtxChange();
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
		String ABVoltage = 
				Integer.parseInt(LowVoltageDeviceCommandUtil.reverseStringBy2(data.substring(30, 36)), 16) + "";
		String BCVoltage = 
				Integer.parseInt(LowVoltageDeviceCommandUtil.reverseStringBy2(data.substring(36, 42)), 16) + "";
		String ACurrent = 
				Integer.parseInt(LowVoltageDeviceCommandUtil.reverseStringBy2(data.substring(42, 48)), 16) + "";
		String BCurrent =
				Integer.parseInt(LowVoltageDeviceCommandUtil.reverseStringBy2(data.substring(48, 54)), 16) + "";
		String CCurrent = 
				Integer.parseInt(LowVoltageDeviceCommandUtil.reverseStringBy2(data.substring(54, 60)), 16) + "";

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

	public boolean isSeparated(String data, int length) {
		if (data.length() > length) {

			return false;
		}
		return true;
	}
	
	/**
	 * 复用01和00的String对象，01是合位，00是分位
	 * @param data
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	private String getStr0Or01(char[] data, int beginIndex, int endIndex) {
	    return CharUtils.equals(data, beginIndex, endIndex, CODE_01) ? STR_01 : STR_00;
    }
	
	/**
	 * 判断双点遥信位，01是分位，02是合位，03不确定。
	 * 系统统一判断位，00是分位，01是合位，02不确定。
	 * @param value
	 * @return
	 */
	private String getDualPointStr(String value) {
		switch (value) {
		case "01" : 
			//	分位
			return STR_00;
		case "02" :
			//	合位
			return STR_01;
		case "03" : 
			return STR_02;
		default : return STR_02;
		}
	}
	
	/**
	 * 判断双点遥信是否为合位，是则报警
	 * @param ctx
	 * @param value
	 * @param iden
	 */
	private void hitchJudgement(ChannelHandlerContext ctx, String value, String iden) {
		if (getDualPointStr(value).equals(STR_02)) {
			createHitchEvent(CtxStore.get(ctx).getId(), HitchConst.getHitchReason(iden)); 
		}
	}
	
	private void createHitchEvent(String switchId, String hitchEventDesc) {
		HighVoltageHitchEvent event = new HighVoltageHitchEvent();
		event.setHitchTime(TimeUtil.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
		event.setHitchReason(hitchEventDesc);
		event.setId(UUIDUtil.getUUID());
		event.setSwitchId(switchId);
		hitchEventService.insert(event);
	}
	
//	public static void main(String[] args) {
//		String a = "685d5d68f36800031c030168001b00021b00021800020100022400020100011c00021b00021800020100022400020100011c00022f00021b00021800020100022400020100011c00021b00021900021b00021800020100021c00022700020100016f16";
////		String b = a.substring(30, a.length()-4);
//		String b = a.substring(26, a.length()-4);
////		System.out.println(b);
////		System.out.println(b.substring(35 * 2, 35 * 2 + 12));
//////		String a =	 	Integer.parseInt(LowVoltageDeviceCommandUtil.reverseStringBy2("3c0000"), 16) + "";
//////		System.out.println(a);
//		int i = b.length() / 6;
//		for (int c = 0; c < i; c++) {
//			System.out.println(b.substring(c * 6, c * 6 + 4));
//			System.out.println(b.substring(c * 6 + 4, c * 6 + 6));
//		}
//	}
}
