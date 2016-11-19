package com.gdut.symon;

import com.gdut.dongjun.constant.Constant;
import com.gdut.dongjun.util.HexString_BytesUtil;
import sun.awt.image.ImageWatched;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.net.InetAddress.getLocalHost;

/**
 * Created by symon on 16-11-15.
 */
public class GetMessageSource {

    private static Map<String, LinkedList<String>> cache = new HashMap<>();

    private static ExecutorService threadPool =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public static void main(String[] args) throws IOException {

        getSource();

        send();
    }

    private static void send() throws IOException {
        int clientPort = 22366;
        for(Map.Entry<String, LinkedList<String>> entry : cache.entrySet()) {
            Socket socket = new Socket("127.0.0.1", 8464, InetAddress.getLocalHost(), clientPort++);
            threadPool.execute(new Sender(socket, entry.getValue()));
        }
    }

    private static class Sender extends Thread {
        private Socket socket;
        private List<String> cacheMsg;
        private int index;
        private int size;

        public Sender(Socket socket, List<String> cacheMsg) {
            this.socket = socket;
            this.cacheMsg = cacheMsg;
            this.index = 0;
            this.size = cacheMsg.size();
        }

        @Override
        public void run() {
            OutputStream os = null;
            while(true) {
                try {
                    os = socket.getOutputStream();
                    System.out.println("------");
                    os.write(HexString_BytesUtil.hexStringToBytes(cacheMsg.get(index++ % size)));
                    os.flush();
                    //Random random = new Random(3);
                    Thread.sleep(2 * 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void getSource() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("/home/symon/myApp.log.2016-11-07.log"),
                        "gb2312"));
        /*BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("/home/symon/1.txt"),
                        "utf-8"));*/

        String str;

        while((str = br.readLine()) != null) {
            if (str.contains("接收到的报文： eb90") || str.contains("接收到的报文： 68")) {
                String value = str.substring(str.lastIndexOf("接收到的报文： ") + "接收到的报文： ".length(), str.length());
                String key;
                if(value.startsWith("eb90")) {
                    key = value.substring(12, 16);
                } else {
                    key = value.substring(10, 14);
                }
                if (!cache.containsKey(key)) {
                    //不存在key
                    cache.put(key, new LinkedList<String>());
                }
                cache.get(key).add(value);
            }
        }
        br.close();
    }
}
