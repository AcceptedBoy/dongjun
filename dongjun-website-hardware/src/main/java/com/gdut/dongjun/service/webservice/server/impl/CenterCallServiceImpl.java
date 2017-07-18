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
		List<String> hexAddr = new ArrayList<String>();
		for (String addr : addrs) {
			char[] char_addr = Integer.toHexString(Integer.parseInt(addr)).toCharArray();
			char[] address = new char[4];
			if (char_addr.length != 4) {
				for (int i = 0; i < 4; i++) {
					if (i < char_addr.length) {
						address[i] = char_addr[i];
					} else {
						address[i] = 0;
					}
				}
			}
			String address1 = String.valueOf(address);
			address1  = HighVoltageDeviceCommandUtil.reverseString(address1);
			hexAddr.add(address1);
		}
		ctxStore.setAvailableAddrList(hexAddr);
	}
	
	private Object removeCtxAttribute(ChannelHandlerContext ctx, String name) {
		AttributeKey<Object> key = AttributeKey.valueOf(name);
		Attribute<Object> attr = ctx.attr(key);
		Object temp = attr.get();
		attr.remove();
		return temp;
	}

}
