package com.gdut.dongjun.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

/**
 * @Author link xiaoMian <972192420@qq.com>
 * @ClassName VoiceFixUtil.java
 * @Time 2016年3月3日下午9:25:32
 * @Description TODO
 * @Version 1.0 Topview
 */
public class VoiceFixUtil {

	// 暂时用我个人的百度账户的服务，以后记得要改掉
	private static final String apiKey = "zTlDPueNBgUzDN2Vbt0Bj96z";
	private static final String secretKey = "Gr5yfWuFhKYd8ldPGCo3wDCCmZWNtqe2";
	private static final String cuid = "9098792";

	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?" + httpArg;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.setRequestProperty("apikey", "51b39d53c62ec457bbece217f1574745");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static byte[] request1(String text) throws IOException {
		String baiduUrl = "http://tsn.baidu.com/text2audio?lan=zh&cuid=9098792&ctp=1&tok=" + getToken() + "&tex="
				+ getUTF8XMLString(text);
		 URL url = new URL(baiduUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		connection.connect();
		InputStream is = connection.getInputStream();
//		File f = new File("D:\\audio-" + UUIDUtil.getUUID() + ".mp3");
//		OutputStream output = new FileOutputStream(f);
//
//		byte[] buffer = new byte[8192];
//		int byteReads = 0;
//		while ((byteReads = is.read(buffer, 0, 8192)) != -1) {
//			output.write(buffer, 0, byteReads);
//		}
		byte[] array = new byte[is.available()];
		is.read(array);
		is.close();
		return array;
	}

	private static String getToken() throws IOException {
		String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" + "&client_id="
				+ apiKey + "&client_secret=" + secretKey;
		HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();

		if (conn.getResponseCode() != 200) {
			// request error
			return "";
		}
		InputStream is = conn.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		String token = new JSONObject(response.toString()).getString("access_token");
		return token;
	}

	public static String getUTF8XMLString(String xml) {
		// A StringBuffer Object
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		String xmlUTF8 = "";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
			xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return xmlUTF8;
	}

}
