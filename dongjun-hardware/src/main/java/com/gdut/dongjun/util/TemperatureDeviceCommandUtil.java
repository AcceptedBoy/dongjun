package com.gdut.dongjun.util;

import org.springframework.stereotype.Component;

import com.gdut.dongjun.enums.TemperatureControlCode;

@Component
public class TemperatureDeviceCommandUtil extends StringCommonUtil {

	private String address;// 地址
	@SuppressWarnings("unused")
	private String control;// 控制域
	private String dataLength;// 报文长度
	@SuppressWarnings("unused")
	private String data;// 应用服务数据单元ASDU
	private String check;// 帧校验和CS
	
	public TemperatureDeviceCommandUtil() {
		super();
	}
	
	public TemperatureDeviceCommandUtil(String address) {
		this.address = address;
	}
	

	public void setData(String data) {
		int sum = 0, length = 0;
		data = data.replace(" ", "");
		for (int i = 0; i < data.length(); i = i + 2) {
			sum += Integer.parseInt(data.substring(i, i + 2), 16);
			length++;
		}
		sum %= 256;
		this.dataLength = Integer.toHexString(length);
		this.check = Integer.toHexString(sum);
		polish();
	}

	/**
	 * 补齐16进制高位的0
	 */
	public void polish() {
		if (this.dataLength.matches(".")) {
			this.dataLength = "0" + this.dataLength;
		}
		if (this.check.matches(".")) {
			this.check = "0" + this.check;
		}
	}

	/**
	 * 获取设备连接
	 * 
	 * @return
	 */
	public String getConnection() {
		return "" + TemperatureControlCode.CONNECTION;
	}
	
	/**
	 * 返回确认链接
	 * @return
	 */
	public String getConnection0() {
		return "EB90EB90EB90" + address + "16";
	}

	/**
	 * 总召
	 * 
	 * @return
	 */
	public String getTotalCall() {
		String msg = TemperatureControlCode.CONTROL + address + TemperatureControlCode.TOTALCALL + address + "000014";
		this.setData(msg);
		return "68" + this.dataLength + this.dataLength + "68" + msg + this.check + "16";
	}
	
	/**
	 * 对时
	 * 
	 * @param time
	 * @return
	 */
	public String getTimeCheck(String time) {
		String msg = TemperatureControlCode.FIXED_VALUE_CONTROL2.toString() + address + TemperatureControlCode.TIME.toString() + address + "0000" + time;
		this.setData(msg);

		return "68161668" + msg + this.check + "16";
	}

	/**
	 * 主站确认遥信变位
	 * 
	 * @return
	 */
	public String confirmRemoteSignalChannge() {
		String msg = "00" + address;
		this.setData(msg);
		return "10" + msg + this.check + "16";
	}

	/**
	 * 主站确认遥信变位事件
	 * 
	 * @return
	 */
	public String confirmRemoteSignalChangeEvent() {
		String msg = "00" + address;
		this.setData(msg);
		return "10" + msg + this.check + "16";
	}

	/**
	 * 召唤定值
	 * 
	 * @return
	 */
	public String callFixedValue(String parameter) {
		String msg = TemperatureControlCode.FIXED_VALUE_CONTROL + this.address + TemperatureControlCode.FIXED_VALUE
				+ this.address + parameter + TemperatureControlCode.PARAMETER;
		this.setData(msg);

		return "680C0C68" + msg + this.check + "16";
	}

	/**
	 * 主站确认终端上传定值
	 * 
	 * @return
	 */
	public String confirmFixedValue() {
		String msg = "00" + this.address;
		this.setData(msg);
		return "10" + msg + this.check + "16";
	}

	/**
	 * 修改定值
	 * 
	 * @param parameter
	 *            信息体地址 + 参数
	 * @param number
	 *            参数个数
	 * @return
	 */
	public String changeFixedValue(String parameter, String number) {
		String msg = TemperatureControlCode.CONTROL + this.address + TemperatureControlCode.CHANGE_FIXED_VALUE + number
				+ "0601" + this.address + parameter + TemperatureControlCode.PARAMETER;
		this.setData(msg);
		return "68" + this.dataLength + this.dataLength + "68" + msg + this.check + "16";
	}

	/**
	 * 返回定值确认
	 * 
	 * @param parameter
	 * @param number
	 * @return
	 */
	public String confirmFixedValueBack(String parameter, String number) {
		String msg = TemperatureControlCode.FIXED_VALUE_CONTROL2 + this.address
				+ TemperatureControlCode.CHANGE_FIXED_VALUE + number + "0701" + this.address + parameter
				+ TemperatureControlCode.PARAMETER;
		this.setData(msg);
		return "68" + this.dataLength + this.dataLength + "68" + msg + this.check + "16";
	}

	/**
	 * 反转字节
	 * @param data
	 * @return
	 */
	public static String reverseString(String data) {
		char[] data_reverse = data.toCharArray();
		data = "";
		char temp;
		for (int i = 0; i < data_reverse.length / 4; i++) {
			temp = data_reverse[2*i];
			data_reverse[2*i] = data_reverse[data_reverse.length -1 - 2*i - 1];
			data_reverse[data_reverse.length -1 - 2*i] = temp;
			
			temp = data_reverse[2*i + 1];
			data_reverse[2*i + 1] = data_reverse[data_reverse.length -1 - 2*i];
			data_reverse[data_reverse.length -1 - 2*i] = temp;	
		}
		for (int i = 0; i < data_reverse.length; i++) {
			data += data_reverse[i];
		}
		return data;
	}
}
