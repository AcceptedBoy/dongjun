package com.gdut.dongjun.thread.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author AcceptedBoy
 */
public class SimpleThreadFactory implements ThreadFactory  {

	protected static ExecutorService fixedPool = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() + 1);

	/**
	 * TODO 为线程命名
	 * @param r
	 * @return
     */
	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r);
	}
	
	public Thread newDaemonThread(Runnable r) {
		Thread t = newThread(r);
		t.setDaemon(true);
		return t;
	}

	/**
	 * 创建延迟线程，延迟时间为{@code delay}，单位为秒
	 */
	public Thread createDelayThread(final Runnable r, final int delay) {
		return new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000 * delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					r.run();
				}
			}
		};
	}
}
