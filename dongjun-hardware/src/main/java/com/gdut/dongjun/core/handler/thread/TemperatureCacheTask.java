package com.gdut.dongjun.core.handler.thread;

import com.gdut.dongjun.core.TemperatureCtxStore;

/**
 * 有这个任务是因为平台系统的SqlSessionTemplate默认不会自动提交，
 * 导致平台系统做出修改并通过cxf调用服务的时候，数据库还没有更新，
 * 所以弄了这个延迟任务。
 * 以后再想恰当的方法 TODO
 * @author Gordan_Deng
 * @date 2017年5月21日
 */
public class TemperatureCacheTask extends ScheduledTask {
	
	private String id;
	private TemperatureCtxStore ctxStore;

	public TemperatureCacheTask(String id, TemperatureCtxStore ctxStore) {
		super(10);
		this.ctxStore = ctxStore;
		this.id = id;
	}

	@Override
	public void run() {
		ctxStore.setBound(id);
	}

}
