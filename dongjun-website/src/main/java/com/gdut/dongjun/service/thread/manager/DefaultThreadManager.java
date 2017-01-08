package com.gdut.dongjun.service.thread.manager;

import com.gdut.dongjun.service.thread.factory.SimpleThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class DefaultThreadManager {

	protected static ScheduledExecutorService scheduledPool =
			Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	
	protected static ExecutorService fixedPool = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() + 1);
	
	protected static SimpleThreadFactory factory = new SimpleThreadFactory();
	
	public static void execute(Runnable r) {
		fixedPool.execute(r);
	}
	
	public static void delayExecute(Runnable r, int delay) {
		fixedPool.execute(factory.createDelayThread(r, delay));
	}
}
