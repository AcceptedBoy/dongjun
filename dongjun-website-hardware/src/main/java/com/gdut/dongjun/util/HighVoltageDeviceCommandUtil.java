package com.gdut.dongjun.util;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.enums.HighCommandControlCode;

/**
 * 高压设备util类
 * 
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月18日
 * @description
 * @package com.gdut.dongjun.util
 */
@Component
public class HighVoltageDeviceCommandUtil extends StringCommonUtil {

	private String address;// 地址
	@SuppressWarnings("unused")
	private String control;// 控制域
	private String dataLength;// 报文长度
	@SuppressWarnings("unused")
	private String data;// 应用服务数据单元ASDU
	private String check;// 帧校验和CS

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// 预置合闸
	public String closeSwitchPre(String address, String data) {
		
		String msg = HighCommandControlCode.PRE_CONTROL + address
				+ HighCommandControlCode.COMMAND + address + data;
		this.setData(msg);
		return "68" + this.dataLength + this.dataLength + "68" + msg
				+ this.check + "16";
	}

	// 合闸
	public String closeSwitch(String address, String data) {

		String msg = HighCommandControlCode.PRE_CONTROL + address
				+ HighCommandControlCode.COMMAND + address + data;
		this.setData(msg);
		return "68" + this.dataLength + this.dataLength + "68" + msg
				+ this.check + "16";
	}

	// 预置分闸
	public String openSwitchPre(String address, String data) {
	
		String msg = HighCommandControlCode.PRE_CONTROL + address
				+ HighCommandControlCode.COMMAND + address + data;
		this.setData(msg);
		return "68" + this.dataLength + this.dataLength + "68" + msg
				+ this.check + "16";
	}

	// 分闸
	public String openSwitch(String address, String data) {
		
		String msg = HighCommandControlCode.PRE_CONTROL + address
				+ HighCommandControlCode.COMMAND + address + data;
		this.setData(msg);
		return "68" + this.dataLength + this.dataLength + "68" + msg
				+ this.check + "16";
	}

	public void setData(String data) {
		int sum = 0, length = 0;
		
		for (int i = 0; i < data.replace(" ", "").length(); i = i + 2) {
			sum += Integer.parseInt(data.replace(" ", "").substring(i, i + 2),
					16);
			length++;
		}
		sum %= 256;
		this.dataLength = Integer.toHexString(length);
		this.check = Integer.toHexString(sum);
		polish();
	}

	// 补齐16进制高位的0
	public void polish() {
		if (this.dataLength.matches(".")) {
			this.dataLength = "0" + this.dataLength;
		}
		if (this.check.matches(".")) {
			this.check = "0" + this.check;
		}
	}

	// 地址补全
	public String correctAddress(String address) {
		while (address.length() < 4) {
			address = "0" + address;
		}
		if (address.length() < 5) {
			address = address.substring(0, 2) + address.substring(2, 4);
		}
		return address;
	}

	// 字符串反转
	public static String reverseString(String data) {
		char[] data_reverse = data.toCharArray();
		data = "";
		char temp;
		for (int i = 0; i < data_reverse.length / 2; i++) {
			temp = data_reverse[i];
			data_reverse[i] = data_reverse[data_reverse.length / 2 + i];
			data_reverse[data_reverse.length / 2 + i] = temp;
		}
		for (int i = 0; i < data_reverse.length; i++) {
			data += data_reverse[i];
		}
		return data;
	}

	/**
	 * 遥信确定事件
	 */
	public String confirmChangeAffair(String address) {
		
		String msg = "00" + address;
		setData(msg);
		return "68" + msg + this.check + "16";
	}
	
	/**
	 * 发送心跳报文
	 * @param address
	 * @return
	 */
	public String confirmHeart(String address) {
		String code = "72" + address + "68018601" + address
				+ "0000aa55";
		setData(code);
		return "680d0d68" + code + this.check + "16";
	}
	
	/**
	 * 总召激活终止指令
	 */
	public String finshTotalCall(String address) {
		
		String msg = "F4" + address;
		setData(msg);
		return "680C0C68" + msg + "64010A01" + address + "000014" + this.check + "16";
	}

