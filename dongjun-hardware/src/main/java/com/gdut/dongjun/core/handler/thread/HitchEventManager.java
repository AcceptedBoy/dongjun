package com.gdut.dongjun.core.handler.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 * 应该会有更好的设计
 * @author Gordan_Deng
 * @date 2017年3月4日
 */
public class HitchEventManager {

	protected static ExecutorService fixedPool = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

	public static void addHitchEvent(HitchEventThread eventThread) {
		fixedPool.execute(eventThread);
	}
}
