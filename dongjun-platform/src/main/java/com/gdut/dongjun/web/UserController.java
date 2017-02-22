package com.gdut.dongjun.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdut.dongjun.domain.model.ErrorInfo;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.authc.Role;
import com.gdut.dongjun.domain.po.authc.UserRoleKey;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserLogService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.RoleService;
import com.gdut.dongjun.service.authc.UserRoleService;
import com.gdut.dongjun.service.impl.enums.LoginResult;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@SessionAttributes("currentUser")
public class UserController {
	
	private static final String GUEST = "guest";
	
	private static final String PLATFORM_ADMIN = "platform_group_admin";
	
	private static final String MAINSTAFF = "yes";

	@Autowired
	private UserService userService;
	@Autowired
	private org.apache.shiro.mgt.SecurityManager manager;
	@Autowired
	private UserLogService userLogService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserRoleService urService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private PlatformGroupService pgService;
	
	private static final Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "/dongjun/login")
	public String login() {

		return "login";
	}
	
	@RequestMapping(value = "/dongjun/elecon/login_form")
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
		if (null != users && 0 != users.size() && null != users.get(0)) {

			user = users.get(0);
		} else {
			return LoginResult.USER_NO_EXIST.value(); 
		}

		try {
			currentUser.login(token);
			session.setAttribute("currentUser", user);
			SecurityUtils.getSubject().getSession().setTimeout(-1000l);
			userLogService.createNewLog(user);
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
	
	@RequiresAuthentication
	@RequestMapping(value="/dongjun/logout")
	@ResponseBody
	public Object logout() {
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject != null && subject.isAuthenticated()) {
			subject.logout(); 
		}
		return "";
	}
	
	@RequestMapping("/dongjun/user/unauthorized")
	@ResponseBody
	public ResponseEntity<ResponseMessage> unauthorized(HttpServletRequest request) {
		
		return new ResponseEntity<>(ResponseMessage.addException(
	    		new ErrorInfo(request.getRequestURL().toString(), 
	    						"缺少权限", 
	    						HttpStatus.UNAUTHORIZED.value(), 
	    						"缺少权限")), 
	    		HttpStatus.UNAUTHORIZED);
	}
	
	@RequestMapping("/islogin")
	@ResponseBody
	public String isLogin(HttpSession session) {
		User user = (User)session.getAttribute("currentUser");
		if (user == null) {
			return "false";
		}
		return "true";
	}
	
	/**
	 * 注册用户，如果mainStaff为"yes"则为公司创始人，权限变为platform_group_admin，否则是guest
	 * TODO涉及多个表的修改，考虑添加事务
	 * @param user
	 * @param mainStaff
	 * @return
	 */
	@RequestMapping(value = "/dongjun/elecon/user_registy", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage doRegisty(User user, String mainStaff) {
		List<Role> roles = roleService.selectByParameters(null);
		user.setId(UUIDUtil.getUUID());
		if (userService.updateByPrimaryKey(user) == 1) {
			UserRoleKey ur = new UserRoleKey();
			if (MAINSTAFF.equals(mainStaff)) {
				for (Role role : roles) {
					if (PLATFORM_ADMIN.equals(role.getRole())) {
						ur.setRoleId(role.getId());
						break;
					}
				}
			} else {
				for (Role role : roles) {
					if (GUEST.equals(role.getRole())) {
						ur.setRoleId(role.getId());
						break;
					}
				}
			}
			ur.setUserId(user.getId());
			urService.insert(ur);
			
			//修改公司的负责人
			if (MAINSTAFF.equals(mainStaff)) {
				Company c = companyService.selectByPrimaryKey(user.getCompanyId());
				c.setMainStaffId(user.getId());
				if (companyService.updateByPrimaryKey(c) == 0) {
					return ResponseMessage.warning("操作失败");
				}
			}
		} else {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequestMapping(value = "/dongjun/elecon/company_registry", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage edit(Company com) {
		if (null == com.getId() || "".equals(com.getId())) {
			com.setId(UUIDUtil.getUUID());
			/* 插入与Company相对应的PlatformGroup */
			PlatformGroup pg = new PlatformGroup();
			pg.setId(UUIDUtil.getUUID());
			pg.setGroupId("default");	//默认组别
			byte num = 0;
			pg.setIsDefault(num);
			pg.setName(com.getName());
			pg.setCompanyId(com.getId());
			pg.setType(num);
			pgService.updateByPrimaryKey(pg);
		}
		if (companyService.updateByPrimaryKey(com) == 0) {
			return ResponseMessage.danger("操作失败");
		}
		return ResponseMessage.success(com.getId());
	}
	
	@RequiresAuthentication
	@RequestMapping("/dongjun/user/imformation")
	@ResponseBody
	public ResponseMessage getPersonalImformation() {
		//TODO
		return null;
	}
	
	@RequiresAuthentication
	@RequestMapping("/dongjun/user/edit")
	@ResponseBody
	public ResponseMessage editUser(User user) {
		//TODO
		return null;
	}
	
	@RequiresRoles("platform_group_admin")
	@RequestMapping("/dongjun/user/del")
	@ResponseBody
	public ResponseMessage delUser() {
		//TODO
		return null;
	}
	
	@RequestMapping(value = "/dongjun/elecon/fuzzy_search", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage doFuzzySearch(String name) {
		return ResponseMessage.success(companyService.fuzzySearch(name));
	}
	
}
