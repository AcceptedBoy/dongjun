package com.gdut.dongjun.service.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.po.Substation;
import com.gdut.dongjun.service.common.CommonSwitch;
import com.gdut.dongjun.service.webservice.client.CommonServiceClient;
import com.gdut.dongjun.service.webservice.client.po.HighVoltageSwitchDTO;


/**
 * TODO 部分功能未测试
 */
@Aspect
@Component
public class CentorAspect {

    @Autowired
    private CommonServiceClient centorServiceClient;

    @Autowired
    private CommonSwitch commonSwitch;

    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.deleteByPrimaryKey(String))"
            + " && args(substationId) && target(com.gdut.dongjun.service.impl.SubstationServiceImpl)")
    public void deleteSubstation(String substationId) {
        if(commonSwitch.canService()) {
            centorServiceClient.getService().deleteSubstation(substationId);
        }
    }

    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.deleteByPrimaryKey(String))"
            + " && args(lineId) && target(com.gdut.dongjun.service.impl.LineServiceImpl)")
    public void deleteLine(String lineId) {
        if(commonSwitch.canService()) {
            centorServiceClient.getService().deleteLine(lineId);
        }
    }

    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.deleteByPrimaryKey(String))"
            + " && args(hvswitchId) && target(com.gdut.dongjun.service.device.impl.HighVoltageSwitchServiceImpl)")
    public void deleteHvswitch(String hvswitchId) {
        if(commonSwitch.canService()) {
            centorServiceClient.getService().deleteHighVoltageSwitch(hvswitchId);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.insert(*))"
            + " && args(substation) && target(com.gdut.dongjun.domain.dao.impl.SubstationDAOImpl)")
    public void add(Substation substation) {
        if(commonSwitch.canService()) {
            centorServiceClient.getService().addSubstation(substation);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.insert(*))"
            + " && args(line) && target(com.gdut.dongjun.domain.dao.impl.LineDAOImpl)")
    public void add(Line line) {
        if(commonSwitch.canService()) {
            centorServiceClient.getService().addLine(line);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.insert(*))"
            + " && args(highVoltageSwitch) && target(com.gdut.dongjun.domain.dao.impl.HighVoltageSwitchDAOImpl)")
    public void add(HighVoltageSwitch highVoltageSwitch) {
        if(commonSwitch.canService()) {
        	HighVoltageSwitchDTO dto = new HighVoltageSwitchDTO(highVoltageSwitch);
            centorServiceClient.getService().addHighVoltageSwitch(dto);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.updateByPrimaryKey(*))"
            + " && args(substation) && target(com.gdut.dongjun.domain.dao.impl.SubstationDAOImpl)")
    public void update(Substation substation) {
        if(commonSwitch.canService()) {
            centorServiceClient.getService().updateSubstation(substation);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.updateByPrimaryKey(*))"
            + " && args(line) && target(com.gdut.dongjun.domain.dao.impl.LineDAOImpl)")
    public void update(Line line) {
        if(commonSwitch.canService()) {
            centorServiceClient.getService().updateLine(line);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.updateByPrimaryKey(*))"
            + " && args(highVoltageSwitch) && target(com.gdut.dongjun.domain.dao.impl.HighVoltageSwitchDAOImpl)")
    public void update(HighVoltageSwitch highVoltageSwitch) {
        if(commonSwitch.canService()) {
        	HighVoltageSwitchDTO dto = new HighVoltageSwitchDTO(highVoltageSwitch);
            centorServiceClient.getService().updateVoltageSwitch(dto);
        }
    }
}
