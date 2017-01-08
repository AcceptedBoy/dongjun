package com.gdut.dongjun.constant;

/**
 * 常量
 */
public class Constant {

    /** 日志读路径 */
    public static final String LOGGER_FILE = "/file/myApp.txt";

    /** 输出格式化的txt路径 */
    public static final String OUTPUT_FILE = "/file/output.txt";

    /** 要进行判断开始切割的字符串 */
    public static final String JUDGE_START = "接收到的报文： ";

    /** 日志读的格式 */
    public static final String READ_CHARSET = "gb2312";

    /** 日志写的格式 */
    public static final String WRITE_CHARSET = "utf-8";

    /** 发送ip */
    //public static String SEND_IP ="115.28.7.40";
    public static String SEND_IP = "192.168.56.1";

    /** 发送端口号 */
    public static int SEND_PORT = 8464;

    /** 发送报文时间 */
    public static final long AVERAGE_SEND_SECOND = 1000;
}
