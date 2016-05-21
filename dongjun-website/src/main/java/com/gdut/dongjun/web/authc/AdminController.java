package com.gdut.dongjun.web.authc;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.authc.Role;
import com.gdut.dongjun.domain.po.authc.RolePermission;
import com.gdut.dongjun.domain.po.authc.UserRoleKey;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.PermissionService;
import com.gdut.dongjun.service.authc.RolePermissionService;
import com.gdut.dongjun.service.authc.RoleService;
import com.gdut.dongjun.service.authc.UserRoleService;

/**
 * 这个控制层只有super_admin超级管理员才能进去<p>
 * 
 * 由于这个类的拦截是在<code>Application</code>中拦截的，所以因权限不足不会抛出异常，而是直接给出了401页面，
 * 所以要在<code>Application</code>为授权之后的跳转路径，在后端的路径中再返回应有的json格式给前端
 * 
 * 这个是角色的控制层
 * @author link xiaoMian <972192420@qq.com>
 */
@RestController
@RequestMapping("/dongjun/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private  UserRoleService userRoleService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RolePermissionService rolePermissionService;

	@RequestMapping("/user_manager")
	@RequiresPermissions("super_admin:read_user")
	public ResponseMessage getAllUser() {
		return ResponseMessage.success(userService.selectByParameters(null));
	}
	
	@RequestMapping("/role_manager")
	public ResponseMessage getAllRole() {
		return ResponseMessage.success(roleService.selectByParameters(null));
	}
	
	@RequestMapping("/add_role")
	@RequiresPermissions("super_admin:add_role")
	public ResponseEntity<Void> addRole(String userId, String roleId) {
		insertRole(userId, roleId);
		return null;
	}
	
	@RequestMapping("/add_roles")
	@RequiresPermissions("super_admin:add_role")
	public ResponseEntity<Void> addRoles(String userId, String[] rolesId) {
		for(String roleId : rolesId) {
			insertRole(userId, roleId);
		}
		return null;
	}
	
	@RequestMapping("/add_all_role")
	@RequiresPermissions("super_admin:add_role")
	public ResponseEntity<Void> addAllRoles(String userId) {
		List<Role> roles = roleService.selectByParameters(null);
		for(Role role : roles) {
			insertRole(userId, role.getId());
		}
		return null;
	}
	
	@RequestMapping("/role_of_user")
	@RequiresPermissions("super_admin:read_user")
	public ResponseMessage getRoleByUser(@RequestParam(required=true)String userId) {
		return ResponseMessage.success(roleService.selectByUserId(userId));
	}
	
	@RequestMapping("permission_of_user")
	public ResponseMessage getPermissionByUser(@RequestParam(required=true)String userId) {
		return ResponseMessage.success(permissionService.selectByUserId(userId));
	}
	
	@RequestMapping("permission_of_role")
	public ResponseMessage getPermissionByRole(@RequestParam(required=true)String roleId) {
		return ResponseMessage.success(permissionService.selectByRoleId(roleId));
	}
	
	@RequestMapping("add_permission")
	@RequiresPermissions("super_admin:add_permission")
	public void addPermission(String roleId, String permissionId) {
		insertPermission(roleId, permissionId);
	}
	
	@RequestMapping("add_permissions")
	@RequiresPermissions("super_admin:add_permission")
	public ResponseEntity<Void> addPermissions(String roleId, String[] permissionsId) {
		for(String permissionId : permissionsId) {
			insertPermission(roleId, permissionId);
		}
		return null;
	}
	
	private void insertRole(String userId, String roleId) {
		userRoleService.insert(new UserRoleKey(userId, roleId));
	}
	
	private void insertPermission(String roleId, String permissionId) {
		rolePermissionService.insert(new RolePermission(roleId, permissionId));
	}
}
