package com.gdut.dongjun.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.authc.UserRoleKey;
import com.gdut.dongjun.service.BigGroupService;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.UserRoleService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/company")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;

	@Autowired
	private UserService userService; 
	
	@Autowired
	private UserRoleService urService;
	
	@Autowired
	private PlatformGroupService pgService;

//	@RequiresPermissions("platform_group_admin:edit")
	@RequestMapping("/edit")
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
			pgService.updateByPrimaryKey(pg);
		}
		if (companyService.updateByPrimaryKey(com) == 0) {
			return ResponseMessage.danger("操作失败");
		}
		return ResponseMessage.success(com.getId());
	}
	
//	@RequestMapping("/is_exist")
//	@ResponseBody
//	public ResponseMessage edit(String name) {
//		if (null == com.getId() || "".equals(com.getId())) {
//			com.setId(UUIDUtil.getUUID());
//		}
//		if (companyService.updateByPrimaryKey(com) == 0) {
//			return ResponseMessage.danger("操作失败");
//		}
//		return ResponseMessage.success();
//	}
	
	@RequiresPermissions("platform_group_admin:delete")
	@RequestMapping("/del")
	@ResponseBody
	public ResponseMessage del(Company com, HttpSession session) {
		//TODO
		if (userService.getCurrentUser(session).getCompanyId() != com.getId()) {
			return ResponseMessage.warning("该用户非公司相关人员");
		}
		if (companyService.deleteByPrimaryKey(com.getId()) == false) {
			return ResponseMessage.danger("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequiresAuthentication
	@RequestMapping("/Imformation")
	@ResponseBody
	public ResponseMessage getImformationById(@RequestParam(required = true) String id) {
		Company com = companyService.selectByPrimaryKey(id);
		return ResponseMessage.success(com);
	}
	
	@RequiresAuthentication
	@RequestMapping("/staff")
	@ResponseBody
	public ResponseMessage getStaffById(@RequestParam(required = true) String id) {
		List<User> users = userService.selectByParameters(MyBatisMapUtil.warp("company_id", id));
		return ResponseMessage.success(users);
	}
	
	@RequiresPermissions("platform_group_admin:role")
	@RequestMapping("/authc_add")
	@ResponseBody
	public ResponseMessage addStaffAuthc(
							@RequestParam(required = true) String roleId, 
							@RequestParam(required = true) String userId) {
		UserRoleKey key = new UserRoleKey();
		key.setUserId(userId);
		key.setRoleId(roleId);
		if (urService.insert(key) == 0) {
			return ResponseMessage.success("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequiresPermissions("platform_group_admin:role")
	@RequiresAuthentication
	@RequestMapping("/authc_update")
	@ResponseBody
	public ResponseMessage updateStaffAuthc(
							@RequestParam(required = true) String roleId, 
							@RequestParam(required = true) String userId) {
		UserRoleKey key = new UserRoleKey();
		key.setUserId(userId);
		key.setRoleId(roleId);
		if (urService.updateByPrimaryKey(key) == 0) {
			return ResponseMessage.success("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequiresPermissions("platform_group_admin:role")
	@RequiresAuthentication
	@RequestMapping("/authc_del")
	@ResponseBody
	public ResponseMessage doChangeStaffAuthc(
							@RequestParam(required = true) String userId) {
		if (!urService.deleteByPrimaryKey(userId)) {
			return ResponseMessage.success("删除失败");
		}
		return ResponseMessage.success("删除成功");
	}
	
	@RequestMapping("/fuzzy_search")
	@ResponseBody
	public ResponseMessage doFuzzySearch(String name) {
		return ResponseMessage.success(companyService.fuzzySearch(name));
	}
	
}
