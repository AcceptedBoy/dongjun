package com.gdut.dongjun.base;

import com.gdut.dongjun.constant.Constant;
import com.gdut.dongjun.util.HexString_BytesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * Created by AcceptedBoy on 2016/8/28.
 */
public class DefaultSimulateSend extends AbstractSimulateSend {

    @Override
    public void createSourceCache() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream(Constant.OUTPUT_FILE)))){
            String str;
            while((str = br.readLine()) != null) {
                cache.add(str);
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

     @Override
     public void sendToIpWithPort() throws IOException, InterruptedException {
         for(int i = 0, length = cache.size(); i < length; ++i) {
             Socket socket = new Socket(Constant.SEND_IP, Constant.SEND_PORT);
             OutputStream os = socket.getOutputStream();
             os.write(HexString_BytesUtil.hexStringToBytes(cache.get(i)));
             os.flush();
             Thread.sleep(1000);
         }
     }
 }
