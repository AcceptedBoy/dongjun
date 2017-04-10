package com.gdut.dongjun.service.impl;

import javax.jms.JMSException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.UserMapper;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.service.manager.UserHolder;
import com.gdut.dongjun.service.mq.UserMQService;

/**
 * @Title: UserServiceImpl.java
 * @Package com.gdut.dongjun.service.impl.system
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年7月24日 下午2:33:08
 * @version V1.0
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements
		UserService {
	
//	private static final List<User> allCurrentUsers = new CopyOnWriteArrayList<User>();
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserMQService mqService;


	@Override
	protected boolean isExist(User record) {
		if (record != null
				&& userMapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public User getCurrentUser(HttpSession session) {
		return (User)session.getAttribute("currentUser");
	}

	@Override
	public boolean isUserOnline(String id) {
		return UserHolder.isUserLogin(id);
	}
	
	/**
	 * 记录用户登录
	 */
	public void remarkLogIn(User user) throws JMSException {
		logger.info("用户登录 : " + user.getName());
		if (UserHolder.isUserLogin(user.getId())) {
			return ;
		}
		//记录在线用户
		UserHolder.addUser(user);
		//注册User的MessageHolder，功能暂时考虑放弃
//		mqService.remarkLogIn(user);
	}
	
	/**
	 * 记录用户注销
	 */
	public void remarkLogOut(User user) {
		logger.info("用户注销 : " + user.getName());
		UserHolder.delUser(user);
	}

}
