package com.gdut.dongjun.test.bean;

public class MyService implements IService {

	private IMode mode;

	public MyService() {

	}

	public MyService(IMode mode) {
		this.mode = mode;
	}

	public IMode getMode() {
		return mode;
	}

	public String toString() {
		if (mode == null) {
			return "none mode in service";
		}
		return "bingo";
	}
}