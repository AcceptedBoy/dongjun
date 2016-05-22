package com.gdut.dongjun.test;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestClass {	
	
	public Jedis jedis = null;

	@Test
	public void testOne() throws Exception {
		
		RedisFactory redisFactory = new RedisFactory();
		redisFactory.setPoolConnectTimeOut(15000);
		redisFactory.setPoolIp("127.0.0.1");
		redisFactory.setPoolMaxIdel(8);
		redisFactory.setPoolMaxWaitMillis(15000);
		redisFactory.setPoolPassword("root");
		redisFactory.setPoolPort(6379);
		redisFactory.setPoolTestOnBorrow(true);
		redisFactory.init();
		System.out.println(redisFactory);
		System.out.println(redisFactory.getJedis());
		jedis = redisFactory.getJedis();
		People people = new People();
		people.setId("1");
		people.setName("2");
		System.out.println(jedis);
		
		jedis.set("key".getBytes(), SerializeUtil.serialize(people));
		System.out.println(jedis.get("key").getBytes());
		System.out.println((People) SerializeUtil.unserialize(jedis.get("key".getBytes())));
	}
}
