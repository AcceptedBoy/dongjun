package com.gdut.dongjun.service.manager;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.dto.HitchEventDTO;

//@Component
public class MQProductHelper {
//
//	public static final Logger LOG = Logger.getLogger(MQProductHelper.class);
//	private static PooledConnectionFactory poolFactory;
//	private static ActiveMQConnectionFactory connectionFactory;
//
//	@Autowired
//	public static void setConnectionFactory(ActiveMQConnectionFactory factory) {
//		connectionFactory = factory;
//	}
//
//	/**
//	 * 获取单例的PooledConnectionFactory
//	 * 
//	 * @return
//	 */
//	private static synchronized PooledConnectionFactory getPooledConnectionFactory() {
//		if (poolFactory != null)
//			return poolFactory;
//		poolFactory = new PooledConnectionFactory(connectionFactory);
//		// 池中借出的对象的最大数目
//		poolFactory.setMaxConnections(100);
//		poolFactory.setMaximumActiveSessionPerConnection(50);
//		// 后台对象清理时，休眠时间超过了3000毫秒的对象为过期
//		poolFactory.setTimeBetweenExpirationCheckMillis(3000);
//		LOG.info("getPooledConnectionFactory create success");
//		return poolFactory;
//	}
//
//	public static Session createSession() throws JMSException {
//		PooledConnectionFactory poolFactory = getPooledConnectionFactory();
//		PooledConnection pooledConnection = (PooledConnection) poolFactory.createConnection();
//		// false 参数表示 为非事务型消息，后面的参数表示消息的确认类型（见4.消息发出去后的确认模式）
//		return pooledConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//	}
//
//	public static void produce(String subject, String msg) {
//		LOG.info("producer send msg: {} ", msg);
//		if (StringUtil.isEmpty(msg)) {
//			LOG.warn("发送消息不能为空。");
//			return;
//		}
//		try {
//			Session session = createSession();
//			LOG.info("create session");
//			TextMessage textMessage = session.createTextMessage(msg);
//			Destination destination = session.createQueue(subject);
//			MessageProducer producer = session.createProducer(destination);
//			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//			producer.send(textMessage);
//			LOG.info("create session success");
//		} catch (JMSException e) {
//			LOG.error(e.getMessage(), e);
//		}
//	}
	
	@Autowired
	private ActiveMQConnectionFactory connectionFactory;
	
	private Connection con = null;
	
	private Session session = null;
	
	public void sendHitchEvent(User user, HitchEventDTO dto) throws JMSException {
		String queueName = "/queue/user-" + user.getId() + "/hitch";
		
		sendQueueMessage(queueName, dto);
	}
	
	public void sendQueueMessage(String queueName, Object obj) throws JMSException {
		
		if (null == con) {
			con = connectionFactory.createConnection();
		}
		
		if (null == session) {
			session = con.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		}
		Destination destination = session.createQueue(queueName);
		
		MessageProducer producer = session.createProducer(destination);  
		producer.setDeliveryMode(0);
		Message message = session.createMessage();
		
		message.setObjectProperty("HitchEvent", obj);
		
		producer.send(message);
	}
}