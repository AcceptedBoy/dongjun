package com.gdut.dongjun.thread.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.gdut.dongjun.thread.factory.SimpleThreadFactory;

public class DefaultThreadManager {

	protected static ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(15);
	
	protected static ExecutorService fixedPool = Executors.newFixedThreadPool(20);
	
	protected static SimpleThreadFactory factory = new SimpleThreadFactory();
	
	public static void execute(Runnable r) {
		fixedPool.execute(r);
	}
	
	public static void delayExecute(Runnable r, int delay) {
		fixedPool.execute(factory.createDelayThread(r, delay));
	}
}
