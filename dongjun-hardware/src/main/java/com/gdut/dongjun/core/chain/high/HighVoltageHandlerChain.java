package com.gdut.dongjun.core.chain.high;

import com.gdut.dongjun.core.chain.AbstractHandler;
import com.gdut.dongjun.core.chain.AbstractHandlerChain;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
public class HighVoltageHandlerChain extends AbstractHandlerChain {

    private Logger logger = LoggerFactory.getLogger(HighVoltageHandlerChain.class);

    public HighVoltageHandlerChain(ChannelHandlerContext ctx, String data, String functionData) {
        this(null, ctx, data, functionData);
    }

    public HighVoltageHandlerChain(CopyOnWriteArrayList<AbstractHandler> handlerChain,
                                ChannelHandlerContext ctx, String data, String functionData) {
        super.handlerChain = handlerChain;
        super.ctx = ctx;
        super.data = data;
        super.functionData = functionData;
    }

    @Override
    public void preHandlerRequest() {
        logger.info("接收到的报文： " + data);
    }

    @Override
    public void afterHandlerRequest() {

    }

    @Override
    public void unHandlerRequest() {
        //help GC
        data = null;
        functionData = null;
    }
}
