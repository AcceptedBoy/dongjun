package com.gdut.dongjun;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by AcceptedBoy on 2016/8/28.
 */
public class SocketTest {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.56.1", 8464);
        System.out.println("1");
    }
}
