
package com.gdut.dongjun.core.handler.msg_decoder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.HitchConst;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.core.handler.TemperatureParseStrategy;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

/**
 * GPRS模块一连接系统就会发登录包 登录包解析后会在{@code TemperatureCtxStore}里维护GPRS登录情况
 * 关于温度设备的地址，那边的温度设备是分高字节段和低字节段的，高、低字节段都有两个字节。
 * 低字节段在前，高字节段在后。而字节段里两个字节也是低字节在前，高字节在后。 所以当报文以12 34 56 78发过来的时候，低字节段正确的顺序是34
 * 12，高字节段正确的顺序是78 56。 高在前低在后，拼成正确顺序之后转成10进制，得到真正的逻辑地址。
 * 
 * 消息过大的时候会压爆netty，怎么处理?勉神和面试官说的时候是硬件和netty连接的时候， 中间加一个消息中间件做缓冲。
 * 
 * 还有，报文解析的时候怎么做到当用到新的协议，后台改动最少的代码来使得协议上线。 责任链或许会引起性能问题，但不失为一个好方法
 * 
 * TODO如果找不到温度模块就舍弃报文
 * 
 * @author Gordan_Deng
 * @date 2017年3月23日
 */
@Service
@Sharable
public class TemperatureDataReceiver extends AbstractDataReceiver implements InitializingBean {
	
	@Autowired
	private TemperatureParseStrategy parseStrategy;
	@Autowired
	private TemperatureCtxStore ctxStore;

	public TemperatureDataReceiver() {
		super(HitchConst.MODULE_TEMPERATURE, Logger.getLogger(TemperatureDataReceiver.class));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.setParseStrategy(parseStrategy);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctxStore.remove(ctx);
		strategy.clearCache(ctx);
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("异常：" + cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		strategy.clearCache(ctx);
		ctxStore.remove(ctx);
	}

}
