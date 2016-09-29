package com.symon.service.impl;

import com.symon.dao.CenterMapper;
import com.symon.dao.HighVoltageSwitchMapper;
import com.symon.po.Center;
import com.symon.service.CenterService;
import com.symon.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        checkParam.put("mac_addr", record.getMacAddr());

        if (!CollectionUtils.isEmpty(centerMapper.selectByParameters(checkParam))) {
            return true;
        } else {
            return false;
        }
    }
}
