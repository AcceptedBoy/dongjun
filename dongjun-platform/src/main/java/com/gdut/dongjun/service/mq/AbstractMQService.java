package com.gdut.dongjun.service.mq;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.pool.PooledConnectionFactory;

/**
 * 写得有点糟糕
 * 之所以只写session是因为session是操作mq的基本单位
 * @author Gordan_Deng
 * @date 2017年3月22日
 */
public abstract class AbstractMQService {
	
	protected static PooledConnectionFactory pooledFactory;

	public Session getMQSession() throws JMSException {
		Connection connection = pooledFactory.createConnection();
		return connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
	}
	
}
