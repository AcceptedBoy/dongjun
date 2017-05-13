package com.gdut.dongjun.core.handler.msg_decoder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.domain.po.ElectronicModuleHitchEvent;
import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.service.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.ElectronicModuleCurrentService;
import com.gdut.dongjun.service.ElectronicModuleHitchEventService;
import com.gdut.dongjun.service.ElectronicModulePowerService;
import com.gdut.dongjun.service.ElectronicModuleService;
import com.gdut.dongjun.service.ElectronicModuleVoltageService;
import com.gdut.dongjun.service.ModuleHitchEventService;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

/**
 * <p>
 * 按照协议分类 68/3100310068C90000000A1C026000000400551618041502010000 请求帧的一种情况 68 地址
 * 68 控制码 数据域长度 数据标识 校验和 16 1 6 1 1 1 4 1 1 读数据的一种情况 68 地址 68 控制码 数据域长度 数据标识
 * N1...Nm 校验和 16 1 6 1 1 1 4 N 1 1 地址6个字节，每个字节两个BCD码，总共12个十进制数
 * </p>
 * 
 * @author Gordan_Deng
 * @date 2017年4月12日
 */
@Service
@Sharable
public class ElectronicDataReceiver extends AbstractDataReceiver {

	// TODO 考虑下要不要将ElectronicModule直接塞到Attribute里面
	private static final int BYTE = 2;
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
	private DataMonitorSubmoduleService submoduleService;
	@Autowired
	private ElectronicCtxStore ctxStore;
	@Autowired
	private ElectronicModuleHitchEventService hitchEventService;
	@Autowired
	private WebsiteServiceClient websiteService;
	
	// ChannelHandlerContext中的Attribute名称
	public static final String ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED = "ELECTRONIC_MODULE_IS_REGISTED";

	public ElectronicDataReceiver() {
		super(HitchConst.MODULE_ELECTRICITY, Logger.getLogger(ElectronicDataReceiver.class));
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		SwitchGPRS gprs = new SwitchGPRS();// 添加ctx到Store中
//		gprs.setCtx(ctx);
//		ctxStore.add(gprs);
		
//		ChannelInfo info = ctxStore.get(ctx.channel());
//		if (null != info) {
//			info.setCtx(ctx);
//		}
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//		ctxStore.remove(ctx);
		ctxStore.remove(ctx);
		CtxStore.removeCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED);
		super.channelInactive(ctx);
	}

	/**
	 * 获取当前连接的设备地址
	 * 
	 * @param ctx
	 * @param data
	 */
