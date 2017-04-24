package com.gdut.dongjun.service.device;

import java.util.List;

import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.base.EnhancedService;

public interface DataMonitorService extends
EnhancedService<DataMonitor> {

	public List<DataMonitor> selectByUserMapping(User user);
}
