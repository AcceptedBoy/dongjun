package com.gdut.dongjun.service;

import com.gdut.dongjun.core.CtxStore;

public class ChangingSwitchExpireTask extends ScheduledTask {

	private static final int EXPIRED_TIME = 60 * 1;
	private String address;
	
	public ChangingSwitchExpireTask(String address) {
		super(EXPIRED_TIME);
		this.address = address;
	}

	@Override
	public void run() {
		CtxStore.removeChangingSwitch(address);
	}

}
