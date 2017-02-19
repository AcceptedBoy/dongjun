package com.gdut.dongjun.service.device.current;

import com.gdut.dongjun.service.base.BaseService;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 */
public interface DeviceCurrentService<T> extends BaseService<T> {

    public List<Integer> readCurrent(String switchId);
}
