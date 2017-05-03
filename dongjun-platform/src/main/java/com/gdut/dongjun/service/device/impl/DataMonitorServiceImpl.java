package com.gdut.dongjun.service.device.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.DataMonitorMapper;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.UserDeviceMapping;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Service
public class DataMonitorServiceImpl extends EnhancedServiceImpl<DataMonitor> 
implements DataMonitorService {

	@Autowired
	private DataMonitorMapper mapper;
	@Autowired
	private UserDeviceMappingService mappingService;
	
	@Override
	protected boolean isExist(DataMonitor record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}

	/*
	 * 优化sql
	 * @see com.gdut.dongjun.service.device.DataMonitorService#selectByUserMapping(com.gdut.dongjun.domain.po.User)
	 */
	@Override
	public List<DataMonitor> selectByUserMapping(User user) {
		List<UserDeviceMapping> mappings = mappingService.selectByParameters(MyBatisMapUtil.warp("user_id", user.getId()));
		List<DataMonitor> list = new ArrayList<DataMonitor>();
		for (UserDeviceMapping mapping : mappings) {
			list.add(mapper.selectByPrimaryKey(mapping.getDeviceId()));
		}
		return list;
	}

}
