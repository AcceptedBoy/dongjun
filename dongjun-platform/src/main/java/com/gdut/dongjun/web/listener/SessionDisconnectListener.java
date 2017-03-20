package com.gdut.dongjun.web.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.UserService;

/**
 * 用户session销毁的时候清除User实体
 * 其实可以继承{@code ApplicationListener<SessionDisconnectEvent>}专门监听session断开连接。
 * 但是上述的Event类只能获取到sessionId，还要经过一番步骤才能得到HttpSession，因此作罢。
 * @author Gordan_Deng
 * @date 2017年3月20日
 */
@Component
public class SessionDisconnectListener implements HttpSessionListener {

	@Autowired
	private UserService userService;
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		User user = userService.getCurrentUser(session);
		if (null == user) {
			return ;
		} else {
			userService.remarkLogOut(user);
		}
	}

}
