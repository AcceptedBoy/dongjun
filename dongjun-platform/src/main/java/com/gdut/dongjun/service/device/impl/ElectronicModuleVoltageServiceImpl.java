package com.gdut.dongjun.service.device.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModuleVoltageMapper;
import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.domain.vo.chart.ChartData;
import com.gdut.dongjun.domain.vo.chart.ChartData.ChaseData;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.device.ElectronicModuleVoltageService;

@Service
public class ElectronicModuleVoltageServiceImpl extends EnhancedServiceImpl<ElectronicModuleVoltage> implements ElectronicModuleVoltageService {
 
	@Autowired
	private ElectronicModuleVoltageMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModuleVoltage record) {
		if (record != null && null != record.getId()
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
	
	@Override
	public List<ElectronicModuleVoltage> selectByTime(String deviceId, String beginDate, String endDate, String phase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("deviceId", deviceId);
		map.put("phase", phase);
		return mapper.selectByTime(map);
	}

	@Override
	public void createExcel(ChartData chart, String filePath) {
		List<String> header = new ArrayList<String>();
		header.add("时间");
		header.add("数值");
		List<String> timeList = chart.getxAxis().get(0).getData();
		//生成excel文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		
		for (ChaseData d : chart.getSeries()) {
			List<Float> valueList = d.getData();
			HSSFSheet hssfSheet = hssfWorkbook.createSheet(d.getName());
			
			// 处理excel表头
			HSSFRow r = hssfSheet.createRow(0);
			HSSFCell cell = null;
			for (int i = 0; i < header.size(); i++) {
				cell = r.createCell(i);
				HSSFCellStyle cellStyle2 = hssfWorkbook.createCellStyle();
				HSSFDataFormat format = hssfWorkbook.createDataFormat();
				cellStyle2.setDataFormat(format.getFormat("@"));
				cell.setCellStyle(cellStyle2);

				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(header.get(i));
			}
			
			//如果没有值，跳过这个sheet
			if (null == valueList || 0 == valueList.size()) {
				continue;
			}
			
			//设置值
			HSSFRow row = null;
			int startRow = 1;
			try {
				if (valueList != null) {
					for (int i = startRow; i <= valueList.size(); i++) {
						row = hssfSheet.createRow(i);
						cell = row.createCell(0);
						cell.setCellValue(timeList.get(i - 1));
						cell = row.createCell(1);
						if (null == valueList.get(i - 1)) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(valueList.get(i - 1));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//输出文档
		FileOutputStream fileOut = null;
		try {
			try {
				fileOut = new FileOutputStream(filePath);
				hssfWorkbook.write(fileOut);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}