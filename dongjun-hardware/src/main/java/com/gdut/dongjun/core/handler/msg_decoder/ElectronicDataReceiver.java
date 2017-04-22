package com.gdut.dongjun.core.handler.msg_decoder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.service.ElectronicModuleCurrentService;
import com.gdut.dongjun.service.ElectronicModulePowerService;
import com.gdut.dongjun.service.ElectronicModuleService;
import com.gdut.dongjun.service.ElectronicModuleVoltageService;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 按照协议分类
 * 683100310068C90000000A1C026000000400551618041502010000
 * 请求帧的一种情况
 * 68 地址 68 控制码 数据域长度 数据标识 校验和 16
 * 1   6    1    1       1              4           1        1
 * 读数据的一种情况
 * 68 地址 68 控制码 数据域长度 数据标识	 N1...Nm 校验和 16
 * 1   6    1    1       1              4           N          1        1
 * 地址6个字节，每个字节两个BCD码，总共12个十进制数
 * @author Gordan_Deng
 * @date 2017年4月12日
 */
@Service
@Sharable
public class ElectronicDataReceiver extends ChannelInboundHandlerAdapter {
	
	//TODO 考虑下要不要将ElectronicModule直接塞到Attribute里面
	private static final int BYTE = 2;
	private static final char[] VOLTAGE = {'0', '2', '0', '1'};
	private static final char[] CURRENT = {'0', '2', '0', '2'};
	private static final char[] POWER = {'0', '2', '0', '6'};
	private static final char[] EXCEPTION = {'0', '3'};
	private static final char[] A_PHASE = {'0', '1'};
	private static final char[] B_PHASE = {'0', '2'};
	private static final char[] C_PHASE = {'0', '3'};
	private static final char[] CODE_00 = {'0', '0'};
	private static final char[] DATA_BLOCK_UP = {'F', 'F'};
	private static final char[] DATA_BLOCK_DOWN = {'f', 'f'};
	private static final char[] CODE_01 = {'0', '1'};
	private static final char[] CODE_03 = {'0', '3'};
	private static final char[] HITCH_VOLTAGE = {}; //TODO
	
	private Logger logger = Logger.getLogger(ElectronicDataReceiver.class);
	
	@Autowired
	private ElectronicModuleService moduleService;
	@Autowired
	private ElectronicModuleCurrentService currentService;
	@Autowired
	private ElectronicModuleVoltageService voltageService;
	@Autowired
	private ElectronicModulePowerService powerService;
	@Autowired
	private ElectronicCtxStore ctxStore;
	@Autowired
	private GPRSModuleService gprsService;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { 
		SwitchGPRS gprs = new SwitchGPRS();// 添加ctx到Store中
		gprs.setCtx(ctx);
		if (ctxStore.get(ctx) == null) {
			ctxStore.add(gprs);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String m = (String) msg;
		char[] data = CharUtils.removeSpace(m.toCharArray());
		logger.info("ElectronicDataReceiver接收到的信息:" + m);
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
	private void getOnlineAddress(ChannelHandlerContext ctx, char[] data) {

		SwitchGPRS gprs = ctxStore.get(ctx);
		/*
		 * 当注册的温度开关的地址不为空，说明已经注册过了，不再进行相关操作
		 */
		if (gprs != null && gprs.getAddress() != null) {
			ctx.channel().writeAndFlush(data);
			return;
		}
		//由于每个字节是两个BCD码构成，所以不用调转地址，转进制啥的了，直接显示
		String address = CharUtils.newString(data, BYTE * 1, BYTE * 7).intern();
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

				if (ctxStore.get(id) != null) {
					ctxStore.remove(id);
					ctxStore.add(gprs);
				}
			} else {
				logger.warn("当前设备未进行注册");
			}
		}
	}
	
	//TODO
	public boolean check(ChannelHandlerContext ctx, char[] data) {
		String gprsAddress = null;
		// 登录和心跳包
		if (CharUtils.startWith(data, CODE_00)
				&& (CharUtils.equals(data, 6, 8, CODE_01) || CharUtils.equals(data, 6, 8, CODE_03))) {
			char[] gprsNumber = CharUtils.subChars(data, 12, 8);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= 6; i += 2) {
				//示范 30 30 30 31，表示0001地址
				sb.append(gprsNumber[i + 1]);
			}
			//去除开头的0
			gprsAddress = sb.toString();
			
			//判断GPRS是否已在网站上注册
			String gprsId = gprsService.isGPRSAvailable(gprsAddress);
			if (null != gprsId) {
				
				AttributeKey<Integer> key = AttributeKey.valueOf("isGPRSRegisted");
				Attribute<Integer> attr = ctx.attr(key);
				if (null == attr.get()) {
					attr.set(1);
				}
				//更新TemperatureCtxStore的GPRSMap，gprsId为空的话啥都不干。
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
		getOnlineAddress(ctx, data);
		return true;
	}

	private void handleIdenCode(ChannelHandlerContext ctx, char[] data) {
//		char[] controlCode = CharUtils.subChars(data, BYTE * 8, BYTE);
		char[] identification = CharUtils.subChars(data, BYTE * 10, BYTE * 4);
		
		//电压
		if (CharUtils.startWith(identification, VOLTAGE)) {
			saveVoltage(ctx, data);
		}
		//电流
		else if (CharUtils.startWith(identification, CURRENT)) {
			saveCurrent(ctx, data);
		}
		//功率
		else if (CharUtils.startWith(identification, POWER)) {
			savePower(ctx, data);
		} 
		//异常
		else if (CharUtils.startWith(identification, EXCEPTION)) {
			handleException(ctx, data);
		}
	}
	
	public void saveVoltage(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = String.valueOf(address);
		ElectronicModule module = moduleService.selectByDeviceNumber(deviceNumber);
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 2);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		ElectronicModuleVoltage voltage = new ElectronicModuleVoltage();
		voltage.setId(UUIDUtil.getUUID());
		voltage.setGmtCreate(new Date());
		voltage.setGmtModified(new Date());
		voltage.setGroupId(module.getGroupId());
		voltage.setTime(new Date()); 	//TODO
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
		ElectronicModule module = moduleService.selectByDeviceNumber(deviceNumber);
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 3);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		ElectronicModuleCurrent current = new ElectronicModuleCurrent();
		current.setId(UUIDUtil.getUUID());
		current.setGmtCreate(new Date());
		current.setGmtModified(new Date());
		current.setGroupId(module.getGroupId());
		current.setTime(new Date());//TODO
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
		ElectronicModule module = moduleService.selectByDeviceNumber(deviceNumber);
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 3);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		ElectronicModulePower power = new ElectronicModulePower();
		power.setId(UUIDUtil.getUUID());
		power.setGmtCreate(new Date());
		power.setGmtModified(new Date());
		power.setGroupId(module.getGroupId());
		power.setTime(new Date());//TODO
		power.setValue(val);
		if (CharUtils.equals(data, BYTE * 12, BYTE * 13, A_PHASE)) {
			power.setPhase("A");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, B_PHASE)) {
			power.setPhase("B");
		} else if (CharUtils.equals(data, BYTE* 12, BYTE * 13, C_PHASE)) {
			power.setPhase("C");
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, CODE_00)) {
			power.setPhase("D");
		}
		powerService.updateByPrimaryKey(power);
	}
	
	public void handleException(ChannelHandlerContext ctx, char[] data) {
		logger.info("异常事件");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
	
}
