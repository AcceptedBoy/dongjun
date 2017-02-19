package com.gdut.dongjun.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.domain.po.HistoryHighVoltageCurrent;
import com.gdut.dongjun.domain.po.HistoryHighVoltageVoltage;
import com.gdut.dongjun.service.HistoryHighVoltageCurrentService;
import com.gdut.dongjun.service.HistoryHighVoltageVoltageService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.device.current.HighVoltageCurrentService;
import com.gdut.dongjun.service.device.voltage.HighVoltageVoltageService;
import com.gdut.dongjun.thread.manager.DefaultThreadManager;
import com.gdut.dongjun.util.InsertTread;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 * 测试代码
 * @author yzh
 *
 */
//@Controller
//@RequestMapping("/dongjun")
//@SessionAttributes("test")
public class InsertTestController extends DefaultThreadManager {
//
//	@Autowired
//	public HistoryHighVoltageCurrentService HistoryCurrentService;
//	@Autowired
//	public HistoryHighVoltageVoltageService HistoryVoltageService;
//	@Autowired
//	private HighVoltageCurrentService currentService2;
//	@Autowired
//	private HighVoltageVoltageService voltageService2;
//	@Autowired
//	private InsertTread insertTread;
//	
//	@Autowired
//	private HighVoltageSwitchService SwitchService;
//	
//	//ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);
//	/**
//	 * 一次性插入数据
//	 */
//	@RequestMapping("/one_insert")
//	@ResponseBody
//	public String intsertswitch() {
//		float long11=(float)(Math.random()*100);
//		float weith1=(float)(Math.random()*100);
//		Date a=new Date();
//		String data=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") .format(new Date() );
//		for(int i=0;i<100;i++){
//			/*String id, String name, String address, String simNumber, String deviceNumber,
//			Float longitude, Float latitude, Integer inlineIndex, String lineId, String onlineTime*/
//			SwitchService.insert(new HighVoltageSwitch(""+i, "test"+i, "10"+i, "13517500604", 
//					"1234", long11, weith1, 1, "2"+i%10,"test"+i, data));
//		}
//		for(int i=0;i<100;i++){
//			int long1=(int)(Math.random()*100);
//			currentService2.insert(new HighVoltageCurrent(i+"0",a,"A",long1,i+""));
//			currentService2.insert(new HighVoltageCurrent(i+"1",a,"A",long1,i+""));
//			currentService2.insert(new HighVoltageCurrent(i+"2",a,"A",long1,i+""));
//			HistoryCurrentService.insert(new HistoryHighVoltageCurrent(i+"0",a,"A",long1,i+""));
//			HistoryCurrentService.insert(new HistoryHighVoltageCurrent(i+"1",a,"B",long1,i+""));
//			HistoryCurrentService.insert(new HistoryHighVoltageCurrent(i+"2",a,"C",long1,i+""));
//		}
//		for(int i=0;i<100;i++){
//			int long1=(int)(Math.random()*100);
//			HistoryVoltageService.insert(new HistoryHighVoltageVoltage(i+"1",a,"B",long1,i+""));
//			HistoryVoltageService.insert(new HistoryHighVoltageVoltage(i+"2",a,"A",long1,i+""));
//			voltageService2.insert(new HighVoltageVoltage(i+"1",a,"B",long1,i+""));
//			voltageService2.insert(new HighVoltageVoltage(i+"2",a,"A",long1,i+""));
//		}
//		
//		return "插入成功";
//	}
//	
//	/**
//	 * 创建定时器线程池，执行定时查询任务
//	 */
//	@RequestMapping("/loop_insert")
//	@ResponseBody
//	public String loopInsertTest(){
//		DefaultThreadManager.scheduledPool.scheduleWithFixedDelay
//		(insertTread, 0, 5, TimeUnit.SECONDS);
//		for (int i=0; i<2; i++){
//			System.out.printf("等待下一个5秒");
//			try {
//				TimeUnit.MILLISECONDS.sleep(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return "完成";
//	}
//	
//	
//	//ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);
//		/**
//		 * 删除测试数据
//		 */
//		@RequestMapping("/deleteAll")
//		@ResponseBody
//		public String deleteswitch() {
//		
//			for(int i=0;i<100;i++){
//				currentService2.deleteByParameters(
//						MyBatisMapUtil.warp("switch_id", ""+i));
//				HistoryCurrentService.deleteByParameters(
//						MyBatisMapUtil.warp("switch_id", ""+i));
//				HistoryVoltageService.deleteByParameters(
//						MyBatisMapUtil.warp("switch_id", ""+i));
//				HistoryVoltageService.deleteByParameters(
//						MyBatisMapUtil.warp("switch_id", ""+i));
//			}
//			
//			for(int i=0;i<100;i++){
//				
////				SwitchService.insert(new HighVoltageSwitch(""+i, "test"+i, "10"+i, "13517500604", 
////						"1234", 107.983f, 22.149f, 1, "2"+i%10,"test"+i, data));
//				SwitchService.deleteByParameters(
//						MyBatisMapUtil.warp("id", ""+i));
//			}
//			return "删除成功";
//		}
//	
	
}
