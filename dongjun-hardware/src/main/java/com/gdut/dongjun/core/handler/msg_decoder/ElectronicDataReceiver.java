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
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.handler.thread.HitchEventManager;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.domain.po.ElectronicModuleHitchEvent;
import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.service.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.ElectronicModuleCurrentService;
import com.gdut.dongjun.service.ElectronicModulePowerService;
import com.gdut.dongjun.service.ElectronicModuleService;
import com.gdut.dongjun.service.ElectronicModuleVoltageService;
import com.gdut.dongjun.service.ModuleHitchEventService;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
public class ElectronicDataReceiver extends ChannelInboundHandlerAdapter {

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
	private DataMonitorSubmoduleService submoduelService;
	@Autowired
	private HitchEventManager eventManager;
	@Autowired
	private ElectronicCtxStore ctxStore;

	private Logger logger = Logger.getLogger(ElectronicDataReceiver.class);

	// ChannelHandlerContext中的Attribute名称
//	private static final String ATTRIBUTE_ELECTRONIC_MODULE = "ELECTRONIC_MODULE";
	private static final String ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED = "ELECTRONIC_MODULE_IS_REGISTED";

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SwitchGPRS gprs = new SwitchGPRS();// 添加ctx到Store中
		gprs.setCtx(ctx);
		ctxStore.add(gprs);
		// CtxStore.setCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE, gprs);
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctxStore.remove(ctx);
//		CtxStore.removeCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE);
		CtxStore.removeCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE_IS_REGISTED);
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		List<Object> list = (List<Object>) msg;
		int type = (int) list.get(0);
		if (!(HitchConst.MODULE_ELECTRICITY == type)) {
			ctx.fireChannelRead(msg);
			return;
		}
		String m = (String) list.get(1);
		char[] data = CharUtils.removeSpace(m.toCharArray());
		logger.info("电能表接收到的信息:" + m);
		if (check(ctx, data)) {
			handleIdenCode(ctx, data);
		}
	}

	/**
	 * 获取当前连接的设备地址
	 * 
	 * @param ctx
	 * @param data
	 */
	private String getOnlineAddress(ChannelHandlerContext ctx, char[] data) {

		// SwitchGPRS gprs = (SwitchGPRS)CtxStore.getCtxAttribute(ctx,
		// ATTRIBUTE_ELECTRONIC_MODULE);
		SwitchGPRS gprs = ctxStore.get(ctx);
		/*
		 * 当注册的温度开关的地址不为空，说明已经注册过了，不再进行相关操作
		 */
		if (gprs != null && gprs.getAddress() != null) {
			ctx.channel().writeAndFlush(data);
			return null;
		}
		// 由于每个字节是两个BCD码构成，所以不用调转地址，转进制啥的了，直接显示
		String address = CharUtils.newString(data, BYTE * 1, BYTE * 7);
		Integer addressTemp = Integer.parseInt(address);
		address = String.valueOf(addressTemp);
		gprs.setAddress(address);

		if (gprs != null) {
			/*
			 * 根据反转后的地址查询得到TemperatureDevice的集合
			 */
			List<ElectronicModule> list = moduleService
					.selectByParameters(MyBatisMapUtil.warp("device_number", address));

			if (list != null && list.size() != 0) {
				ElectronicModule module = list.get(0);
				String id = module.getId();
				gprs.setId(id);

//				CtxStore.setCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE, module);
				return id;
				// if (ctxStore.get(id) != null) {
				// ctxStore.remove(id);
				// ctxStore.add(gprs);
				// }
			} else {
				logger.warn("当前设备未进行注册或者有复数个相同地址的设备");
			}
		}
		return null;
	}

	// TODO
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

	private void handleIdenCode(ChannelHandlerContext ctx, char[] data) {
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

	public void saveVoltage(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = String.valueOf(address);
		deviceNumber = Integer.parseInt(deviceNumber) + "";
//		ElectronicModule module = (ElectronicModule)CtxStore.getCtxAttribute(ctx, ATTRIBUTE_ELECTRONIC_MODULE);
//				moduleService.selectByDeviceNumber(deviceNumber);
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 2);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		ElectronicModuleVoltage voltage = new ElectronicModuleVoltage();
		voltage.setSubmoduleId(ctxStore.getIdbyAddress(deviceNumber));
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

	public void saveCurrent(ChannelHandlerContext ctx, char[] data) {
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
		current.setSubmoduleId(ctxStore.getIdbyAddress(deviceNumber));
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

	public void savePower(ChannelHandlerContext ctx, char[] data) {
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
		power.setSubmoduleId(ctxStore.getIdbyAddress(deviceNumber));
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

	public void handleException(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = String.valueOf(address);
		deviceNumber = Integer.parseInt(deviceNumber) + "";
		ElectronicModule module = moduleService.selectByDeviceNumber(deviceNumber);
		String submoduleId = module.getId();
		List<DataMonitorSubmodule> submodules = submoduelService
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
		ElectronicModuleHitchEvent event = new ElectronicModuleHitchEvent(moduleEvent);
		eventManager.addHitchEvent(event);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

}
