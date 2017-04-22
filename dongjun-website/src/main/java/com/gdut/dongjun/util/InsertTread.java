package com.gdut.dongjun.util;

/**
 * 循环插入数据库线程
 * @author yzh
 *
 */
//@Component
public class InsertTread implements Runnable{

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
