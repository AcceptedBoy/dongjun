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
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.dto.HitchEventDTO;

/**
 * 处理ActiveMQ的类
 * 
 * @author Gordan_Deng
 * @date 2017年3月27日
 */
@Service
public class UserMQService extends AbstractMQService {

	// User的id和MessageHolder的映射
	private static final Map<String, MessageHolder> messageHolder = new HashMap<String, MessageHolder>();

	private Logger logger = Logger.getLogger(UserMQService.class);

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	public void setPooledConnectionFactory(PooledConnectionFactory pooledFactory) {
		if (null == AbstractMQService.pooledFactory) {
			AbstractMQService.pooledFactory = pooledFactory;
		}
	}

	/**
	 * 生成MessagerProducer
	 * 
	 * @param destinationName
	 * @param session
	 * @return
	 * @throws JMSException
	 */
	public MessageProducer createQueueMessageProducer(String destinationName, Session session) throws JMSException {
		Destination d = session.createQueue(destinationName);
		MessageProducer producer = session.createProducer(d);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		return producer;
	}

	/**
	 * 生成MessageConsumer
	 * 
	 * @param destinationName
	 * @param session
	 * @param user
	 * @return
	 * @throws JMSException
	 */
	public MessageConsumer createQueueMessageConsumer(String destinationName, Session session, final User user)
			throws JMSException {
		Destination d = session.createQueue(destinationName);
		MessageConsumer consumer = session.createConsumer(d);
		logger.info("用户" + user.getName() + "开始监听mq");
		consumer.setMessageListener(new MessageListener() {

			Logger logger = Logger.getLogger(MessageListener.class);

			@Override
			public void onMessage(Message message) {
				logger.info(user.getName() + "接收到报警消息");
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

	/**
	 * 发送报警消息
	 * 
	 * @param user
	 * @param dto
	 * @throws JMSException
	 */
	public void sendHitchMessage(User user, HitchEventDTO dto) throws JMSException {
		Session session = getUserMQSession(user);
		MessageProducer producer = getUserMessageProducer(user);
		ObjectMessage message = session.createObjectMessage();
		message.setObject(dto);
		producer.send(message);
	}

	/**
	 * 初始化用户的MessageHolder
	 * 
	 * @param user
	 * @return
	 * @throws JMSException
	 */
	public Session initUserMessageHolder(User user) throws JMSException {
		messageHolder.remove(user.getId());
		MessageHolder aMessageHolder = new MessageHolder(user);
		messageHolder.put(user.getId(), aMessageHolder);
		return aMessageHolder.getSession();
	}

	/**
	 * 用户登录后的操作，主要初始化MessageHolder和初始化MessageConsumer
	 * 如果在用户登录之前已经被初始化，那可能是本类的sendHitchMessage方法中调用
	 * 
	 * @param user
	 * @throws JMSException
	 */
	public void remarkLogIn(User user) throws JMSException {
		// 如果MessageHolder已经初始化，只需要初始化MessageConsumer
		if (isMessageHolderInited(user)) {
			initUserMessageConsumer(user);
		}
		// 否则连同MessageHolder一起初始化
		else {
			initUserMessageHolder(user);
			initUserMessageConsumer(user);
		}
	}

	/**
	 * 用户登录，初始化消息监听器
	 * 
	 * @param user
	 * @throws JMSException
	 */
	private void initUserMessageConsumer(User user) throws JMSException {
		MessageHolder holder = messageHolder.get(user);
		Session session = holder.getSession();
		holder.setConsumer(createQueueMessageConsumer(holder.getDestinationName(user), session, user));
	}

	/**
	 * 返回当前用户指定的session，如果没有就创建MessageHolder
	 * 
	 * @param user
	 * @return
	 * @throws JMSException
	 */
	private Session getUserMQSession(User user) throws JMSException {

		if (isMessageHolderInited(user)) {
			return messageHolder.get(user).getSession();
		}
		return initUserMessageHolder(user);
	}

	/**
	 * 返回用户的MessageProducer
	 * @param user
	 * @return
	 */
	private MessageProducer getUserMessageProducer(User user) {
		return messageHolder.get(user.getId()).getProducer();
	}

	/**
	 * 返回用户的MessageConsumer
	 * @param user
	 * @return
	 */
	private MessageConsumer getUserMessageConsumer(User user) {
		return messageHolder.get(user.getId()).getConsumer();
	}

	/**
	 * 判断MessageHolder被初始化了没有
	 * @param user
	 * @return
	 */
	private boolean isMessageHolderInited(User user) {
		if (messageHolder.containsKey(user.getId())) {
			return true;
		}
		return false;
	}

	
	/**
	 * 这个类维护了跟User有关的Session，MessageProducer，MessageConsumer
	 * @author Gordan_Deng
	 * @date 2017年3月28日
	 */
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
			// 新建MessageHolder时候有可能用户还没有登录，不新建MessageConsumer
			// consumer = createQueueMessageConsumer(getDestinationName(user),
			// session, user);
			consumer = null;
		}

		public String getDestinationName(User user) {
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
