package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.dao.CenterMapper;
import com.gdut.dongjun.dao.HighVoltageSwitchMapper;
import com.gdut.dongjun.po.Center;
import com.gdut.dongjun.service.CenterService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by symon on 16-9-28.
 */
@Service
public class CenterServiceImpl extends BaseServiceImpl<Center>
    implements CenterService {

    @Autowired
    private CenterMapper centerMapper;

    @Override
    public boolean isExist(Center record) {

        if(record == null) {
            return false;
        }

        Map<String, Object> checkParam = new HashMap<>();
        checkParam.put("ip_addr", record.getIpAddr());
//        checkParam.put("mac_addr", record.getMacAddr());

        if (!CollectionUtils.isEmpty(centerMapper.selectByParameters(checkParam))) {
            return true;
        } else {
            return false;
        }
    }
}
