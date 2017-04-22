package com.gdut.dongjun.core.handler.thread;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractHitchEvent;

public abstract class HitchEventTaskFactory<T extends AbstractHitchEvent> implements ApplicationContextAware {
	
	protected static ApplicationContext applicationContext;

	/**
	 * 建造报警事件
	 * @return
	 */
	public abstract HitchEventTask<T> buildHitchEventTask(T event);
}
