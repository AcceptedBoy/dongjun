package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.BigGroup;
import com.gdut.dongjun.domain.po.BigGroupMapping;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.service.BigGroupMappingService;
import com.gdut.dongjun.service.BigGroupService;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

/**
 *
 */
@Controller
@RequestMapping("/dongjun/group")
public class BigGroupController {

	@Autowired
	private BigGroupService groupService;
	@Autowired
	private BigGroupMappingService groupMappingService;
	@Autowired
	private CompanyService companyService;
	
	public BigGroupController() {
		
	}

	/**
     * 获取一个组的所有platformgroup
     * @param groupId
     * @return
     */
//	@RequiresAuthentication
//    @RequestMapping("/platformgroup_list")
//    @ResponseBody
//    public ResponseMessage deviceList(@RequestParam(required = true) Integer groupId) {
//        return ResponseMessage.info(platformGroupService.selectByParameters(
//                MyBatisMapUtil.warp("group_id", groupId)));
//    }
	
	@RequiresAuthentication
    @RequestMapping("/company_list")
    @ResponseBody
    public ResponseMessage deviceList(@RequestParam(required = true) String groupId) {
		List<BigGroupMapping> mappings = 
				groupMappingService.selectByParameters(MyBatisMapUtil.warp("group_id", groupId));
		List<Company> list = new ArrayList<>();
		for (BigGroupMapping m : mappings) {
			list.add(companyService.selectByPrimaryKeyNoDel(m.getCompanyId()));
		}
        return ResponseMessage.success(list);
    }
	
	@RequiresAuthentication
    @RequestMapping("/list")
    @ResponseBody
    public ResponseMessage list() {
		return ResponseMessage.success(groupService.selectByParameters(null));
    }
	
	/**
	 * 添加一个分组
	 * 
	 * @param group
	 * @return
	 */
//	@RequiresPermissions(value = "big_group_admin:edit")
	@RequiresAuthentication
	@RequestMapping("/add")
	@ResponseBody
	public ResponseMessage addGroup(HttpSession session, BigGroup group) {
		if (null == group.getId() || "".equals(group.getId())) {
			group.setId(UUIDUtil.getUUID());
		}
		group.setIsDefault(0); // 添加的都不是默认的
		if (groupService.insert(group) != 0) {
			return ResponseMessage.success("添加成功");
		} else {
			return ResponseMessage.danger("系统错误");
		}
	}

	
	
	/**
	 * 删除分组 注意： TODO
	 * 
	 * @param groupId
	 * @return
	 */
//	@RequiresPermissions(value = "big_group_admin:delete")
	@RequiresAuthentication
	@RequestMapping("/delete")
	@ResponseBody
	public ResponseMessage deleteGroup(HttpSession session, @NotNull String id) {

		List<BigGroupMapping> mappings = 
				groupMappingService.selectByParameters(MyBatisMapUtil.warp("group_id", id));
		if (null != mappings && mappings.size() > 0) {
			return ResponseMessage.warning("删除失败，该分组下仍有公司信息"); 
		}
		groupMappingService.deleteByParameters(MyBatisMapUtil.warp("group_id", id));
		return ResponseMessage.success("删除成功");
	}

	/**
	 * 更新分组信息
	 * 
	 * @param group
	 * @return
	 */
	@RequiresPermissions(value = "big_group_admin:edit")
	@RequestMapping("/update")
	@ResponseBody
	public ResponseMessage updateGroup(BigGroup group) {
		if (null != group && null != group.getId()
				&& null != groupService.selectByPrimaryKey(group.getId())) {
			group.setIsDefault(0);
			groupService.updateByPrimaryKeySelective(group);
			return ResponseMessage.success("操作成功");
		}
		return ResponseMessage.warning("操作失败");
	}
	
	/**
	 * 
	 * @Title: getLineSwitchList
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequiresAuthentication
	@RequestMapping("/gruop_list")
	@ResponseBody
	public Object getLineSwitchListByLineId(HttpSession session, Model model) {

//		User user = (User) session.getAttribute("currentUser");
		List<BigGroup> list = groupService.selectByParameters(null);
		int size = list.size();
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		map.put("recordsTotal", size);
		map.put("data", list);
		map.put("recordsFiltered", size);
		return map;
	}
	
	@RequiresAuthentication
	@RequestMapping("/group_mapping/edit")
	@ResponseBody
	public ResponseMessage addGourpMapping(
			@RequestParam(required=true) String groupId,
			@RequestParam(required=true) String companyId) {
		if (null == groupService.selectByPrimaryKey(groupId) ||
				null == companyService.selectByPrimaryKeyNoDel(companyId)) {
			return ResponseMessage.warning("操作失败");
		}
		BigGroupMapping m = new BigGroupMapping();
		m.setId(UUIDUtil.getUUID());
		m.setCompanyId(companyId);
		m.setGroupId(groupId);
		groupMappingService.insert(m);
		return ResponseMessage.success("操作成功"); 
	}
	
	@RequiresAuthentication
	@RequestMapping("/group_mapping/del")
	@ResponseBody
	public ResponseMessage delGourpMapping(@RequestParam(required=true) String companyId) {
		if (0 == groupMappingService.deleteByParameters(MyBatisMapUtil.warp("company_id", companyId))) {
			return ResponseMessage.warning("操作失败"); 
		}
		return ResponseMessage.success("操作成功"); 

	}

	
}
