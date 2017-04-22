package com.gdut.dongjun.service.device.impl;

import com.gdut.dongjun.service.device.*;
import com.gdut.dongjun.service.device.current.ControlMearsureCurrentService;
import com.gdut.dongjun.service.device.current.DeviceCurrentService;
import com.gdut.dongjun.service.device.current.HighVoltageCurrentService;
import com.gdut.dongjun.service.device.current.LowVoltageCurrentService;
import com.gdut.dongjun.service.device.voltage.ControlMearsureVoltageService;
import com.gdut.dongjun.service.device.voltage.DeviceVoltageService;
import com.gdut.dongjun.service.device.voltage.HighVoltageVoltageService;
import com.gdut.dongjun.service.device.voltage.LowVoltageVoltageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 */
@Service
@ConfigurationProperties(prefix = "protocol")
public class DeviceCommonServiceImpl implements DeviceCommonService {

    @Autowired
    private HighVoltageSwitchService hvSwitchService;

    @Autowired
    private LowVoltageSwitchService lvSwitchService;

    @Autowired
    private ControlMearsureSwitchService controlSwitchService;

    @Autowired
    private HighVoltageCurrentService hvCurrentService;

    @Autowired
    private LowVoltageCurrentService lvCurrentService;

    @Autowired
    private ControlMearsureCurrentService controlCurrentService;

    @Autowired
    private HighVoltageVoltageService hvVoltageService;

    @Autowired
    private LowVoltageVoltageService lvVoltageService;

    @Autowired
    private ControlMearsureVoltageService controlVoltageService;

    private int lowVoltage = 0;

    private int highVoltage = 1;

    private int controlVoltage = 2;

    @Override
    public DeviceService getDeviceService(int type) {
        if(type == lowVoltage) {
            return lvSwitchService;
        }
        if(type == highVoltage) {
            return hvSwitchService;
        }
        if(type == controlVoltage) {
            return controlSwitchService;
        }
        return null;
    }

    @Override
    public DeviceCurrentService getCurrentService(int type) {
        if(type == lowVoltage) {
            return lvCurrentService;
        }
        if(type == highVoltage) {
            return hvCurrentService;
        }
        if(type == controlVoltage) {
            return controlCurrentService;
        }
        return null;
    }

    public DeviceVoltageService getVoltageService(int type) {
        if(type == lowVoltage) {
            return lvVoltageService;
        }
        if(type == highVoltage) {
            return hvVoltageService;
        }
        if(type == controlVoltage) {
            return controlVoltageService;
        }
        return null;
    }

    public int getLowVoltage() {
        return lowVoltage;
    }

    public void setLowVoltage(int lowVoltage) {
        this.lowVoltage = lowVoltage;
    }

    public int getHighVoltage() {
        return highVoltage;
    }

    public void setHighVoltage(int highVoltage) {
        this.highVoltage = highVoltage;
    }

    public int getControlVoltage() {
        return controlVoltage;
    }

    public void setControlVoltage(int controlVoltage) {
        this.controlVoltage = controlVoltage;
    }
}
