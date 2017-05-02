package com.gdut.dongjun.service;


import java.util.List;

import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.base.EnhancedService;

/**
 * Created by symon on 16-10-18.
 */
public interface PlatformGroupService extends EnhancedService<PlatformGroup> {

    /**
     * 根据公司id和类型获取其默认的分组
     * @param companyId 公司id
     * @param type 类型
     * @return
     */
    public PlatformGroup getDefaultGroup(String companyId, int type);
    
    public User selectBossByPlatformId(String platformId);
    
    public List<PlatformGroup> fuzzySearch(String name);
}
