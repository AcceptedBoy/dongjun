package com.gdut.dongjun;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.server.impl.ControlMeasureServer;
import com.gdut.dongjun.core.server.impl.HighVoltageServer;
import com.gdut.dongjun.core.server.impl.LowVoltageServer;
import com.gdut.dongjun.domain.dao.ProtocolPortMapper;
import com.gdut.dongjun.domain.po.port.ProtocolPort;


@Component
public class MonitorStartup extends HttpServlet implements ApplicationContextAware {
	
	@Override
	public void init() {
		
		ProtocolPort port = ((ProtocolPortMapper)getBean("protocolPortDAOImpl")).
				selectByPrimaryKey("1");
		if(null != port) {
			((LowVoltageServer)getBean("LowVoltageServer")).setPort(port.getLvPort());
			((HighVoltageServer)getBean("HighVoltageServer")).setPort(port.getHvPort());
			((ControlMeasureServer)getBean("ControlMeasureServer")).setPort(port.getHvPort());
			
			logger.info("低压开关端口号：" + port.getLvPort());
			logger.info("高压开关端口号：" + port.getHvPort());
			logger.info("管控开关端口号：" + port.getConPort());
			((LowVoltageServer)getBean("LowVoltageServer")).start();
			((HighVoltageServer)getBean("HighVoltageServer")).start();
			((ControlMeasureServer)getBean("ControlMeasureServer")).start();
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private static ApplicationContext applicationContext;
	
	/**
	 * 实现该接口的setApplicationContext(ApplicationContext context)方法，
	 * 并保存ApplicationContext 对象。Spring初始化时，
	 * 会通过该方法将ApplicationContext对象注入。
	 * 
	 * 如果只是在类中使用autowired对使用对象进行注入，这一步骤在spring的执行过程中
	 * 完成bean初始化后还没有执行，则得出的值为null
	 */
	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext; 
	}
	
	public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    } 
	
	public static Object getBean(String name) throws BeansException {  
        return applicationContext.getBean(name);  
    }
}
