package com.gdut.dongjun.service.device.event.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureMeasureHitchEventMapper;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.web.vo.TemperatureMeasureHitchEventVO;

@Service
public class TemperatureMeasureHitchEventServiceImpl extends EnhancedServiceImpl<TemperatureMeasureHitchEvent>
		implements TemperatureMeasureHitchEventService{

	@Autowired
	private TemperatureMeasureHitchEventMapper mapper;
	@Autowired
	private ModuleHitchEventService moduleHitchService;
	@Autowired
	private TemperatureModuleService moduleService;
	
	@Override
	protected boolean isExist(TemperatureMeasureHitchEvent record) {
		if (record != null && null != record.getId()
				&& mapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<TemperatureMeasureHitchEventVO> selectMeasureHitch(String companyId) {
		//搜索type为301的报警事件
		List<ModuleHitchEvent> list = moduleHitchService.selectByType(301, 302, companyId);
		List<TemperatureMeasureHitchEventVO> dtoList = new ArrayList<TemperatureMeasureHitchEventVO>();
		for (ModuleHitchEvent e : list) {
			TemperatureMeasureHitchEvent event = 
					mapper.selectByParameters(MyBatisMapUtil.warp("hitch_id", e.getId())).get(0);
			TemperatureMeasureHitchEventVO dto = new TemperatureMeasureHitchEventVO(e, event);
			TemperatureModule temModule = moduleService.selectByPrimaryKey(e.getModuleId());
			if (null == temModule) {
				dto.setName("该设备已被删除");
			} else {
				dto.setName(temModule.getName());
			}
			dtoList.add(dto);
		}
		return dtoList;
	}

}
