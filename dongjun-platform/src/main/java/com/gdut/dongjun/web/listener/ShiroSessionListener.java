package com.gdut.dongjun.web.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.manager.SpringApplicationContextHolder;

/**
 * 能做到同样功能的类：
 * HttpSessionListener
 * ApplicationListener<SessionDisconnectEvent>
 * @author Gordan_Deng
 * @date 2017年3月29日
 */
@Component
public class ShiroSessionListener implements SessionListener, InitializingBean {
	
	@Autowired
	private UserService userService;
	
	private Logger logger = Logger.getLogger(ShiroSessionListener.class);

	@Override
	public void onStart(Session session) {
		System.out.println("session注册");
		if (null != userService) {
			System.out.println("userService成功注入!!");
		}
	}

	@Override
	public void onStop(Session session) {
		logger.info("session销毁");
		System.out.println("session类型" + session.getClass().getName());
		session.removeAttribute("currentUser");
		System.out.println("用户直接关掉网页或者浏览器");
	}

	@Override
	public void onExpiration(Session session) {}

	/**
	 * 为session管理器添加当前的session监听器
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		DefaultWebSessionManager manager = SpringApplicationContextHolder.getBean(DefaultWebSessionManager.class);
		ShiroSessionListener listener = SpringApplicationContextHolder.getBean(ShiroSessionListener.class);
		List<SessionListener> listeners = new ArrayList<SessionListener>();
		listeners.add(listener);
		manager.setSessionListeners(listeners);
	}

}
