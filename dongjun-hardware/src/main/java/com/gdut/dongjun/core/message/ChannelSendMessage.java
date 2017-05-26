package com.gdut.dongjun.core.message;

import java.util.Stack;

import io.netty.channel.Channel;

public class ChannelSendMessage {

	private Channel channel;
	private Stack<String> allMessage;
	
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public Stack<String> getAllMessage() {
		return allMessage;
	}
	public void setAllMessage(Stack<String> allMessage) {
		this.allMessage = allMessage;
	}
	
	
	
}
