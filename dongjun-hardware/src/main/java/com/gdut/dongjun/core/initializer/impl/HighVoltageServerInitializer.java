package com.gdut.dongjun.core.initializer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.handler.Decoder;
import com.gdut.dongjun.core.handler.Encoder;
import com.gdut.dongjun.core.handler.OverlongTextDecoder;
import com.gdut.dongjun.core.handler.msg_decoder.HighVoltageDataReceiver;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import com.sun.xml.bind.v2.TODO;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**   
 * @author	Sherlock-lee
 * @date	2015年11月17日 上午11:51:05
 * @see 	TODO
 * @since   1.0
 */
@Component("HighVoltageServerInitializer")
public class HighVoltageServerInitializer extends ServerInitializer{

	@Autowired
	private HighVoltageDataReceiver dataReceiver;
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		
		super.initChannel(ch);
		ChannelPipeline p = ch.pipeline();
//		p.addLast(new OverlongTextDecoder());
		p.addLast(new Decoder());
		p.addLast(new Encoder());
		p.addLast(dataReceiver);
	}

}
