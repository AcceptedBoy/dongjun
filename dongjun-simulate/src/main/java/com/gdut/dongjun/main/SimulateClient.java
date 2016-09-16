package com.gdut.dongjun.main;

import com.gdut.dongjun.base.DefaultSimulateSend;

import java.io.IOException;

/**
 * 开始发送模拟数据的类，有main方法
 * Created by AcceptedBoy on 2016/8/28.
 */
public class SimulateClient {

    public static void main(String[] args) throws IOException, InterruptedException {

        new DefaultSimulateSend().simulateSend();
    }
}
