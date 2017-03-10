package com.gdut.dongjun.service.device.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureDeviceMapper;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.service.device.TemperatureDeviceService;
import com.gdut.dongjun.util.ExcelUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Service
public class TemperatureDeviceServiceImpl extends BaseServiceImpl<TemperatureDevice> 
		implements TemperatureDeviceService {
	
	@Autowired
	private TemperatureDeviceMapper mapper;

	@Override
	protected boolean isExist(TemperatureDevice record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}

	@Override
	public boolean createDeviceExcel(String filePath, List<TemperatureDevice> object) {
		List<String> headList = createHeadList();// 生成表头
		Map<String, String> map = createHeadListMap();// 生成映射关系
		ExcelUtil poi = new ExcelUtil();
		try {
			poi.exportExcel("Sheet1", filePath, map, headList, object, 1,
					TemperatureDevice.class);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean uploadDevice(String realPath, String platformId) {

		List<String> headList = createHeadList();// 生成表头
		Map<String, String> map = createHeadListMap();// 生成映射关系
		ExcelUtil poi = new ExcelUtil();

		List<TemperatureDevice> result = new LinkedList<>();
		// 2.解析Excel内容
		try {
			result = poi.importExcel("Sheet1", realPath, map, headList,
					TemperatureDevice.class);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (TemperatureDevice t : result) {
			
			TemperatureDevice device = new TemperatureDevice();
			device.setId(UUIDUtil.getUUID());
			device.setAddress(t.getAddress());
			device.setDeviceNumber(t.getDeviceNumber());
			device.setGroupId(platformId);
			device.setSimNumber(t.getSimNumber());
			device.setName(t.getName());
			device.setAddTime(format.format(new Date()));
			mapper.insertSelective(device);
		}
		return true;
	}
	
	/**
	 * 声明excel表的列头
	 * 
	 * @return
	 */
	private List<String> createHeadList() {

		List<String> headList = new ArrayList<String>();
		headList.add("设备号码");
		headList.add("名称");
		headList.add("地址");
		headList.add("SIM号");
		return headList;
	}
	
	/**
	 * 声明列头与数据集合的对应关系
	 * TODO
	 * @return
	 */
	private Map<String, String> createHeadListMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("设备号码", "deviceNumber");
		map.put("名称", "name");
		map.put("地址", "address");
		map.put("SIM号", "simNumber");
		return map;
	}

	@Override
	public List<TemperatureDevice> selectDeviceByIds(List<String> ids) {
		return mapper.selectDeviceByIds(ids);
	}

	@Override
	public String selectNameById(String id) {
		return mapper.selectNameById(id);
	}

	
}
