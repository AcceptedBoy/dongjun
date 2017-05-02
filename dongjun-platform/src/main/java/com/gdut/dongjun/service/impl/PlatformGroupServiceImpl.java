package com.gdut.dongjun.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.domain.dao.PlatformGroupMapper;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

/**
 * Created by symon on 16-10-18.
 */
@Service
public class PlatformGroupServiceImpl extends EnhancedServiceImpl<PlatformGroup>
    implements PlatformGroupService{

    @Autowired
    private PlatformGroupMapper groupMapper;
    @Autowired
    private CompanyService comService;
    @Autowired
    private UserService userService;

    @Override
    protected boolean isExist(PlatformGroup record) {
        if (record != null
                && groupMapper.selectByPrimaryKey(record.getId() + "") != null) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public PlatformGroup getDefaultGroup(String companyId, int type) {
        Map<String, Object> param = new HashMap<>();
        param.put("company_id", companyId);
        param.put("type", type);
        param.put("is_default", 1);
        List<PlatformGroup> list =  groupMapper.selectByParameters(param);
        if(CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

	@Override
	public User selectBossByPlatformId(String platformId) {
		PlatformGroup pg = this.selectByPrimaryKey(platformId);
		Company c = comService.selectByPrimaryKey(pg.getCompanyId());
		return userService.selectByPrimaryKey(c.getMainStaffId());
	}

	@Override
	public List<PlatformGroup> fuzzySearch(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("%").append(name).append("%");
		return groupMapper.fuzzySearch(sb.toString());
	}
}
