package com.gdut.dongjun.web;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 *
 */
@Controller
@RequestMapping("/dongjun/platform_group")
public class PlatformGroupController {

    @Autowired
    private PlatformGroupService groupService;

    @Autowired
    private HighVoltageSwitchService hvSwitchService;

    /**
     * 添加一个分组
     * @param group
     * @return
     */
    @Autowired
    @RequestMapping("/add")
    private ResponseMessage addGroup(PlatformGroup group) {
        if(groupService.insert(group) != 0) {
            return ResponseMessage.success("添加成功");
        } else {
            return ResponseMessage.danger("系统错误");
        }
    }

    /**
     * 获取一个组的所有开关
     * @param groupId
     * @return
     */
    @Autowired
    @RequestMapping("/group_list")
    private ResponseMessage getGroup(@RequestParam(required = true) Integer groupId) {
        return ResponseMessage.info(hvSwitchService.selectByParameters(
                MyBatisMapUtil.warp("group_id", groupId)));
    }

    /**
     * 删除分组
     * 注意：不要连分组里面的开关都删掉，应该将其保存到默认的组中
     * @param groupId
     * @return
     */
    @Autowired
    @RequestMapping("/delete")
    private ResponseMessage deleteGroup(HttpSession session,
                                        @RequestParam(required = true) Integer groupId) {
        groupService.selectByParameters(MyBatisMapUtil.warp("is_default", 1));
        //TODO 没有做判断
        PlatformGroup defaultGroup =
                groupService.getDefaultGroup(((User)session.getAttribute("currentUser")).getCompanyId(), 1);

        if(groupService.deleteByPrimaryKey(String.valueOf(groupId))) {
            return ResponseMessage.success("删除成功");
        } else {
            return ResponseMessage.danger("系统错误");
        }
    }
}
