package com.gdut.dongjun.core.initializer.impl;

import com.gdut.dongjun.core.handler.Decoder;
import com.gdut.dongjun.core.handler.Encoder;
import com.gdut.dongjun.core.handler.msg_decoder.HighVoltageDataReceiver_V1_3;
import com.gdut.dongjun.core.initializer.ServerInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**   
 * @author	Sherlock-lee
 * @date	2015年11月17日 上午11:51:05
 * @see 	TODO
 * @since   1.0
 */
@Component("HighVoltageServerInitializer_V1_3")
public class HighVoltageServerInitializer_V1_3 extends ServerInitializer{

	@Autowired
	private HighVoltageDataReceiver_V1_3 dataReceiver;
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		
		super.initChannel(ch);
		ChannelPipeline p = ch.pipeline();
		p.addLast(new Decoder());
		p.addLast(new Encoder());
		p.addLast(dataReceiver);
	}

}
