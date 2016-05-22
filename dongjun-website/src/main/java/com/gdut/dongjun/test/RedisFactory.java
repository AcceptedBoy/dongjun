package com.gdut.dongjun.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis工厂
 * 
 * Redis数据结构 与 对应的使用场景：
 * 
 * 1、key-value ：当数据的缓存使用 
 * 2、List 		：特点为合先进先出，可当消息队列使用 
 * 3、Set		：特点为Set中的元素不能一样，可用来统计有多少个IP访问我们的应用，也可以用来作为存放"敏感词"的仓库 
 * 4、Sort Set	：特点为有序集合，可用来做游戏排名
 * 5、Hash		：类似String类型的key-value对，特点为value可以是一个HashMap
 * 
 * 
 * 1、key-value 对应API前缀：无
 * 2、List		对应API前缀：r、l
 * 3、Set		对应API前缀：s
 * 4、Sort Set  对应API前缀：z
 * 5、Hash		对应API前缀：h
 * 
 * API	:	http://tool.oschina.net/apidocs/apidoc?api=jedis-2.1.0
 * 入门	：	http://www.tuicool.com/articles/vaqABb
 * 
 * @author xiaoMzjm
 * 
 */
public class RedisFactory {

	// 本地的jedisPool
	private static JedisPool localJedisPool = null;
	
	// 日志
	private static Logger log = LoggerFactory.getLogger(RedisFactory.class);
	
	// 以下参数值由spring注入
	private int poolMaxIdel;			// 控制一个pool最多有多少个状态为空闲的的jedis实例，默认值也是8。
	private int poolMaxWaitMillis;		// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private boolean poolTestOnBorrow;	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private String poolIp;				// redis服务器IP地址
	private int poolPort;				// redis服务的端口
	private int poolConnectTimeOut;		// 连接超时时间，单位毫秒
	private String poolPassword;		// redis服务的密码
	
	// 初始化Jedis池
	public void init(){
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(poolMaxIdel); 
			config.setMaxWaitMillis(poolMaxWaitMillis); 
			config.setTestOnBorrow(poolTestOnBorrow); 
			localJedisPool = new JedisPool(config, poolIp, poolPort, poolConnectTimeOut,
					poolPassword);
		} catch (Exception e) {
			log.error("ERROR:",e);
		}
	}

	/**
	 * 获取原生Jedis实例
	 * @return
	 */
	public synchronized Jedis getJedis() throws Exception{
		if (localJedisPool != null) {
			Jedis jedis = localJedisPool.getResource();
			return jedis;
		} else {
			return null;
		}
	}
	

	// get & set
	public int getPoolMaxIdel() {
		return poolMaxIdel;
	}

	public void setPoolMaxIdel(int poolMaxIdel) {
		this.poolMaxIdel = poolMaxIdel;
	}

	public int getPoolMaxWaitMillis() {
		return poolMaxWaitMillis;
	}

	public void setPoolMaxWaitMillis(int poolMaxWaitMillis) {
		this.poolMaxWaitMillis = poolMaxWaitMillis;
	}

	public boolean isPoolTestOnBorrow() {
		return poolTestOnBorrow;
	}

	public void setPoolTestOnBorrow(boolean poolTestOnBorrow) {
		this.poolTestOnBorrow = poolTestOnBorrow;
	}

	public String getPoolIp() {
		return poolIp;
	}

	public void setPoolIp(String poolIp) {
		this.poolIp = poolIp;
	}

	public int getPoolPort() {
		return poolPort;
	}

	public void setPoolPort(int poolPort) {
		this.poolPort = poolPort;
	}

	public int getPoolConnectTimeOut() {
		return poolConnectTimeOut;
	}

	public void setPoolConnectTimeOut(int poolConnectTimeOut) {
		this.poolConnectTimeOut = poolConnectTimeOut;
	}

	public String getPoolPassword() {
		return poolPassword;
	}

	public void setPoolPassword(String poolPassword) {
		this.poolPassword = poolPassword;
	}
}