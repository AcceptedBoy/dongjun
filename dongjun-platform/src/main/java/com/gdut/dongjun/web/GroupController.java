package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.group;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.groupService;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 *
 */
@Controller
@RequestMapping("/dongjun/group")
public class GroupController {

	@Autowired
	private groupService groupService;

	@Autowired
	private PlatformGroupService platformGroupService;

	/**
     * 获取一个组的所有platformgroup
     * @param groupId
     * @return
     */
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
	@RequestMapping("/add")
	@ResponseBody
	public ResponseMessage addGroup(HttpSession session, group group) {
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
	@RequestMapping("/delete")
	@ResponseBody
	public ResponseMessage deleteGroup(HttpSession session, @NotNull Integer groupId) {

		Map<String, Object> param = new HashMap<>();
		param.put("is_default", 1);
		List<group> list = groupService.selectByParameters(param);
		if (CollectionUtils.isEmpty(list)) {
			return ResponseMessage.danger("系统错误");
		} else {
			List<PlatformGroup> platformGroupList = platformGroupService
					.selectByParameters(MyBatisMapUtil.warp("group_id", groupId));
			for (PlatformGroup platformGroup : platformGroupList) {
				platformGroup.setGroupId(list.get(0).getGroupId());
				platformGroupService.updateByPrimaryKey(platformGroup);
			}
		}
		if (groupService.deleteByPrimaryKey(groupId)) {
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
	@RequestMapping("/update")
	@ResponseBody
	public ResponseMessage updateGroup(group group) {
		group.setIsDefault(0);
		groupService.updateByPrimaryKeySelective(group);
		return ResponseMessage.success("操作成功");
	}

}
