package com.gdut.dongjun.service.mq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.dto.HitchEventDTO;
import com.gdut.dongjun.service.UserService;

@Service
public class UserMQService extends AbstractMQService {

	private static final Map<User, MessageHolder> messageHolder = new HashMap<User, MessageHolder>();

	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private UserService userService;

	@Autowired
	public void setPooledConnectionFactory(PooledConnectionFactory pooledFactory) {
		if (null == AbstractMQService.pooledFactory) {
			AbstractMQService.pooledFactory = pooledFactory;
		}
	}

	public MessageProducer createQueueMessageProducer(String destinationName, Session session) throws JMSException {
		Destination d = session.createQueue(destinationName);
		MessageProducer producer = session.createProducer(d);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		return producer;
	}

	public MessageConsumer createQueueMessageConsumer(String destinationName, Session session, final User user)
			throws JMSException {
		Destination d = session.createQueue(destinationName);
		MessageConsumer consumer = session.createConsumer(d);
		consumer.setMessageListener(new MessageListener() {

			Logger logger = Logger.getLogger(MessageListener.class);

			@Override
			public void onMessage(Message message) {
				String id = user.getId();
				HitchEventDTO dto = null;
				try {
					dto = (HitchEventDTO) ((ObjectMessage) message).getObject();
					template.convertAndSend("/queue/user-" + id + "/hitch", dto);
				} catch (JMSException e) {
					logger.warn("MQ转化错误，发生错误MQ为" + "/queue/user-" + user.getId() + "/hitch");
					e.printStackTrace();
				}
			}

		});
		return consumer;
	}

	public void sendHitchMessage(User user, HitchEventDTO dto) throws JMSException {
		Session session = getUserMQSession(user);
		MessageProducer producer = getUserMessageProducer(user);
		ObjectMessage message = session.createObjectMessage();
		message.setObject(dto);
		producer.send(message);
	}

	public Session initUserMessageHolder(User user) throws JMSException {
		for (User u : messageHolder.keySet()) {
			if (u.getId() == user.getId() || u.getId().equals(user.getId())) {
				messageHolder.remove(u);
				break;
			}
		}
		MessageHolder aMessageHolder = new MessageHolder(user);
		messageHolder.put(user, aMessageHolder);
		return aMessageHolder.getSession();
	}
	
	private Session getUserMQSession(User user) throws JMSException {
		for (User u : messageHolder.keySet()) {
			if (u.getId().equals(user.getId()) || u.getId() == user.getId()) {
				return messageHolder.get(u).getSession();
			}
		}
		return initUserMessageHolder(user);
	}
	
	private MessageProducer getUserMessageProducer(User user) {
		for (User u : messageHolder.keySet()) {
			if (u.getId() == user.getId() || u.getId().equals(user.getId())) {
				return messageHolder.get(u).getProducer();
			}
		}
		return null;
	}
	
	private MessageConsumer getUserMessageConsumer(User user) {
		for (User u : messageHolder.keySet()) {
			if (u.getId() == user.getId() || u.getId().equals(user.getId())) {
				return messageHolder.get(u).getConsumer();
			}
		}
		return null;
	}

	class MessageHolder {

		private static final String HITCH_QUEUE_PREDIX = "/queue/user-";

		private static final String HITCH_QUEUE_POSTDIX = "/hitch";

		private User user;

		private MessageProducer producer;

		private MessageConsumer consumer;

		private Session session;

		public MessageHolder(User user) throws JMSException {
			this.user = user;
			session = getMQSession();
			producer = createQueueMessageProducer(getDestinationName(user), session);
			consumer = createQueueMessageConsumer(getDestinationName(user), session, user);
		}

		private String getDestinationName(User user) {
			return HITCH_QUEUE_PREDIX + user.getId() + HITCH_QUEUE_POSTDIX;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public MessageProducer getProducer() {
			return producer;
		}

		public void setProducer(MessageProducer producer) {
			this.producer = producer;
		}

		public MessageConsumer getConsumer() {
			return consumer;
		}

		public void setConsumer(MessageConsumer consumer) {
			this.consumer = consumer;
		}

		public Session getSession() {
			return session;
		}

		public void setSession(Session session) {
			this.session = session;
		}
	}
}
