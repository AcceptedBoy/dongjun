package com.gdut.dongjun.service.device.voltage;

import com.gdut.dongjun.service.base.BaseService;

import java.util.List;

/**
 * Created by symon on 16-11-30.
 */
public interface DeviceVoltageService<T> extends BaseService<T> {

    public List<Integer> getVoltage(String switchId);
}
