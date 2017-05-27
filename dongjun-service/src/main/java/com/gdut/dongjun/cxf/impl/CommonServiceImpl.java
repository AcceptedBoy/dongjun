package com.gdut.dongjun.cxf.impl;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.cxf.CommonService;
import com.gdut.dongjun.cxf.po.InitialParam;
import com.gdut.dongjun.po.*;
import com.gdut.dongjun.service.*;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.vo.AvailableHighVoltageSwitch;

import javax.ws.rs.FormParam;
import java.util.*;

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

    @Autowired
    private CenterService centerService;

    @Autowired
    private ZTreeNodeService treeNodeService;

    private static final Map<String, Boolean> SUCCESS = new HashMap<String, Boolean>(1) {{
        put("success", true);
    }};

    @Override
    public Map<String, Boolean> addSubstation(Substation substation) {
        substationService.insert(substation);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> deleteSubstation(String substationId) {
        substationService.deleteByPrimaryKey(String.valueOf(substationId));
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> updateSubstation(Substation substation) {
        substationService.updateByPrimaryKeySelective(substation);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> addLine(Line line) {
        lineService.insert(line);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> deleteLine(String lineId) {
        lineService.deleteByPrimaryKey(lineId);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> updateLine(Line line) {
        lineService.updateByPrimaryKeySelective(line);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> addHighVoltageSwitch(HighVoltageSwitch hvSwitch) {
        hvSwitchService.insert(hvSwitch);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> deleteHighVoltageSwitch(String switchId) {
        hvSwitchService.deleteByPrimaryKey(switchId);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> updateVoltageSwitch(HighVoltageSwitch hvSwitch) {
        hvSwitchService.updateByPrimaryKeySelective(hvSwitch);
        return SUCCESS;
    }

    @Override
    public Map<String, Boolean> systemInitial(InitialParam initialParam) {

        List<Substation> substationList = initialParam.getSubstationList();
        List<Line> lineList = initialParam.getLineList();
        List<HighVoltageSwitch> hvswitchList = initialParam.getHvswitchList();

        if (!CollectionUtils.isEmpty(substationList)) {
            for (Substation substation : substationList) {
                substationService.updateByPrimaryKey(substation);
            }
        }
        if (!CollectionUtils.isEmpty(lineList)) {
            for (Line line : lineList) {
                lineService.updateByPrimaryKey(line);
            }
        }
        if (!CollectionUtils.isEmpty(hvswitchList)) {
            for (HighVoltageSwitch highVoltageSwitch : hvswitchList) {
                hvSwitchService.updateByPrimaryKey(highVoltageSwitch);
            }
        }
        return SUCCESS;
    }

    @Override
    public Center registerService(Long ipAddr, String macAddr) {

        Center center = null;

        if (ipAddr == null || StringUtils.isEmpty(macAddr)) {
            center.setIpAddr(ipAddr);
            center.setMacAddr(macAddr);
            return center;
        }

        Map<String, Object> check = new HashMap<>();
        check.put("ip_addr", ipAddr);
        check.put("mac_addr", macAddr);
        List<Center> centers = centerService.selectByParameters(check);
        if (!CollectionUtils.isEmpty(centers) && centers.size() == 1) {

            center = centers.get(0);
            center.setStartCount(center.getStartCount() + 1);
            centerService.updateByPrimaryKeySelective(center);
        } else {

            center = new Center();
            center.setIpAddr(ipAddr);
            center.setMacAddr(macAddr);
            center.setFirstStartTime(new Date());
            center.setStartCount(1);
            center.setServiceCount(1);
            centerService.insert(center);//TODO 以后再来使用serviceCount这个字段
        }

        center.setLatestStartTime(new Date());
        return center;
    }

    @Override
    public List<ZTreeNode> getSwitchTree(String companyId, String type) {
        return treeNodeService.getSwitchTree(companyId, type);
    }

    @Override
    public List<AvailableHighVoltageSwitch> switchsOfLine(Integer type,
                                 String lineId) {
        switch (type) {
            //高压
            case 1:
                if (lineId != null) {
                    return AvailableHighVoltageSwitch.change2VoList(
                            hvSwitchService
                                    .selectByParameters(MyBatisMapUtil.warp("line_id", lineId)));
                } else {
                    return AvailableHighVoltageSwitch.change2VoList(
                            hvSwitchService
                                    .selectByParameters(MyBatisMapUtil.warp(null)));
                }
        }
        return null;
    }
}
