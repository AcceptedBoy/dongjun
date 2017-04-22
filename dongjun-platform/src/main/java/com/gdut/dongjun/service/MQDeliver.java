package com.gdut.dongjun.service;

import javax.jms.MessageProducer;

public interface MQDeliver {
	
	public void sendMessageToQueue(MessageProducer producer, Object obj);
	
	public void sendMessageToTopic(MessageProducer producer, Object obj);
}
