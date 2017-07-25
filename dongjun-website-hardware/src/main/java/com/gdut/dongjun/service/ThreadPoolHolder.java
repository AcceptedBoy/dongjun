package com.gdut.dongjun.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolHolder {

	public static final ExecutorService fixedPool = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
}
