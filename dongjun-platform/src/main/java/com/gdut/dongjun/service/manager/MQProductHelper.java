package com.gdut.dongjun.service.manager;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.service.webservice.server.WebsiteService;

//@Component
//public class MQProductHelper implements InitializingBean {
	public class MQProductHelper {

	// public static final Logger LOG = Logger.getLogger(MQProductHelper.class);
	// private static PooledConnectionFactory poolFactory;
	// private static ActiveMQConnectionFactory connectionFactory;
	//
	// @Autowired
	// public static void setConnectionFactory(ActiveMQConnectionFactory
	// factory) {
	// connectionFactory = factory;
	// }
	//
	// /**
	// * 获取单例的PooledConnectionFactory
	// *
	// * @return
	// */
	// private static synchronized PooledConnectionFactory
	// getPooledConnectionFactory() {
	// if (poolFactory != null)
	// return poolFactory;
	// poolFactory = new PooledConnectionFactory(connectionFactory);
	// // 池中借出的对象的最大数目
	// poolFactory.setMaxConnections(100);
	// poolFactory.setMaximumActiveSessionPerConnection(50);
	// // 后台对象清理时，休眠时间超过了3000毫秒的对象为过期
	// poolFactory.setTimeBetweenExpirationCheckMillis(3000);
	// LOG.info("getPooledConnectionFactory create success");
	// return poolFactory;
	// }
	//
	// public static Session createSession() throws JMSException {
	// PooledConnectionFactory poolFactory = getPooledConnectionFactory();
//	 PooledConnection pooledConnection = (PooledConnection)
	// poolFactory.createConnection();
	// // false 参数表示 为非事务型消息，后面的参数表示消息的确认类型（见4.消息发出去后的确认模式）
	// return pooledConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	// }
	//
	// public static void produce(String subject, String msg) {
	// LOG.info("producer send msg: {} ", msg);
	// if (StringUtil.isEmpty(msg)) {
	// LOG.warn("发送消息不能为空。");
	// return;
	// }
	// try {
	// Session session = createSession();
	// LOG.info("create session");
	// TextMessage textMessage = session.createTextMessage(msg);
	// Destination destination = session.createQueue(subject);
	// MessageProducer producer = session.createProducer(destination);
	// producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	// producer.send(textMessage);
	// LOG.info("create session success");
	// } catch (JMSException e) {
	// LOG.error(e.getMessage(), e);
	// }
	// }

	/*
	 * 测试代码
	 */
//	@Autowired
//	private ActiveMQConnectionFactory connectionFactory;
//
//	private SimpMessagingTemplate template;
//	@Autowired
//	public void setTemplate(SimpMessagingTemplate template) {
//		this.template = template;
//	}
//
//	private static final String QUEUE_NAME = "/queue/user-001/hitch";
//
//	//连接池化
//	private Connection con = null;
//
//	//session池化
//	private Session session = null;
//	
//	//端点和user一起在后台维护
//	private Destination destination = null;
//	
//	//这个跟Destination一起维护了
//	private MessageProducer producer = null;
//	
//	//这个跟Destination一起维护了
//	private MessageConsumer consumer = null;
//
//	public void sendHitchEvent(User user, HitchEventVO vo) throws JMSException {
//		String queueName = "/queue/user-" + user.getId() + "/hitch";
//		sendQueueMessage(queueName, vo);
//	}
//
//	public void sendQueueMessage(String queueName, Serializable obj) throws JMSException {
//		
//		// 持久
//		ObjectMessage message = session.createObjectMessage();
//
//		// primitive objects, String, Map and List types are
//		// allowed，必须是String,map或者list
//		message.setObject(obj);
//
//		producer.send(message);
//	}
//
////	public Object get() throws JMSException {
////	}
////
////	public Map changeIntoMap(HitchEventDTO dto) {
////		Map<String, String> map = new HashMap<String, String>();
////		map.put("group_id", dto.getGroupId());
////		map.put("name", dto.getName());
////		return map;
////	}
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		con = connectionFactory.createConnection();
//		session = con.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
//		destination = session.createQueue(QUEUE_NAME);
//		producer = session.createProducer(destination);
//		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
//		consumer = session.createConsumer(destination);
//		consumer.setMessageListener(new MessageListener() {
//			@Override
//			public void onMessage(Message message) {
//				System.out.println("接收到信息");
//				HitchEventVO vo = null;
//				try {
//					ObjectMessage ms = (ObjectMessage)message;
//					vo = (HitchEventVO) ms.getObject();
//					template.convertAndSend("/queue/user-001/hitch", vo);
//				} catch (JMSException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		con.start();
//	}
//	
	
}