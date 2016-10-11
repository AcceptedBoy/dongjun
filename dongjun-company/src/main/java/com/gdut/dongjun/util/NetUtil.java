package com.gdut.dongjun.util;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 */
public class NetUtil {

    public static String getRealLocalIp() throws SocketException {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces =
                NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    public static String getLocalMacAddress() {
        return getLocalInetMessage().get("mac").toString();
    }

    public static String getCpuArch() {
        return getLocalInetMessage().get("cpu-arch").toString();
    }

    public static String getIpnetType() {
        return getLocalInetMessage().get("ipnet").toString();
    }

    public static Map<String, Object> getLocalInetMessage() {

        Map<String, Object> ipMacInfo = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface
                        .getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    ipMacInfo = pickInetAddress(inetAddress, networkInterface);
                    if (ipMacInfo != null) {

                        return ipMacInfo;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Object> pickInetAddress(InetAddress inetAddress,
                                                       NetworkInterface ni) {
        try {
            String name = ni.getDisplayName();
            if (name.contains("Adapter")
                    || name.contains("Virtual") || name.contains("VMnet") || name.contains("#")) {
                return null;
            }
            if (ni.isVirtual() || !ni.isUp() || !ni.supportsMulticast()) {
                return null;
            }

            if (inetAddress.isSiteLocalAddress()) {
                Formatter formatter = new Formatter();
                String sMAC = null;
                byte[] macBuf = ni.getHardwareAddress();
                for (int i = 0; i < macBuf.length; i++) {
                    sMAC = formatter.format(Locale.getDefault(), "%02X%s",
                            macBuf[i], (i < macBuf.length - 1) ? "-" : "")
                            .toString();
                }
                formatter.close();
                Map<String, Object> ipMacInfo = new HashMap<String, Object>();
                ipMacInfo.put("hostname", inetAddress.getHostName()); //系统当前hostname
                ipMacInfo.put("ip", inetAddress.getHostAddress()); //ip地址
                ipMacInfo.put("ipnet", inetAddressTypeName(inetAddress)); //网络类型
                ipMacInfo.put("os", System.getProperty("os.name")); //系统名称
                ipMacInfo.put("mac", sMAC); //mac 地址
                ipMacInfo.put("cpu-arch", System.getProperty("os.arch")); //cpu架构
                ipMacInfo.put("network-arch", ni.getDisplayName()); //网卡名称
                return ipMacInfo;
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String inetAddressTypeName(InetAddress inetAddress) {
        return (inetAddress instanceof Inet4Address) ? "ipv4" : "ipv6";
    }

    public static String inetAton(long add) {
        return ((add & 0xff000000) >> 24) + "." + ((add & 0xff0000) >> 16)
                + "." + ((add & 0xff00) >> 8) + "." + ((add & 0xff));
    }

    public static long inetAton(Inet4Address add) {
        byte[] bytes = add.getAddress();
        long result = 0;
        for (byte b : bytes) {
            if ((b & 0x80L) != 0) {
                result += 256L + b;
            } else {
                result += b;
            }
            result <<= 8;
        }
        result >>= 8;
        return result;
    }

    /**
     * significantly faster than parse the string into long
     */
    public static long inetAton(String add) {
        long result = 0;
        // number between a dot
        long section = 0;
        // which digit in a number
        int times = 1;
        // which section
        int dots = 0;
        for (int i = add.length() - 1; i >= 0; --i) {
            if (add.charAt(i) == '.') {
                times = 1;
                section <<= dots * 8;
                result += section;
                section = 0;
                ++dots;
            } else {
                section += (add.charAt(i) - '0') * times;
                times *= 10;
            }
        }
        section <<= dots * 8;
        result += section;
        return result;
    }

    public static String postJsonConnect(String url, Object addEntity) {
        HttpClient httpClient = new HttpClient();

        try {
            HttpMethod method = postMethod(url, addEntity);
            httpClient.executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getConnect(String url) {
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod(url);
        try {
            httpClient.executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO 未测试
    public static HttpMethod postSimpleConnect(String url, Map<String, String> params) {
        PostMethod method = new PostMethod(url);
        if(!MapUtils.isEmpty(params)) {
            NameValuePair pairs[] = new NameValuePair[params.size()];
            int index = 0;
            for(Map.Entry<String, String> param : params.entrySet()) {
                pairs[index++] = new NameValuePair(param.getKey(), param.getValue());
            }
        }
        return method;
    }

    public static HttpMethod postMethod(String url, Object addEntity) throws IOException {

        PostMethod method = new PostMethod(url);
        setJsonParameter(method, addEntity);
        return method;
    }

    public static void setJsonParameter(PostMethod method, Object addEntity) {

        try {
            if(addEntity instanceof List) {
                JSONArray jsonArray = JSONArray.fromObject(addEntity);
                method.setRequestEntity(new StringRequestEntity(
                        jsonArray.toString(), "application/json", "UTF-8"));
            } else {
                JSONObject jsonObject = JSONObject.fromObject(addEntity);
                method.setRequestEntity(new StringRequestEntity(
                        jsonObject.toString(), "application/json", "UTF-8"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        method.setRequestHeader("Content-Type","application/json; charset=utf-8");
        method.setRequestHeader("Accept", "application/json");
    }
}
