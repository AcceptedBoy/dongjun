package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.PlatformGroupMapper;
import com.gdut.dongjun.domain.po.group;
import com.gdut.dongjun.service.groupService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

/**
 * Created by symon on 16-10-18.
 */
@Service
public class groupServiceImpl extends BaseServiceImpl<group>
    implements groupService{

    @Autowired
    private PlatformGroupMapper groupMapper;

    @Override
    protected boolean isExist(group record) {
        if (record != null
                && groupMapper.selectByPrimaryKey(String.valueOf(record.getId())) != null) {

            return true;
        } else {
            return false;
        }
    }


}
