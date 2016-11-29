package com.gdut.dongjun.service.impl;

import com.gdut.dongjun.domain.dao.LowVoltageVoltageMapper;
import com.gdut.dongjun.domain.dao.PlatformGroupMapper;
import com.gdut.dongjun.domain.dao.impl.PlatformGroupDAOImpl;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by symon on 16-10-18.
 */
@Service
public class PlatformGroupServiceImpl extends BaseServiceImpl<PlatformGroup>
    implements PlatformGroupService{

    @Autowired
    private PlatformGroupMapper groupMapper;

    @Override
    protected boolean isExist(PlatformGroup record) {
        if (record != null
                && groupMapper.selectByPrimaryKey(record.getId()) != null) {

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
}
