package com.gdut.dongjun.core.initializer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.handler.Encoder;
import com.gdut.dongjun.core.handler.SeparatedTextDecoder;
import com.gdut.dongjun.core.handler.msg_decoder.ElectronicDataReceiver;
import com.gdut.dongjun.core.initializer.ServerInitializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

//@Component("ElectronicServerInitializer")
public class ElectronicServerInitializer extends ServerInitializer {

	@Autowired
	private ElectronicDataReceiver receiver;
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {

		super.initChannel(ch);
		ChannelPipeline p = ch.pipeline();
		//处理拆包，能转16进制byte数组到String
		p.addLast(new SeparatedTextDecoder());
		p.addLast(new Encoder());
		p.addLast(receiver);
	}
}
