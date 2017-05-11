package com.gdut.dongjun.core.handler;

import java.util.List;

public abstract class ParseStrategyAdaptor {

	public abstract boolean applyParseStrategy(ParseStrategy strategy);
	
	public abstract List<Object> doParse(char[] data);
}
