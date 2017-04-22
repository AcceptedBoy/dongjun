package com.gdut.dongjun.domain.vo.authc;

import java.util.LinkedList;
import java.util.List;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.authc.Role;
import com.gdut.dongjun.domain.po.authc.UserRole;
import com.gdut.dongjun.domain.po.authc.UserRoleKey;

public class UserRoleVo {

	private String userId;
	
	private String userName;
	
	private List<Role> roles;
	
	public UserRoleVo(User user, List<Role> roles) {
		this.userId = user.getId();
		this.userName = user.getName();
		this.roles = roles;
	}
	
	private static void addRole(UserRoleVo roleVo, Role role) {
		roleVo.getRoleList().add(role);
	}
	
	private static List<UserRole> createUserRoleListByUser(String userId, List<UserRole> userRoles) {
		List<UserRole> resultList = new LinkedList<>();
		for(UserRole userRole : userRoles) {
			if(userRole.equals(userId)) {
				resultList.add(userRole);
			}
		}
		return resultList;
	}
	
	public static List<UserRoleVo> createList(List<User> users, List<Role> roles, 
			List<UserRole> userRoles) {
		List<UserRoleVo> resultVo = new LinkedList<>();
		for(User user : users) {
			UserRoleVo userRoleVo = new UserRoleVo(user, new LinkedList<Role>());
			List<UserRole> userRoleList = createUserRoleListByUser(user.getId(), userRoles);
			for(Role role : roles) {
				for(UserRole userRole : userRoleList) {
					if(userRole.getRoleId().equals(role.getId())) {
						addRole(userRoleVo, role);
					}
				}
			}
			resultVo.add(userRoleVo);
		}
		return resultVo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Role> getRoleList() {
		return roles;
	}

	public void setRoleList(List<Role> roleList) {
		this.roles = roleList;
	}
	
	
}
