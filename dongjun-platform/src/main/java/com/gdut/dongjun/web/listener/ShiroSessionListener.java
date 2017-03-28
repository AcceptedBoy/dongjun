package com.gdut.dongjun.web.listener;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.gdut.dongjun.service.UserService;

public class ShiroSessionListener implements SessionListener {
	
//	private UserService userService;
	
	Logger logger = Logger.getLogger(ShiroSessionListener.class);

	@Override
	public void onStart(Session session) {
		System.out.println("用户访问");
//		if (null != userService) {
			System.out.println("userService成功注入!!");
//		}
	}

	@Override
	public void onStop(Session session) {
		logger.info("session销毁");
		System.out.println("session类型" + session.getClass().getName());
		session.removeAttribute("currentUser");
		System.out.println("用户直接关掉网页或者浏览器");
	}

	@Override
	public void onExpiration(Session session) {
		// TODO Auto-generated method stub
		
	}

}
