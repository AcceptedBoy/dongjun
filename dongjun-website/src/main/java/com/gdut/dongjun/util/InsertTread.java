package com.gdut.dongjun.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.domain.po.HistoryHighVoltageCurrent;
import com.gdut.dongjun.domain.po.HistoryHighVoltageVoltage;
import com.gdut.dongjun.service.HighVoltageCurrentService;
import com.gdut.dongjun.service.HighVoltageVoltageService;
import com.gdut.dongjun.service.HistoryHighVoltageCurrentService;
import com.gdut.dongjun.service.HistoryHighVoltageVoltageService;

/**
 * 循环插入数据库线程
 * @author yzh
 *
 */
//@Component
public class InsertTread implements Runnable{
//	@Autowired
//	public HistoryHighVoltageCurrentService HistoryCurrentService;
//	@Autowired
//	public HistoryHighVoltageVoltageService HistoryVoltageService;
//	@Autowired
//	private HighVoltageCurrentService currentService2;
//	@Autowired
//	private HighVoltageVoltageService voltageService2;
//	@Autowired
//	
//	@Override
//	public void run() {
//		System.out.println("进入线程");
//			Date a=new Date();
//			for(int i=0;i<100;i++){
//				currentService2.deleteByPrimaryKey(i+"0");
//				currentService2.deleteByPrimaryKey(i+"1");
//				currentService2.deleteByPrimaryKey(i+"2");
//				voltageService2.deleteByPrimaryKey(i+"0");
//				voltageService2.deleteByPrimaryKey(i+"1");
//			}
//			String id=new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
//			for(int i=0;i<100;i++){
//				currentService2.insert(new HighVoltageCurrent(i+"0",a,"A",i,i+""));
//				currentService2.insert(new HighVoltageCurrent(i+"1",a,"B",i,i+""));
//				currentService2.insert(new HighVoltageCurrent(i+"2",a,"C",i,i+""));
//				HistoryCurrentService.insert(new HistoryHighVoltageCurrent(id+i+"1",a,"A",i,i+""));
//				HistoryCurrentService.insert(new HistoryHighVoltageCurrent(id+i+"2",a,"B",i,i+""));
//				HistoryCurrentService.insert(new HistoryHighVoltageCurrent(id+i+"3",a,"C",i,i+""));
//			}
//			for(int i=0;i<100;i++){
//				voltageService2.insert(new HighVoltageVoltage(i+"0",a,"B",i,i+""));
//				voltageService2.insert(new HighVoltageVoltage(i+"1",a,"A",i,i+""));
//				HistoryVoltageService.insert(new HistoryHighVoltageVoltage(id+i+"1",a,"B",i,i+""));
//				HistoryVoltageService.insert(new HistoryHighVoltageVoltage(id+i+"2",a,"A",i,i+""));
//			}
//			System.out.println("一次插入完成");
//	}

}
