package com.gdut.dongjun.service.impl;

import com.gdut.dongjun.domain.dao.LowVoltageVoltageMapper;
import com.gdut.dongjun.domain.dao.PlatformGroupMapper;
import com.gdut.dongjun.domain.dao.impl.PlatformGroupDAOImpl;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                && groupMapper.selectByPrimaryKey(String.valueOf(record.getId())) != null) {

            return true;
        } else {
            return false;
        }
    }
}
