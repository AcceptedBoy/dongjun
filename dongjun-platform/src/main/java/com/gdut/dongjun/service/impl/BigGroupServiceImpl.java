package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.PlatformGroupMapper;
import com.gdut.dongjun.domain.po.BigGroup;
import com.gdut.dongjun.service.BigGroupService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

/**
 * Created by symon on 16-10-18.
 */
@Service
public class BigGroupServiceImpl extends BaseServiceImpl<BigGroup>
    implements BigGroupService{

    @Autowired
    private PlatformGroupMapper groupMapper;

    @Override
    protected boolean isExist(BigGroup record) {
        if (record != null
                && groupMapper.selectByPrimaryKey(record.getId()) != null) {

            return true;
        } else {
            return false;
        }
    }


}
