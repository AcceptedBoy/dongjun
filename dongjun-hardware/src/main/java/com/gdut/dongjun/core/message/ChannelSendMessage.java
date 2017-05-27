package com.gdut.dongjun.core.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.netty.channel.Channel;

public class ChannelSendMessage {

	private List<Channel> channel;
	private Stack<String> allMessage;
	
	public ChannelSendMessage() {
		channel = new ArrayList<Channel>();
	}
	
	public List<Channel> getChannel() {
		return channel;
	}
	public void setChannel(List<Channel> channel) {
		this.channel = channel;
	}
	public Stack<String> getAllMessage() {
		return allMessage;
	}
	public void setAllMessage(Stack<String> allMessage) {
		this.allMessage = allMessage;
	}
	
	
	
}
