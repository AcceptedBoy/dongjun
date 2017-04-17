package com.gdut.dongjun.core.handler.thread;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;

/**
 * TODO
 * 应该会有更好的设计
 * @author Gordan_Deng
 * @date 2017年3月4日
 */
@Component
public class HitchEventManager {
	
	@Autowired
	private TemperatureHitchEventThreadFactory temHitchEventThreadFactroy;

	protected static ExecutorService fixedPool = ThreadPoolHolder.fixedPool;
	
	/**
	 * 添加温度报警事件
	 * @param event
	 */
	public void addHitchEvent(TemperatureMeasureHitchEvent event) {
		fixedPool.execute(temHitchEventThreadFactroy.buildHitchEventThread(event));
	}
}
