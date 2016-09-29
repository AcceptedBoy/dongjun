package com.gdut.dongjun.core.chain.high.impl;

import com.gdut.dongjun.core.chain.AbstractHandler;
import com.gdut.dongjun.core.chain.high.HighVoltageHandlerChain;
import com.gdut.dongjun.core.chain.high.factory.HighVoltageChainFactory;
import com.gdut.dongjun.util.StringCommonUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * Created by AcceptedBoy on 2016/9/16.
 */
@Component
public class CircleAllGetHandler extends AbstractHandler {

    private static CopyOnWriteArrayList<AbstractHandler> handlerChain;

    @Resource
    private void setHandlerChain(HighVoltageChainFactory factory) {
        handlerChain = factory.getInstance();
    }

    /**
     * 旧机器解析
     */
    @Override
    public boolean handleRequest(ChannelHandlerContext ctx, String data, String functionData) {

        if(!functionData.equalsIgnoreCase("64") || !(functionData.length() <= 36)) {
            return false;
        }

        while(data.length() != 0) {
            int index = StringCommonUtil.getFirstIndexOfEndTag(data, "16");
            if(index != -1) {
                String dataInfo = data.substring(0, index);
                new HighVoltageHandlerChain(handlerChain, ctx, dataInfo, dataInfo.substring(14, 16)).process();
                data = data.substring(index, data.length());
            }
        }
        return true;
    }
}
