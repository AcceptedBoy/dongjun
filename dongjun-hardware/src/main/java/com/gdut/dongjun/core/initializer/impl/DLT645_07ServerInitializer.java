package com.gdut.dongjun.core.initializer.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.core.handler.Encoder;
import com.gdut.dongjun.core.handler.msg_decoder.ElectronicDataReceiver;
import com.gdut.dongjun.core.initializer.ServerInitializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * 多功能电表DLT645_2007协议服务器
 * @author Gordan_Deng
 * @date 2017年8月7日
 */
//@Component("ElectronicServerInitializer")
public class DLT645_07ServerInitializer extends ServerInitializer {

	@Autowired
	private ElectronicDataReceiver receiver;
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {

		super.initChannel(ch);
		ChannelPipeline p = ch.pipeline();
		//处理拆包，能转16进制byte数组到String
//		p.addLast(new SeparatedTextDecoder());
		p.addLast(new Encoder());
		p.addLast(receiver);
	}
}
