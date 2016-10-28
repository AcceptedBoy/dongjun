package com.gdut.dongjun;

import com.gdut.dongjun.core.server.impl.HighVoltageServer_V1_3;
import com.gdut.dongjun.domain.po.ProtocolPort;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.server.impl.ControlMeasureServer;
import com.gdut.dongjun.core.server.impl.HighVoltageServer;
import com.gdut.dongjun.core.server.impl.LowVoltageServer;
import com.gdut.dongjun.domain.dao.ProtocolPortMapper;

import javax.annotation.Resource;



/**
 * 这个是应用程序在初始化的时候启动，所以因此可以开启对端口的监听；
 * <p>编写InitializingBean的实现类,项目在加载完毕后立刻执行{@code afterPropertiesSet()} 方法 ,
 * 并且可以使用spring 注入好的bean
 * @author link xiaoMian <972192420@qq.com>
 */
@Component
public class MonitorStartup implements InitializingBean {
	
	@Autowired
	private LowVoltageServer lowVoltageServer;
	
	@Autowired
	private HighVoltageServer highVoltageServer;

	@Resource(name = "HighVoltageServer_V1_3")
	private HighVoltageServer_V1_3 highVoltageServer_v1_3;
	
	@Autowired
	private ControlMeasureServer controlMeasureServer;
	
	@Autowired
	private ProtocolPortMapper protocolPortDAOImpl;

	private static Logger logger = Logger.getLogger(MonitorStartup.class);

	/**
	 * 在项目启动的时候开启监听端口
	 * @throws Exception
     */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		ProtocolPort port = protocolPortDAOImpl.selectByPrimaryKey("1");
		
		logger.info("低压开关端口号：" + port.getLvPort());
		logger.info("高压开关端口号：" + port.getHvPort());
		logger.info("管控开关端口号：" + port.getConPort());
		logger.info("高压版本1.3端口号：" + port.getHv13Port());

		lowVoltageServer.setPort(port.getLvPort());
		highVoltageServer.setPort(port.getHvPort());
		controlMeasureServer.setPort(port.getConPort());
		highVoltageServer_v1_3.setPort(port.getHv13Port());

		lowVoltageServer.start();
		highVoltageServer.start();
		controlMeasureServer.start();
		highVoltageServer_v1_3.start();
	}	
	
	
}