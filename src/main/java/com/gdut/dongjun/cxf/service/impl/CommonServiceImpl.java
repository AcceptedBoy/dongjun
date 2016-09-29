package com.gdut.dongjun.cxf.service.impl;

import com.gdut.dongjun.cxf.service.CommonService;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.po.Substation;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.service.SubstationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by symon on 16-9-27.
 */
public class CommonServiceImpl implements CommonService {

    @Autowired
    private SubstationService substationService;

    @Autowired
    private LineService lineService;

    @Autowired
    private HighVoltageSwitchService hvSwitchService;

    @Override
    public void addSubstation(Substation substation) {
        substationService.insert(substation);
    }

    @Override
    public void deleteSubstation(String substationId) {
        substationService.deleteByPrimaryKey(substationId);
    }

    @Override
    public void updateSubstation(Substation substation) {
        substationService.updateByPrimaryKeySelective(substation);
    }

    @Override
    public void addLine(Line line) {
        lineService.insert(line);
    }

    @Override
    public void deleteLine(String lineId) {
        lineService.deleteByPrimaryKey(lineId);
    }

    @Override
    public void updateLine(Line line) {
        lineService.updateByPrimaryKeySelective(line);
    }

    @Override
    public void addHighVoltageSwitch(HighVoltageSwitch hvSwitch) {
        hvSwitchService.insert(hvSwitch);
    }

    @Override
    public void deleteHighVoltageSwitch(String switchId) {
        hvSwitchService.deleteByPrimaryKey(switchId);
    }

    @Override
    public void updateVoltageSwitch(HighVoltageSwitch hvSwitch) {
        hvSwitchService.updateByPrimaryKeySelective(hvSwitch);
    }
}
