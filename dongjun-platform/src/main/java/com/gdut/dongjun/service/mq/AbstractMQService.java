package com.gdut.dongjun.service.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 写得有点糟糕
 * 之所以只写session是因为session是操作mq的基本单位
 * @author Gordan_Deng
 * @date 2017年3月22日
 */
public abstract class AbstractMQService {
	
	protected static PooledConnectionFactory pooledFactory;

	public Session getMQSession() throws JMSException {
		return pooledFactory.createConnection().createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
	}
	
}
