package com.gdut.dongjun.core.handler.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdut.dongjun.core.handler.msg_decoder.TemperatureDataReceiver;

public abstract class HitchEventThread implements Runnable {
	
	protected static final Logger logger = LoggerFactory.getLogger(HitchEventThread.class);
}
