package com.gdut.dongjun.core.device_message_engine.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.device_message_engine.ControlMessageEngine;
import com.gdut.dongjun.core.device_message_engine.DataMessageEngine;
import com.gdut.dongjun.core.device_message_engine.EventMessageEngine;
import com.gdut.dongjun.enums.HighCommandControlCode;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;

/**
 * @author Sherlock-lee
 * @date 2015年11月11日 下午3:19:32
 * @see TODO
 * @since 1.0
 */
@Service("HighVoltageSwitchMessageEngine")
public class HighVoltageSwitchMessageEngine implements ControlMessageEngine,
		DataMessageEngine, EventMessageEngine {

	private HighVoltageDeviceCommandUtil util = new HighVoltageDeviceCommandUtil();
	
	public String generatePreparedCloseSwitchMesg(String address) {
		
		return util.closeSwitchPre(address,
				HighCommandControlCode.PRE_CLOSE_SWITCH.toString());
	}

	@Override
	public String generateCloseSwitchMessage(String address) {

		Logger.getLogger(HighVoltageSwitchMessageEngine.class).info("执行合闸");
		return util.closeSwitch(address,
				HighCommandControlCode.CLOSE_SWITCH.toString())
				+ util.readVoltageAndCurrent(address,
				HighCommandControlCode.READ_VOLTAGE_CURRENT.toString());
	}
	
	public String generatePreparedOpenSwitchMesg(String address) {
		
		return util.openSwitchPre(address,
				HighCommandControlCode.PRE_OPEN_SWITCH.toString());
	}

	@Override
	public String generateOpenSwitchMessage(String address) {

		Logger.getLogger(HighVoltageSwitchMessageEngine.class).info("执行分闸");
		return util.openSwitch(address,
						HighCommandControlCode.OPEN_SWITCH.toString())
				+ util.readVoltageAndCurrent(address,
						HighCommandControlCode.READ_VOLTAGE_CURRENT.toString());
	}

	@Override
	public String generateReadAPhaseCurrentMessage(String address) {
		return util.readVoltageAndCurrent(address,
				HighCommandControlCode.READ_VOLTAGE_CURRENT.toString());
	}

	@Override
	public String generateReadBPhaseCurrentMessage(String address) {
		return util.readVoltageAndCurrent(address,
				HighCommandControlCode.READ_VOLTAGE_CURRENT.toString());
	}

	@Override
	public String generateReadCPhaseCurrentMessage(String address) {
		return util.readVoltageAndCurrent(address,
				HighCommandControlCode.READ_VOLTAGE_CURRENT.toString());
	}

	@Override
	public String generateReadAPhaseVoltageMessage(String address) {
		return util.readVoltageAndCurrent(address,
				HighCommandControlCode.READ_VOLTAGE_CURRENT.toString());
	}

	@Override
	public String generateReadBPhaseVoltageMessage(String address) {
		return util.readVoltageAndCurrent(address,
				HighCommandControlCode.READ_VOLTAGE_CURRENT.toString());
	}

	@Override
	public String generateReadCPhaseVoltageMessage(String address) {
		return util.readVoltageAndCurrent(address,
				HighCommandControlCode.READ_VOLTAGE_CURRENT.toString());
	}

	@Override
	public String generateReadHitchEventMessage(String address) {
		return null;
	}
}
