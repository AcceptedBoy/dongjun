package com.gdut.dongjun.core.chain;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by AcceptedBoy on 2016/9/16.
 */
public abstract class AbstractHandlerChain {

    protected CopyOnWriteArrayList<AbstractHandler> handlerChain;

    protected ChannelHandlerContext ctx;

    protected String data;

    protected String functionData;

    public void createHandlerChain(AbstractHandler ... handlers) {
        for(AbstractHandler handler : handlers) {
            handlerChain.add(handler);
        }
    }

    public abstract void preHandlerRequest();

    public abstract void afterHandlerRequest();

    public abstract void unHandlerRequest();

    public void process() {
        preHandlerRequest();
        for(AbstractHandler handler : handlerChain) {
            if(handler.handleRequest(ctx, data, functionData)) {
                afterHandlerRequest();
                return;
            }
        }
        unHandlerRequest();
    }
}