//	@Override
//	protected String getOnlineAddress(ChannelHandlerContext ctx, char[] data) {
//		
//		ChannelInfo info = ctxStore.get(ctx);
//		
//		if (null != info && null != info.getAddress()) {
//			return info.getModuleId();
//		}
//		//如果十进制地址和数据库中的十进制地址一样，认为是同一个设备
//		// 由于每个字节是两个BCD码构成，所以不用调转地址，转进制啥的了，直接显示
//		String address = CharUtils.newString(data, BYTE * 1, BYTE * 7);
//		Integer addressTemp = Integer.parseInt(address);
//		String decimalAddress = String.valueOf(addressTemp);
//		ChannelInfo channelInfo = ctxStore.getChannelInfobyAddress(decimalAddress);
//		if (null != channelInfo) {
//			channelInfo.setCtx(ctx);
//			channelInfo.setAddress(address);
//		} else {
//			logger.info("网站的子模块地址配置与实际接收到的地址不符，网站的子模块地址为" + info.getDecimalAddress());
//			return null;
//		}
//		//TODO
//		ChannelHandlerManager.addCtx(info.getMonitorId(), ctx);
//		return info.getModuleId();
//
//		// SwitchGPRS gprs = (SwitchGPRS)CtxStore.getCtxAttribute(ctx,
//		// ATTRIBUTE_ELECTRONIC_MODULE);
////		SwitchGPRS gprs = ctxStore.get(ctx);
////		/*
////		 * 当注册的温度开关的地址不为空，说明已经注册过了，不再进行相关操作
////		 */
////		if (gprs != null && gprs.getAddress() != null) {
////			ctx.channel().writeAndFlush(data);
////			return null;
////		}
////		// 由于每个字节是两个BCD码构成，所以不用调转地址，转进制啥的了，直接显示
////		String address = CharUtils.newString(data, BYTE * 1, BYTE * 7);
////		Integer addressTemp = Integer.parseInt(address);
////		address = String.valueOf(addressTemp);
////		gprs.setAddress(address);
////
////		if (gprs != null) {
////			/*
////			 * 根据反转后的地址查询得到TemperatureDevice的集合
////			 */
////			List<ElectronicModule> list = moduleService
////					.selectByParameters(MyBatisMapUtil.warp("device_number", address));
////
////			if (list != null && list.size() != 0) {
////				ElectronicModule module = list.get(0);
////				String id = module.getId();
////				gprs.setId(id);
////				List<DataMonitorSubmodule> submodules = submoduleService.selectByParameters(MyBatisMapUtil.warp("module_id", module.getId()));
////				ChannelHandlerManager.addCtx(submodules.get(0).getDataMonitorId(), ctx);
////				return id;
////				// if (ctxStore.get(id) != null) {
////				// ctxStore.remove(id);
////				// ctxStore.add(gprs);
////				// }
////			} else {
////				logger.warn("当前设备未进行注册或者有复数个相同地址的设备");
////			}
////		}
////		return null;
//	}

	// TODO
	@Override
	public boolean check(ChannelHandlerContext ctx, char[] data) {

		Integer isRegisted = (Integer) CtxStore.getCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED);
		if (null == isRegisted) {
			// 68开头16结尾的报文
			if (null != getOnlineAddress(ctx, data)) {
				CtxStore.setCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED, new Integer(1));
			}
		}
		return true;
	}

	@Override
	protected void channelReadInternal(ChannelHandlerContext ctx, char[] data) {
		// 登录包、心跳包，或长度过少的报文，忽略
		if (data.length < 10) {
			logger.info("忽略报文" + String.valueOf(data));
			return;
		}

		if (CharUtils.startWith(data, EB_UP) || CharUtils.startWith(data, EB_DOWN)) {
			// TODO
		}

		// char[] controlCode = CharUtils.subChars(data, BYTE * 8, BYTE);
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
	}

	private void saveVoltage(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = String.valueOf(address);
		deviceNumber = Integer.parseInt(deviceNumber) + "";
//		ElectronicModule module = (ElectronicModule)CtxStore.getCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE);
//				moduleService.selectByDeviceNumber(deviceNumber);
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
//		ElectronicModule module = moduleService.selectByDeviceNumber(deviceNumber);
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
//		ElectronicModule module = moduleService.selectByDeviceNumber(deviceNumber);
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
		List<DataMonitorSubmodule> submodules = submoduleService
				.selectByParameters(MyBatisMapUtil.warp("module_id", submoduleId));
		if (submodules.size() == 0) {
			return;
		}
		String monitorId = submodules.get(0).getDataMonitorId();
		ModuleHitchEvent moduleEvent = new ModuleHitchEvent();
		moduleEvent.setId(UUIDUtil.getUUID());
		moduleEvent.setGroupId(module.getGroupId());
		moduleEvent.setHitchTime(new Date());
		moduleEvent.setModuleId(module.getId());
		moduleEvent.setMonitorId(monitorId);
		char[] hitchReason = CharUtils.subChars(data, BYTE * 14, BYTE * 2);
		// 失压 201
		if (CharUtils.equals(hitchReason, SHI_YA)) {
			moduleEvent.setType(201);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(201));
		}
		// 欠压 202
		else if (CharUtils.equals(hitchReason, QIAN_YA)) {
			moduleEvent.setType(202);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(202));
		}
		// 过压 203
		else if (CharUtils.equals(hitchReason, GUO_YA)) {
			moduleEvent.setType(203);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(203));
		}
		// 断相 204
		else if (CharUtils.equals(hitchReason, DUAN_XIANG)) {
			moduleEvent.setType(204);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(204));
		}
		// 全失压 205
		else if (CharUtils.equals(hitchReason, QUAN_SHI_YA)) {
			moduleEvent.setType(205);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(205));
		}
		// 失流 206
		else if (CharUtils.equals(hitchReason, SHI_LIU_UP) || CharUtils.equals(hitchReason, SHI_LIU_DOWN)) {
			moduleEvent.setType(206);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(206));
		}
		// 过流 207
		else if (CharUtils.equals(hitchReason, GUO_LIU_UP) || CharUtils.equals(hitchReason, GUO_LIU_DOWN)) {
			moduleEvent.setType(207);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(207));
		}
		// 断流 208
		else if (CharUtils.equals(hitchReason, DUAN_LIU_UP) || CharUtils.equals(hitchReason, DUAN_LIU_DOWN)) {
			moduleEvent.setType(208);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(208));
		}
		// 未知报警 200
		else {
			moduleEvent.setType(200);
			moduleEvent.setHitchReason(HitchConst.getHitchReason(200));
		}
		moduleHitchEventService.updateByPrimaryKey(moduleEvent);
		ElectronicModuleHitchEvent event = new ElectronicModuleHitchEvent();
		event.setId(UUIDUtil.getUUID());
		event.setHitchId(moduleEvent.getId());
		//TODO
		hitchEventService.updateByPrimaryKey(event);
		HitchEventDTO dto = new HitchEventDTO();
		dto.setId(moduleEvent.getId());
		ChannelInfo info = ctxStore.get(ctx);
		dto.setGroupId(info.getGroupId());
		dto.setModuleId(info.getModuleId());
		dto.setMonitorId(info.getMonitorId());
		dto.setType(moduleEvent.getType());
		websiteService.getService().callbackHitchEvent(dto);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage());
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//		ctxStore.remove(ctx);
		ctxStore.remove(ctx);
		CtxStore.removeCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED);
		super.handlerRemoved(ctx);
	}

	@Override
	protected String getAddress(char[] data) {
		return CharUtils.newString(data, BYTE * 1, BYTE * 7);
	}

	@Override
	protected String getDecimalAddress(char[] data) {
		String address = getAddress(data);
		int i = 0;
		for (; ; i++) {
			char ch = address.charAt(i);
			if (!('a' == ch || 'A' == ch)) {
				break;
			}
		}
		return TemperatureDeviceCommandUtil.reverseString(address.substring(i, address.length() - 1));
	}
	

}
