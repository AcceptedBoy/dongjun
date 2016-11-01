package com.gdut.dongjun.core.initializer.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.handler.Decoder;
import com.gdut.dongjun.core.handler.Encoder;
import com.gdut.dongjun.core.handler.msg_decoder.TemperatureDataReceiver;
import com.gdut.dongjun.core.initializer.ServerInitializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

@Component("TemperatureServerInitializer")
public class TemperatureServerInitializer extends ServerInitializer {

	@Resource
	private TemperatureDataReceiver receiver;

	@Override
	public void initChannel(SocketChannel ch) throws Exception {

		super.initChannel(ch);
		ChannelPipeline p = ch.pipeline();
		p.addLast(new Decoder());
		p.addLast(new Encoder());
		p.addLast(receiver);
	}
}
