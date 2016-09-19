package com.gdut.dongjun.web;

import com.gdut.dongjun.domain.model.ErrorInfo;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.impl.enums.LoginResult;
import com.gdut.dongjun.util.MyBatisMapUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dongjun")
@SessionAttributes("currentUser")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private org.apache.shiro.mgt.SecurityManager manager;
	
	private static final Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "/login")
	public String login() {

		return "login";
	}
	
	@RequestMapping(value = "elecon/login_form")
	@ResponseBody
	public Object loginForm(String name, String password, Model model,
			RedirectAttributes redirectAttributes, HttpSession session) {

		SecurityUtils.setSecurityManager(manager);
		Subject currentUser = SecurityUtils.getSubject();

		UsernamePasswordToken token = new UsernamePasswordToken(name, password);
		token.setRememberMe(true);

		Map<String, Object> map = MyBatisMapUtil.warp("name", name);
		map.put("password", password);

		List<User> users = userService.selectByParameters(map);
		User user = null;
		
		// 数据库查找账号密码
		if (CollectionUtils.isNotEmpty(users)) {

			user = users.get(0);
		}

		try {
			currentUser.login(token);
			session.setAttribute("currentUser", user);
			SecurityUtils.getSubject().getSession().setTimeout(-1000l);
			// if no exception, that's it, we're done!
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
	
	@RequestMapping(value="/logout")
	@ResponseBody
	public Object logout() {
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject != null && subject.isAuthenticated()) {
			subject.logout(); 
		}
		return "";
	}
	
	@RequestMapping("/user/unauthorized")
	@ResponseBody
	public ResponseEntity<ResponseMessage> unauthorized(HttpServletRequest request) {
		
		return new ResponseEntity<>(ResponseMessage.addException(
	    		new ErrorInfo(request.getRequestURL().toString(), 
	    						"缺少权限", 
	    						HttpStatus.UNAUTHORIZED.value(), 
	    						"缺少权限")), 
	    		HttpStatus.UNAUTHORIZED);
	}
}
