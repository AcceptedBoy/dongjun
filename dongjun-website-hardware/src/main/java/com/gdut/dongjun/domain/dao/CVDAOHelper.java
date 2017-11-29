package com.gdut.dongjun.domain.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.service.HighVoltageCurrentService;
import com.gdut.dongjun.service.HighVoltageVoltageService;

@Component
public class CVDAOHelper extends Thread implements InitializingBean {

	@Autowired
	private HighVoltageVoltageService voltageService;
	@Autowired
	private HighVoltageCurrentService currentService;
	public static BaseCache<HighVoltageVoltage> voltageCache = new BaseCache<HighVoltageVoltage>();
	public static BaseCache<HighVoltageCurrent> currentCache = new BaseCache<HighVoltageCurrent>();
	private static Object vLock = new Object();
	private static Object cLock = new Object();
	private int SLEEP = 10;
	
	public CVDAOHelper() {}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		start();
	}
	
	public static void add(HighVoltageVoltage v) {
		synchronized(vLock) {
			voltageCache.add(v);
		}
	}
	
	public static void add(HighVoltageCurrent c) {
		synchronized(cLock) {
			currentCache.add(c);
		}
	}

	@Override
	public void run() {
		while (true) {
			flush();
			try {
				Thread.sleep(1000 * SLEEP);
			} catch(InterruptedException e) {}
		}
	}

	private void flush() {
		flushV();
		flushC();
	}
	
	private void flushV() {
		ArrayList<HighVoltageVoltage> list = null;
		synchronized(vLock) {
			if (voltageCache.currBuf == 'A') {
				list = voltageCache.bufA;
				voltageCache.currBuf = 'B';
			} else if (voltageCache.currBuf == 'B') {
				list = voltageCache.bufB;
				voltageCache.currBuf = 'A';
			}
		}
		voltageService.insertMulti(list);
	}
	
	private void flushC() {
		ArrayList<HighVoltageCurrent> list = null;
		synchronized(cLock) {
			if (currentCache.currBuf == 'A') {
				list = currentCache.bufA;
				currentCache.currBuf = 'B';
			} else if (currentCache.currBuf == 'B') {
				list = currentCache.bufB;
				currentCache.currBuf = 'A';
			}
		}
		currentService.insertMulti(list);
	}
	
}

class BaseCache<V> {
	
	public ArrayList<V> bufA = new ArrayList<V>();
	public ArrayList<V> bufB = new ArrayList<V>();
	public char currBuf = 'A';
	
	public void add(V value) {
		
		if (currBuf == 'A') {
			bufA.add(value);
		} else if (currBuf == 'B') {
			bufB.add(value);
		}
	}
	
}
