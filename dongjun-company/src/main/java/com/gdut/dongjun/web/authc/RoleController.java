package com.gdut.dongjun.web.authc;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.authc.UserRoleVo;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.RoleService;
import com.gdut.dongjun.service.authc.UserRoleService;

@RestController
@RequestMapping("/dongjun/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	
	@RequestMapping("get_roles_from_user")
	public ResponseMessage getRolesByUser(HttpSession session) {
		
		User user = (User)session.getAttribute("currentUser");
		if(user != null) {
			return ResponseMessage.success(roleService.selectByUserId(user.getId()));
		} else {
			return ResponseMessage.success(null);
		}
	}
	
	@RequestMapping("/user_role_manager")
	public ResponseMessage userRoleManager() {
		
		return ResponseMessage.success(UserRoleVo.createList(
				userService.selectByParameters(null), 
				roleService.selectByParameters(null), 
				userRoleService.selectByParameters(null)));
	}

}
