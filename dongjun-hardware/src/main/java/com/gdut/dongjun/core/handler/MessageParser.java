package com.gdut.dongjun.core.handler;

import java.util.List;

public interface MessageParser {

	public List<Object> parse(char[] data);
}
