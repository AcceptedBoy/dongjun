package com.gdut.dongjun.core.handler.msg_decoder;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.ElectronicModuleCurrentService;
import com.gdut.dongjun.service.ElectronicModulePowerService;
import com.gdut.dongjun.service.ElectronicModuleService;
import com.gdut.dongjun.service.ElectronicModuleVoltageService;
import com.gdut.dongjun.util.CharUtils;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TemperatureDeviceCommandUtil;
import com.gdut.dongjun.util.UUIDUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 按照协议分类
 * 683100310068C90000000A1C026000000400551618041502010000
 * 请求帧的一种情况
 * 68 地址 68 控制码 数据域长度 数据标识 校验和 16
 * 1   6    1    1       1              4           1        1
 * 读数据的一种情况
 * 68 地址 68 控制码 数据域长度 数据标识	 N1...Nm 校验和 16
 * 1   6    1    1       1              4           N          1        1
 * @author Gordan_Deng
 * @date 2017年4月12日
 */
@Service
@Sharable
public class ElectronicDataReceiver extends ChannelInboundHandlerAdapter {
	
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
	
	private Logger logger = Logger.getLogger(ElectronicDataReceiver.class);
	
	@Autowired
	private ElectronicModuleService moduleService;
	@Autowired
	private ElectronicModuleCurrentService currentService;
	@Autowired
	private ElectronicModuleVoltageService voltageService;
	@Autowired
	private ElectronicModulePowerService powerService;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { }

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String m = (String) msg;
		char[] data = CharUtils.removeSpace(m.toCharArray());
		logger.info("ElectronicDataReceiver接收到的信息:" + m);
		if (check(ctx, data)) {
			
		}
		handleIdenCode(ctx, data);
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
//				TemperatureDevice device = list.get(0);
				String id = module.getId();
				gprs.setId(id);

				if (CtxStore.get(id) != null) {
					CtxStore.remove(id);
					CtxStore.add(gprs);
				}
			} else {
				logger.warn("当前设备未进行注册");
			}
		}
	}
	
	public boolean check(ChannelHandlerContext ctx, char[] data) {
		return false;
	}

	private void handleIdenCode(ChannelHandlerContext ctx, char[] data) {
		char[] address = CharUtils.subChars(data, BYTE, BYTE * 6);
		String deviceNumber = Integer.parseInt(String.valueOf(address), 16) + "";
//		char[] controlCode = CharUtils.subChars(data, BYTE * 8, BYTE);
		char[] identification = CharUtils.subChars(data, BYTE * 10, BYTE * 4);
		
		if (CharUtils.startWith(identification, VOLTAGE)) {
			saveVoltage(ctx, data);
		}
		else if (CharUtils.startWith(identification, CURRENT)) {
			saveCurrent(ctx, data);
		}
		else if (CharUtils.startWith(identification, POWER)) {
			savePower(ctx, data);
		} 
		else if (CharUtils.startWith(identification, EXCEPTION)) {
			handleException(ctx, data);
		}
	}
	
	public void saveVoltage(ChannelHandlerContext ctx, char[] data) {
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 2);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		if (CharUtils.equals(data, BYTE * 12, BYTE * 13, A_PHASE)) {
			ElectronicModuleVoltage voltage = new ElectronicModuleVoltage();
			voltage.setId(UUIDUtil.getUUID());
			voltage.setGroupId();
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, B_PHASE)) {
			logger.info("存储电压B" + val.doubleValue());
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, C_PHASE)) {
			logger.info("存储电压C" + val.doubleValue());
		}
	}
	
	public void saveCurrent(ChannelHandlerContext ctx, char[] data) {
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 3);
		
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		if (CharUtils.equals(data, BYTE * 12, BYTE * 13, A_PHASE)) {
			logger.info("存储电流" + val.doubleValue());
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, B_PHASE)) {
			logger.info("存储电流B" + val.doubleValue());
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, C_PHASE)) {
			logger.info("存储电流C" + val.doubleValue());
		}
	}
	
	public void savePower(ChannelHandlerContext ctx, char[] data) {
		char[] value = CharUtils.subChars(data, BYTE * 14, BYTE * 3);
		BigDecimal val = new BigDecimal(Double.valueOf(Integer.parseInt(String.valueOf(value), 16)) / 10);
		if (CharUtils.equals(data, BYTE * 12, BYTE * 13, A_PHASE)) {
			logger.info("存储功率A" + val.doubleValue());
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, B_PHASE)) {
			logger.info("存储功率B" + val.doubleValue());
		} else if (CharUtils.equals(data, BYTE
				* 12, BYTE * 13, C_PHASE)) {
			logger.info("存储功率C" + val.doubleValue());
		} else if (CharUtils.equals(data, BYTE * 12, BYTE * 13, CODE_00)) {
			logger.info("存储总功率因数" + val.doubleValue());
		}
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
