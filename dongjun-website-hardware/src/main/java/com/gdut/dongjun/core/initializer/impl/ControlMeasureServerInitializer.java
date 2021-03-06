package com.gdut.dongjun.core.initializer.impl;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.handler.Decoder;
import com.gdut.dongjun.core.handler.Encoder;
import com.gdut.dongjun.core.handler.msg_decoder.ControlMeasureDataReceiver;
import com.gdut.dongjun.core.initializer.ServerInitializer;


/**   
 * @author	Sherlock-lee
 * @date	2015年11月17日 上午11:51:05
 * @see 	TODO
 * @since   1.0
 */
@Component("ControlMeasureServerInitializer")
public class ControlMeasureServerInitializer extends ServerInitializer{

	@Autowired
	private ControlMeasureDataReceiver dataReceiver;
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline p = ch.pipeline();
		if (super.sslCtx != null) {
			p.addLast(super.sslCtx.newHandler(ch.alloc()));// 建立TCP长连接
		}
		p.addLast(new Decoder());
		p.addLast(new Encoder());
		p.addLast(dataReceiver);
		
		
	}
	
	
	
	
	
	
	

}
