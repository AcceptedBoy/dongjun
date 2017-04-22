package com.gdut.dongjun.base;

import com.gdut.dongjun.constant.Constant;
import com.gdut.dongjun.util.HexString_BytesUtil;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * 抽象类，主要包含着发送模拟报文的整个流程
 */
public abstract class AbstractSimulateSend {

    /**
     * 存储报文
     */
    protected static List<String> cache = new LinkedList<>();

    public List<String> getCache() {
        return cache;
    }

    /**
     * 模板方法，创建cache后发送报文
     * @throws IOException
     */
    public final void simulateSend() throws IOException, InterruptedException {
        createSourceCache();
        sendToIpWithPort();
    }

    /**
     * 模板方法，先转化为能够识别的txt内容，再执行{@code simulateSend}的流程
     * @throws IOException
     */
    public final void solveAndSimulateSend() throws IOException, InterruptedException {
        new SourceTxt().createSourceTxt();
        simulateSend();
    }

    /**
     * 发送报文到指定的ip和端口
     * @throws IOException
     */
    public void sendToIpWithPort() throws IOException, InterruptedException {
    	
        for(int i = 0, length = cache.size();; ++i) {
            Socket socket = new Socket(Constant.SEND_IP, Constant.SEND_PORT);
            if(i == length) {
                i = 0;
               continue;
            }
            OutputStream os = socket.getOutputStream();
            os.write(HexString_BytesUtil.hexStringToBytes(cache.get(i)));
            os.flush();
            Thread.sleep(1000);
        }
    }

    /**
     * 抽象方法，决定了cache的产生
     */
    public abstract void createSourceCache();
}
