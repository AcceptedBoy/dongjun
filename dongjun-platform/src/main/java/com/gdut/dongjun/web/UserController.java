package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.domain.model.ErrorInfo;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.domain.po.PersistentHitchMessage;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.authc.Role;
import com.gdut.dongjun.domain.po.authc.UserRole;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.PersistentHitchMessageService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.RemoteEventService;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.UserLogService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.RoleService;
import com.gdut.dongjun.service.authc.UserRoleService;
import com.gdut.dongjun.service.impl.enums.LoginResult;
import com.gdut.dongjun.service.thread.manager.DefaultThreadManager;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;
import com.gdut.dongjun.web.vo.HitchEventVO;

@Controller
@SessionAttributes("currentUser")
public class UserController {

	private static final String PLATFORM_ADMIN = "platform_group_admin";

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
	@Autowired
	private PersistentHitchMessageService messageService;
	@Autowired
	private RemoteEventService hitchEventService;
	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private UserDeviceMappingService deviceMappingService;

	private static final Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "/dongjun")
	public String dongjun() {

		return "login";
	}

	@RequestMapping(value = "/dongjun/login")
	public String login() {

		return "login";
	}

	@RequestMapping(value = "/dongjun/elecon/login_form")
	@ResponseBody
	public Object loginForm(String name, String password, Model model, RedirectAttributes redirectAttributes,
			HttpSession session) throws JMSException {

		//这一步扔到ApplicationContext吧
		SecurityUtils.setSecurityManager(manager);
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(name, password);
		token.setRememberMe(true);	//这个项目好像暂时不需要记住我的功能

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
			userLogService.createNewLog(user);
			// 记录在线用户
			userService.remarkLogIn(user);
			// if no exception, that's it, we're done!
			// 登陆成功后，等前端页面加载完成，5秒后返回未读的报警消息
			DefaultThreadManager.delayExecute(new Runnable() {

				@Override
				public void run() {
					List<PersistentHitchMessage> list = messageService.getAllUnreadHitchMessage(user.getId());
					List<HitchEventVO> vos = new ArrayList<HitchEventVO>();
					for (PersistentHitchMessage message : list) {
						int type = message.getType() / 100;
						switch (type) {
						case 3: {
							HitchEventVO vo = warpIntoVO(message);
							vos.add(vo);
							break;
						}
						default:
							break;
						}
					}
					for (HitchEventVO vo : vos) {
						template.convertAndSend("/queue/user-" + user.getId() + "/hitch", vo);
					}
				}

				private HitchEventVO warpIntoVO(PersistentHitchMessage message) {
					HitchEventDTO dto = new HitchEventDTO();
					dto.setId(message.getHitchId());
					dto.setGroupId(user.getCompanyId());
					dto.setMonitorId(message.getMonitorId());
					dto.setGroupId(message.getGroupId());
					dto.setType(message.getType());
					return hitchEventService.wrapHitchVO(dto);
				}

			}, 5);
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
		} catch (JMSException e) {
			// 注销已登录用户
			currentUser.logout();
			// 抛出至ExceptionHandler
			throw e;
		}
		return LoginResult.LOGIN_PASS.value();
	}

	@RequiresAuthentication
	@RequestMapping(value = "/dongjun/logout")
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
				new ErrorInfo(request.getRequestURL().toString(), "缺少权限", HttpStatus.UNAUTHORIZED.value(), "缺少权限")),
				HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping("/islogin")
	@ResponseBody
	public HashMap<String, Object> isLogin(HttpSession session) {
		User user = (User) session.getAttribute("currentUser");
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (user == null) {
			map.put("isLogin", "false");
			return map;
		}
		map.put("isLogin", "true");
		if (null != session.getAttribute("currentRoles")) {
			map.put("currentRoles", session.getAttribute("currentRoles"));
		} else {
			List<Role> roles = roleService.selectByUserId(user.getId());
			List<String> roleName = new ArrayList<String>();
			for (Role r : roles) {
				roleName.add(r.getRole());
			}
			session.setAttribute("currentRoles", roleName);
			map.put("currentRoles", roleName);
		}
		map.put("currentUser", user.getId());
		return map;
	}

	/**
	 * TODO涉及多个表的修改，考虑添加事务
	 * 
	 * @param user
	 * @param mainStaff
	 * @return
	 */
	@RequestMapping(value = "/dongjun/elecon/user_registy", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public ResponseMessage doRegisty(User user) {
		List<Role> roles = roleService.selectByParameters(null);
		user.setId(UUIDUtil.getUUID());
		user.setAvailable(1);
		if (userService.updateByPrimaryKey(user) == 1) {
			UserRole ur = new UserRole();
			for (Role role : roles) {
				if ("user".equals(role.getRole())) {
					ur.setRoleId(role.getId());
					break;
				}
			}
			ur.setUserId(user.getId());
			urService.insert(ur);

		} else {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}

	@InitBinder("company")
	public void initAccountBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("company.");
	}

	@InitBinder("user")
	public void initUserBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("user.");
	}

	@RequestMapping(value = "/dongjun/elecon/company_registry", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public ResponseMessage edit(
			@ModelAttribute("company") Company com, BindingResult result1,  
			@ModelAttribute("user") User user, BindingResult result2) {
		// 注册用户并赋予公司管理员角色
		List<Role> roles = roleService.selectByParameters(null);
		
		PlatformGroup pg = new PlatformGroup();
		pg.setId(UUIDUtil.getUUID());
		pg.setGroupId("default"); // 默认组别
		byte num = 0;
		pg.setIsDefault(num);
		pg.setName(com.getName());
		pg.setCompanyId(com.getId());
		pg.setType(num);
		user.setCompanyId(pg.getId());
		user.setId(UUIDUtil.getUUID());
		//注册公司
		com.setId(UUIDUtil.getUUID());
		com.setMainStaffId(user.getId());
		pg.setCompanyId(com.getId());

		if (userService.updateByPrimaryKey(user) == 1) {
			UserRole ur = new UserRole();
			for (Role role : roles) {
				if (PLATFORM_ADMIN.equals(role.getRole())) {
					ur.setRoleId(role.getId());
					break;
				}
			}
			ur.setUserId(user.getId());
			urService.insert(ur);
		} else {
			logger.warn("Exception : 注册用户失败");
		}
		if (0 == pgService.updateByPrimaryKey(pg)) {
			return ResponseMessage.danger("操作失败");
		}
		if (companyService.updateByPrimaryKey(com) == 0) {
			return ResponseMessage.danger("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}

	@RequiresAuthentication
	@RequestMapping("/dongjun/user/information")
	@ResponseBody
	public ResponseMessage getPersonalImformation(HttpSession session) {
		User user = userService.selectByPrimaryKey(((User) session.getAttribute("currentUser")).getId());
		return ResponseMessage.success(user);
	}

	/**
	 * 只允许修改用户
	 * @param user
	 * @param session
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/dongjun/user/edit")
	@ResponseBody
	public ResponseMessage editUser(User user, HttpSession session) {
		if (null == user.getId()) {
			return ResponseMessage.warning("操作失败");
		}
		if (0 == userService.updateByPrimaryKeySelective(user)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success(user);
	}
	
	@RequiresRoles("super_admin")
	@RequestMapping("/dongjun/admin/user/list")
	@ResponseBody
	public ResponseMessage listUser(String platformId) {
		return ResponseMessage.success(userService.selectByParameters(MyBatisMapUtil.warp("company_id", platformId)));
	}
	
	@RequiresRoles("super_admin")
	@RequestMapping("/dongjun/admin/user/del")
	@ResponseBody
	public ResponseMessage delUser(String id) {
		deviceMappingService.deleteByParameters(MyBatisMapUtil.warp("user_id", id));
		if (!userService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		userLogService.deleteByParameters(MyBatisMapUtil.warp("user_id", id));
		return ResponseMessage.success("操作成功");
	}
	
	

	@RequestMapping(value = "/dongjun/elecon/fuzzy_search", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage doFuzzySearch(String name) {
		return ResponseMessage.success(pgService.fuzzySearch(name));
	}

}
