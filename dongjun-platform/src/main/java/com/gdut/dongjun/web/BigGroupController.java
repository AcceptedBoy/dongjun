package com.gdut.dongjun.web;

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
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.BigGroupService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
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
	private PlatformGroupService platformGroupService;

	/**
     * 获取一个组的所有platformgroup
     * @param groupId
     * @return
     */
	@RequiresAuthentication
    @RequestMapping("/platformgroup_list")
    @ResponseBody
    public ResponseMessage deviceList(@RequestParam(required = true) Integer groupId) {
        return ResponseMessage.info(platformGroupService.selectByParameters(
                MyBatisMapUtil.warp("group_id", groupId)));
    }
	
	/**
	 * 添加一个分组
	 * 
	 * @param group
	 * @return
	 */
	@RequiresPermissions(value = "big_group_admin:edit")
	@RequestMapping("/add")
	@ResponseBody
	public ResponseMessage addGroup(HttpSession session, BigGroup group) {
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
	@RequiresPermissions(value = "big_group_admin:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public ResponseMessage deleteGroup(HttpSession session, @NotNull Integer id) {

		Map<String, Object> param = new HashMap<>();
		param.put("is_default", 1);
		List<BigGroup> list = groupService.selectByParameters(param);
		if (CollectionUtils.isEmpty(list)) {
			return ResponseMessage.danger("系统错误");
		} else {
			List<PlatformGroup> platformGroupList = platformGroupService
					.selectByParameters(MyBatisMapUtil.warp("group_id", id));
			for (PlatformGroup platformGroup : platformGroupList) {
				platformGroup.setGroupId(list.get(0).getId());
				platformGroupService.updateByPrimaryKey(platformGroup);
			}
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
	@RequiresPermissions(value = "big_group_admin:edit")
	@RequestMapping("/update")
	@ResponseBody
	public ResponseMessage updateGroup(BigGroup group) {
		group.setIsDefault(0);
		groupService.updateByPrimaryKeySelective(group);
		return ResponseMessage.success("操作成功");
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

	
}
