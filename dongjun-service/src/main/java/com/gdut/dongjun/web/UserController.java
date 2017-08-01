package com.gdut.dongjun.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.po.User;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.web.vo.LoginResult;
import com.gdut.dongjun.web.vo.ResponseMessage;

@RequestMapping("/dongjun")
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private org.apache.shiro.mgt.SecurityManager manager;
	private static Logger logger = Logger.getLogger(UserController.class);

	@ResponseBody
	@RequestMapping("/login_form")
	public String loginForm(String name, String password, HttpSession session) {
		//这一步扔到ApplicationContext吧
		SecurityUtils.setSecurityManager(manager);
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(name, password);
		Map<String, Object> map = MyBatisMapUtil.warp("name", name);
		map.put("password", password);

		List<User> users = userService.selectByParameters(map);
		final User user;

		// 数据库查找账号密码
		if (null != users && 0 != users.size() && null != users.get(0)) {

			user = users.get(0);
		} else {
			return LoginResult.USER_NO_EXIST.value();
		}

		try {
			currentUser.login(token);
			session.setAttribute("currentUser", user);
			SecurityUtils.getSubject().getSession().setTimeout(-1000l);
		} catch (UnknownAccountException uae) {
			// username wasn't in the system, show them an error message?
			return LoginResult.USER_NO_EXIST.value();
		} catch (IncorrectCredentialsException ice) {
			// password didn't match, try again?
			return LoginResult.WORNING_PASSWORD.value();
		} catch (LockedAccountException lae) {
			// account for that username is locked - can't login. Show them a
			// message?
			logger.error("login error!");
		} catch (AuthenticationException ae) {
			// unexpected condition - error?
			logger.error("login error!");
		}
		return LoginResult.LOGIN_PASS.value();

	}

	@ResponseBody
	@RequestMapping("/logout")
	public ResponseMessage logout(String name, String password, HttpSession session) {
		Subject sub = SecurityUtils.getSubject();
		sub.logout();
		return ResponseMessage.success("success");
	}

}
