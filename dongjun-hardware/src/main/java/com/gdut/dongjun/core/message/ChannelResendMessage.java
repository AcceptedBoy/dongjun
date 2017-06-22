package com.gdut.dongjun.core.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.Channel;

public class ChannelResendMessage {

	private List<Channel> channels;
	private Map<String, String> msgs;
	
	public ChannelResendMessage() {
		super();
		this.channels = new ArrayList<Channel>();
		this.msgs = new HashMap<String, String>();
	}
	public List<Channel> getChannels() {
		return channels;
	}
	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
	public Map<String, String> getMsgs() {
		return msgs;
	}
	public void setMsgs(Map<String, String> msgs) {
		this.msgs = msgs;
	}
	
	
}
