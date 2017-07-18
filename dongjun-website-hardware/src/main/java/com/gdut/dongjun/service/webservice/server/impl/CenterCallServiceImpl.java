package com.gdut.dongjun.service.webservice.server.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.core.HighVoltageCtxStore;
import com.gdut.dongjun.core.SwitchGPRS;
import com.gdut.dongjun.service.webservice.server.CenterCallService;
import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

@Service
public class CenterCallServiceImpl implements CenterCallService {
	
	@Autowired
	private HighVoltageCtxStore ctxStore;
	
	@Override
	public void updateSwitchAddressAvailable(List<String> addrs) {
		//10进制的数字转换为16进制
		List<String> realAddr = new ArrayList<String>();
		for (String addr : addrs) {
			String hexAddr = String.valueOf(Integer.toHexString(Integer.parseInt(addr)));
			StringBuilder sb = new StringBuilder();
			if (hexAddr.length() < 4) {
				for (int i = 0; i < 4 - hexAddr.length(); i++) {
					sb.append("0".intern());
				}
			}
			sb.append(hexAddr);
			String address = HighVoltageDeviceCommandUtil.reverseString(
					sb.toString());
			realAddr.add(address);
		}
		ctxStore.setAvailableAddrList(realAddr);
	}

}
