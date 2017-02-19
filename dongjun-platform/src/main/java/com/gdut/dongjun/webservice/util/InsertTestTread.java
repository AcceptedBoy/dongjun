package com.gdut.dongjun.webservice.util;

/**
 * 循环插入数据库线程
 * @author yzh
 *
 */

public class InsertTestTread implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
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
//			Date a=new Date();
//			/*for(int i=0;i<100;i++){
//				currentService2.deleteByPrimaryKey(i+"1");
//				currentService2.deleteByPrimaryKey(i+"2");
//				currentService2.deleteByPrimaryKey(i+"3");
//				voltageService2.deleteByPrimaryKey(i+"1");
//				voltageService2.deleteByPrimaryKey(i+"2");
//			}*/
//			String id=new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
//			for(int i=0;i<100;i++){
//				int long1=(int)(Math.random()*100);
//				currentService2.updateByPrimaryKeySelective(new HighVoltageCurrent(i+"0",a,"A",long1,i+""));
//				currentService2.updateByPrimaryKeySelective(new HighVoltageCurrent(i+"1",a,"B",long1,i+""));
//				currentService2.updateByPrimaryKeySelective(new HighVoltageCurrent(i+"2",a,"C",long1,i+""));
//				HistoryCurrentService.insert(new HistoryHighVoltageCurrent(id+"1",a,"A",long1,i+""));
//				HistoryCurrentService.insert(new HistoryHighVoltageCurrent(id+"2",a,"B",long1,i+""));
//				HistoryCurrentService.insert(new HistoryHighVoltageCurrent(id+"3",a,"C",long1,i+""));
//			}
//			for(int i=0;i<100;i++){
//				int long1=(int)(Math.random()*100000);
//				voltageService2.updateByPrimaryKeySelective(new HighVoltageVoltage(i+"1",a,"B",long1,i+""));
//				voltageService2.updateByPrimaryKeySelective(new HighVoltageVoltage(i+"2",a,"A",long1,i+""));
//				HistoryVoltageService.insert(new HistoryHighVoltageVoltage(id+"1",a,"B",long1,i+""));
//				HistoryVoltageService.insert(new HistoryHighVoltageVoltage(id+"2",a,"A",long1,i+""));
//			}
//	}

}
