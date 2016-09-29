package com.gdut.dongjun.thread.factory;

import java.util.concurrent.ThreadFactory;

/**
 * @author AcceptedBoy
 */
public class SimpleThreadFactory implements ThreadFactory  {

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
	
	public Thread createDelayDaemonThread(Runnable r, int delay) {
		return newDaemonThread(createDelayThread(r, delay));
	}
}
