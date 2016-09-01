package com.gdut.dongjun.strategy;

import com.gdut.dongjun.strategy.read.Read;
import com.gdut.dongjun.strategy.send.Send;

/**
 * 报文发送的策略
 * Created by AcceptedBoy on 2016/9/1.
 */
public abstract class SimulateStrategy {

    private Read read;

    private Send send;

    protected Object read() {
        return read.read();
    }

    protected void send(Object msg) {
        send.send(msg);
    }

    public abstract void readAndSend();

    protected void setRead(Read read) {
        this.read = read;
    }

    protected void setSend(Send send) {
        this.send = send;
    }
}
