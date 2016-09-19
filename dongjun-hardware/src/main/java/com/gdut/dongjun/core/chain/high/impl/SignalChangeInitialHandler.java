package com.gdut.dongjun.core.chain.high.impl;

import com.gdut.dongjun.core.chain.AbstractHandler;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public class SignalChangeInitialHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddressRegisterHandler.class);

    @Override
    public boolean handleRequest(ChannelHandlerContext ctx, String data, String functionData) {
        if (!functionData.equals("03")) {
            return false;
        }

        String resu = new HighVoltageDeviceCommandUtil().confirmChangeAffair(data.substring(10, 14));
        logger.info("遥信变位确定---------" + resu);
        ctx.writeAndFlush(resu);
        return true;
    }
}
