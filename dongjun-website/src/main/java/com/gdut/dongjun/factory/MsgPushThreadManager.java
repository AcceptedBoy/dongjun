package com.gdut.dongjun.factory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author AcceptedBoy
 *
 */
public class MsgPushThreadManager {

	private static ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(10);
	
	/**
	 * 因为在业务中，一个用户id可能有多个线程同时运行，所以值采用list容器
	 */
	private static Map<String, LinkedList<ScheduledFuture<?>>> scheduledMap = new HashMap<>(100);
	
	private static int initialDelay = 0;
	
	private int intervalSecond = 10;
	
	public MsgPushThreadManager(){}
	
	public MsgPushThreadManager(int intervalSecond) {
		this.intervalSecond = intervalSecond;
	}
	
	/**
	 * 对新的用户初始化linkedList
	 */
	private static void init(String userId) {
		LinkedList<ScheduledFuture<?>> futures = scheduledMap.get(userId);
		if(futures == null) {
			scheduledMap.put(userId, new LinkedList<ScheduledFuture<?>>());
		}
	}
	
	/**
	 * {@code Deprecated: }因为用户的线程都托管到这里，所以需要用户id来进行线程的停止
	 */
	@Deprecated
	public static ScheduledFuture<?> createScheduledPoolDaemonThread(Runnable r, int second) {
		return scheduledPool.scheduleWithFixedDelay(r, initialDelay, second, TimeUnit.SECONDS);
	}
	
	/**
	 * {@code Deprecated: }因为用户的线程都托管到这里，所以需要用户id来进行线程的停止
	 */
	@Deprecated
	public static ScheduledFuture<?> createScheduledPoolDaemonThread(Runnable r, int second, int initialDelay) {
		return scheduledPool.scheduleWithFixedDelay(r, initialDelay, second, TimeUnit.SECONDS);
	}
	
	/**
	 * 创建定时任务，时间间隔为{@code second}
	 */
	public static void createScheduledPoolDaemonThread(Runnable r, int second, String userId) {
		init(userId);
		//保持引用
		scheduledMap.get(userId).add(scheduledPool.scheduleWithFixedDelay
				(r, initialDelay, second, TimeUnit.SECONDS));
	}
	
	public static void createScheduledPoolDaemonThread(Runnable r, int second, int initialDelay, String userId) {
		init(userId);
		scheduledMap.get(userId).add(scheduledPool.scheduleWithFixedDelay
				(r, initialDelay, second, TimeUnit.SECONDS));
	}
	
	public static void finishScheduledByUser(String userId) {
		LinkedList<ScheduledFuture<?>> futures = scheduledMap.get(userId); 
		for(ScheduledFuture<?> future : futures) {
			future.cancel(true);
		}
		removeScheduledByUser(userId);
	}
	
	private static void removeScheduledByUser(String userId) {
		scheduledMap.put(userId, new LinkedList<ScheduledFuture<?>>());
	}
	
	public static void shutdown() {
		scheduledMap = null; //help GC
		scheduledPool.shutdown();
	}
	
	public int getIntervalSecond() {
		return intervalSecond;
	}
}
