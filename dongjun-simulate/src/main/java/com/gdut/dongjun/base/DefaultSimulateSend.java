package com.gdut.dongjun.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import com.gdut.dongjun.constant.Constant;
import com.gdut.dongjun.util.HexString_BytesUtil;

/**
 */
public class DefaultSimulateSend extends AbstractSimulateSend {

	@Override
	public void createSourceCache() {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream(Constant.OUTPUT_FILE)))) {
			String str;
			while ((str = br.readLine()) != null) {
				cache.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendToIpWithPort() throws IOException, InterruptedException {
//		cache.clear();
//		cache.add(
//				"682f3f68F4FA00000009C11404F400000011223344556677004000DF0000DF0000DF0000DF0000000000000000000000000000000000000000000000000000000000000000000000000000000016");
//		for (int i = 0, length = cache.size(); i < length; ++i) {
//			Socket socket = new Socket("115.28.7.40", 8466);
//			OutputStream os = socket.getOutputStream();
//			os.write(HexString_BytesUtil.hexStringToBytes(cache.get(i)));
//			os.flush();
//			Thread.sleep(200);
//		}
		Thread t = new Thread(new TemThread());
		t.start();
		Thread h = new Thread(new HighThread());
		h.start();
	}

	class TemThread implements Runnable {

		@Override
		public void run() {
			Socket socket;
			try {
				socket = new Socket("115.28.7.40", 8466);
				OutputStream os = socket.getOutputStream();
				String pre = "682f3f68F4FA00000009C11404F4000000112233445566770040";
				String post = "ff16";
				while (true) {
					StringBuilder sb = new StringBuilder();
					sb.append(pre);
					Random r1 = new Random();
					for (int i = 0; i < 16; i++) {
						String num = Integer.toHexString((r1.nextInt(7) + 17) * 10);
						sb.append("00").append(num).append("00");
					}
					sb.append("0000000000").append(post);
					os.write(HexString_BytesUtil.hexStringToBytes(sb.toString()));
					os.flush();
					Thread.sleep(1000 * 60 * 10);
					sb = new StringBuilder();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	class HighThread implements Runnable {

		@Override
		public void run() {
			Socket socket;
			try {
				socket = new Socket("115.28.7.40", 8464);
				OutputStream os = socket.getOutputStream();
				String pre = "68474768f4af0009941401af000140";
				
				String post = "004c59000000000000003400000000000000000000000300000500005802008a13000000000000000000000000000000000000000000000000001d16";
				while (true) {
					StringBuilder sb = new StringBuilder();
					sb.append(pre);
					Random r1 = new Random();
						String num = Integer.toHexString((r1.nextInt(700) * 10 + 19000));
						char[] chars = new char[4];
						chars[0] = num.charAt(2);
						chars[1] = num.charAt(3);
						chars[2] = num.charAt(0);
						chars[3] = num.charAt(1);
						sb.append(new String(chars));
					sb.append(post);
					os.write(HexString_BytesUtil.hexStringToBytes(sb.toString()));
					os.flush();
					Thread.sleep(1000 * 60 * 10);
					sb = new StringBuilder();
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
