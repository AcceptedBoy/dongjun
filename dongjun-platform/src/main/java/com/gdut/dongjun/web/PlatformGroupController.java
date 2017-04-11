package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

/**
 *
 */
@Controller
@RequestMapping("/dongjun/platform_group")
public class PlatformGroupController {

	@Autowired
	private PlatformGroupService groupService;
	@Autowired
	private UserService userService;
	@Autowired
	private TemperatureModuleService temperatureService;
	
	/**
	 * 添加一个分组
	 * 
	 * @param group
	 * @return
	 */
	@RequiresPermissions("platform_group_admin:edit")
	@RequestMapping("/add")
	@ResponseBody
	public ResponseMessage addGroup(HttpSession session, PlatformGroup group) {
		group.setIsDefault((byte) 0); // 添加的都不是默认的
		group.setCompanyId(userService.getCurrentUser(session).getCompanyId());
		if (null == group.getId() || "".equals(group.getId())) {
			group.setId(UUIDUtil.getUUID());
		}
		if (groupService.insert(group) != 0) {
			return ResponseMessage.success("添加成功");
		} else {
			return ResponseMessage.danger("系统错误");
		}
	}

	/**
	 * 获取一个组的所有开关设备
	 * 
	 * @param groupId
	 * @return
	 */
//	@RequiresAuthentication
//	@RequestMapping("/device_list")
//	@ResponseBody
//	public ResponseMessage deviceList(@RequestParam(required = true) Integer groupId) {
//		return ResponseMessage.info(hvSwitchService.selectByParameters(MyBatisMapUtil.warp("group_id", groupId)));
//	}

	/**
	 * 获取某个协议类型的所有分组
	 * 
	 * @param session
	 * @param type
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/group_list")
	@ResponseBody
	public ResponseMessage groupList(HttpSession session, @RequestParam(required = true) Integer type) {
		User user = userService.getCurrentUser(session);
		return ResponseMessage.info(groupService.selectByParameters(
				MyBatisMapUtil.warp(buildMap(userService.getCurrentUser(session).getCompanyId(), type, 0))));
	}

	/**
	 * 将一个设备移动到另一个分组上
	 * 
	 * @param groupId
	 * @param deviceId
	 * @param type
	 * @return
	 */
//	@RequiresAuthentication
//	@RequestMapping("moveO_new")
//	@ResponseBody
//	@Deprecated
//	public ResponseMessage moveNew(@NotNull String groupId, @NotNull String deviceId, @NotNull Integer type) {
//
//		// TODO 只有高压的移动
//		switch (type) {
//		case 1:
//			HighVoltageSwitch hvSwitch = hvSwitchService.selectByPrimaryKey(deviceId);
//			hvSwitch.setGroupId(groupId);
//
//			hvSwitchService.updateByPrimaryKey(hvSwitch);
//		}
//		return ResponseMessage.success("操作成功");
//	}

	/**
	 * 将一个设备移动到另一个分组上
	 * 
	 * @param groupId
	 * @param deviceId
	 * @param type
	 * @return
	 */
	@RequiresAuthentication
	@RequestMapping("/move_new_group")
	@ResponseBody
	public String moveNewGroup(@NotNull String groupId, @NotNull String id) {
		PlatformGroup group = groupService.selectByPrimaryKey(id);
		group.setGroupId(groupId);
		groupService.updateByPrimaryKey(group);
		return groupId;
	}

	/**
	 * 删除分组 注意：不要连分组里面的开关都删掉，应该将其保存到默认的组中 TODO
	 * 如果要删除公司的话，要不就设置可用位为0算了，不要删除数据
	 * 
	 * @param groupId
	 * @return
	 */
	@RequiresPermissions("platform_group_admin:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public ResponseMessage deleteGroup(HttpSession session, @NotNull String id, @NotNull Integer type) {
		// TODO 没有做判断，即删除的设备是否属于本公司，本人有没有权利删除
		String defaultGroupId = groupService.getDefaultGroup(userService.getCurrentUser(session).getCompanyId(), type)
				.getId();
		
		// TODO 删除分组后的照理来说旗下的设备全都要删除，这里保留到默认组
		List<TemperatureModule> switchList2 = temperatureService.selectByParameters(MyBatisMapUtil.warp("group_id", id));
		for (TemperatureModule tem : switchList2) {
			tem.setGroupId(defaultGroupId);
			temperatureService.updateByPrimaryKey(tem);
		}
		
		if (groupService.deleteByPrimaryKey(id + "")) {
			return ResponseMessage.success("删除成功");
		} else {
			return ResponseMessage.danger("系统错误");
		}
	}

	/**
	 * 更新分组信息
	 * 
	 * @param group
	 * @return
	 */
	@RequiresPermissions("platform_group_admin:edit")
	@RequestMapping("/update")
	@ResponseBody
	public ResponseMessage updateGroup(PlatformGroup group) {
		group.setIsDefault((byte) 0);
		groupService.updateByPrimaryKeySelective(group);
		return ResponseMessage.success("操作成功");
	}

	/**
	 * 该controller特定生成查询map
	 * 
	 * @param companyId
	 * @param type
	 * @param isDefault
	 * @return
	 */
	private Map<String, Object> buildMap(String companyId, Integer type, Integer isDefault) {

		Map<String, Object> params = new HashMap<>();
		if (StringUtils.isNotEmpty(companyId)) {
			params.put("company_id", companyId);
		}
		if (type != null) {
			params.put("type", type);
		}
		if (isDefault != null) {
			params.put("is_default", isDefault);
		}
		return params;
	}

	/**
	 * 
	 * @Title: getLineSwitchList @Description: TODO @param @param
	 *         lineId @param @param model @param @return @return String @throws
	 */
	@RequiresAuthentication
	@RequestMapping("/platform_group_list_by_group_id")
	@ResponseBody
	public Object getLineSwitchListByLineId(String groupId, HttpSession session, Model model) {

		List<PlatformGroup> list = groupService.selectByParameters(MyBatisMapUtil.warp("group_id", groupId));
		int size = list.size();
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		map.put("recordsTotal", size);
		map.put("data", list);
		map.put("recordsFiltered", size);
		return map;
	}
}