package com.gdut.dongjun.core.chain.high.impl;

import com.gdut.dongjun.core.chain.AbstractHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public class UnSolveHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddressRegisterHandler.class);

    @Override
    public boolean handleRequest(ChannelHandlerContext ctx, String data, String functionData) {
        logger.error("无法解析报文：" + data);
        return true;
    }
}