	// 读取电压电流的报文
	public String readVoltageAndCurrent(String address, String data) {

		String msg = HighCommandControlCode.CONTROL + address + data + address
				+ "000014";
		setData(msg);
		return "68" + this.dataLength + this.dataLength + "68" + msg
				+ this.check + "16";
	}

	// 读AB的电压
	public String readABPhaseVoltage(String data) {

		return changToRight(data.substring(30, 36));
	}
	
//	@Test
//	public void t(){
//		
//		System.out.println("68474768d4690009941401690001405659000000000000000000000f00006e0000000000750000000000000000e803008313003b0000000000000000000000000000000000000000000000f616".substring(60, 66));
//		System.out.println(readAPhaseCurrent("68474768d4690009941401690001405659000000000000000000000f00006e0000000000750000000000000000e803008313003b0000000000000000000000000000000000000000000000f616"));
//	}

	// @Test
	// public void test() {
	// System.out.println( readVoltageAndCurrent("b900",
	// HighCommandControlCode.READ_VOLTAGE_CURRENT.toString()));
	// }
	// 将线电压装化为相电压
	public static String changToRight(String data) {

		//以两位为单位，翻转
		return Integer.parseInt(LowVoltageDeviceCommandUtil.reverseStringBy2(data), 16) + "";
	}

	// 读BC电压
	public String readBCPhaseVoltage(String data) {

		return changToRight(data.substring(42, 48));
	}

	public String readCPhaseVoltage(String address) {
		return null;
	}

	// ***************************************************************
	// 电流3个字节长度
	// 读A电流
	// ***************************************************************
	public String readAPhaseCurrent(String data) {

		return changToRight(data.substring(60, 66));
	}

	// 读B电流
	public String readBPhaseCurrent(String data) {
		
		return changToRight(data.substring(66, 72));
	}

	// 读C的电流
	public String readCPhaseCurrent(String data) {
		
		return changToRight(data.substring(72, 78));
	}

	/**
	 * 
	 * @Title: toHexaddress
	 * @Description: 将十进制地址转换成十六进制地址
	 * @param @param address
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String toHexaddress(String address) {

		address = Integer.toHexString(Integer.parseInt(address));
		switch (address.length()) {
		case 1:
			address = "000" + address;
			break;
		case 2:
			address = "00" + address;
			break;
		case 3:
			address = "0" + address;
			break;

		default:
			break;
		}
		return address;
	}
	
	/**
	 * 在不知道设备地址情况下的全域总召
	 * 用0001地址代替源地址
	 * @return
	 */
	public String anonTotalCall() {
		return this.readVoltageAndCurrent("0100", HighCommandControlCode.READ_VOLTAGE_CURRENT
				.toString());
	}
	
	/**
	 * 对时
	 * @param address
	 * @return
	 */
	public String checkTime() {
		Calendar cal = Calendar.getInstance();
		String second = Integer.toHexString(cal.get(Calendar.SECOND) * 1000);
		if (second.length() != 4) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 4 - second.length(); i++) {
				sb.append("0".intern());
			}
			sb.append(second);
			second = reverseString(sb.toString());
		} else {
			second = reverseString(second);
		}
		String minute = Integer.toHexString(cal.get(Calendar.MINUTE));
		String hour = Integer.toHexString(cal.get(Calendar.HOUR));
		String day = Integer.toHexString(cal.get(Calendar.DATE));
		String month = Integer.toHexString(cal.get(Calendar.MONTH) + 1);
		String year = cal.get(Calendar.YEAR) + "";
		year = year.substring(2, 4);
		year = Integer.toHexString(Integer.parseInt(year));
		String time = second
				+ ((minute.length() == 1) ? "0" + minute : minute) + ((hour.length() == 1) ? "0" + hour : hour)
				+ ((day.length() == 1) ? "0" + day : day) + ((month.length() == 1) ? "0" + month : month)
				+ ((year.length() == 1) ? "0" + year : year);
		String msg = "7301006701060101000000" + time;
		this.setData(msg);
		return "68121268" + msg + this.check + "16";
	}
	
}
