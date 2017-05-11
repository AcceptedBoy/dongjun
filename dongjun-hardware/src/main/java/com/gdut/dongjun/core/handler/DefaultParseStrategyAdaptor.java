package com.gdut.dongjun.core.handler;

import java.util.List;

public class DefaultParseStrategyAdaptor extends ParseStrategyAdaptor {

	private ParseStrategy parseStrategy;
	
	@Override
	public boolean applyParseStrategy(ParseStrategy strategy) {
		this.parseStrategy = strategy;
		return true;
	}

	@Override
	public List<Object> doParse(char[] data) {
		return parseStrategy.parse(data);
	}

}
