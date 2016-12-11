package com.gdut.dongjun.service.device;

import com.gdut.dongjun.service.device.current.DeviceCurrentService;
import com.gdut.dongjun.service.device.voltage.DeviceVoltageService;

/**
 * Created by symon on 16-11-30.
 */
public interface DeviceCommonService {

    public DeviceService getDeviceService(int type);

    public DeviceCurrentService getCurrentService(int type);

    public DeviceVoltageService getVoltageService(int type);
}
