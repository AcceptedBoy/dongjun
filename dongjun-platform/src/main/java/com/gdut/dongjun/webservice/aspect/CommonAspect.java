/*
package com.gdut.dongjun.webservice.aspect;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.po.Substation;
import com.gdut.dongjun.webservice.Constant;
import com.gdut.dongjun.webservice.client.CommonServiceClient;
import com.gdut.dongjun.webservice.util.JaxrsClientUtil;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


*/
/**
 * Created by symon on 16-9-29.
 *//*

@Aspect
@Component
public class CommonAspect {

    @Resource
    public void setClient(Constant constant) {
        this.constant = constant;
        client = (CommonServiceClient)
                new JaxrsClientUtil().getClient(constant.getPreSerivcePath(), CommonServiceClient.class);
    }


    private CommonServiceClient client;

    private Constant constant;

    //execution(* AbstractWorkFlow+.executeWorkflow()) && target(ProductionWorkFlow)

    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.deleteByPrimaryKey(String))"
            + " && args(substationId) && target(com.gdut.dongjun.service.impl.SubstationServiceImpl)")
    public void deleteSubstation(String substationId) {
        if(constant.isService()) {
            client.deleteSubstation(substationId);
        }
    }

    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.deleteByPrimaryKey(String))"
            + " && args(lineId) && target(com.gdut.dongjun.service.impl.LineServiceImpl)")
    public void deleteLine(String lineId) {
        if(constant.isService()) {
            client.deleteLine(lineId);
        }
    }

    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.deleteByPrimaryKey(String))"
            + " && args(hvswitchId) && target(com.gdut.dongjun.service.impl.HighVoltageSwitchServiceImpl)")
    public void deleteHvswitch(String hvswitchId) {
        if(constant.isService()) {
            client.deleteHighVoltageSwitch(hvswitchId);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.insert(*))"
            + " && args(substation) && target(com.gdut.dongjun.domain.dao.impl.SubstationDAOImpl)")
    public void add(Substation substation) {
        if(constant.isService()) {
            client.addSubstation(substation);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.insert(*))"
            + " && args(line) && target(com.gdut.dongjun.domain.dao.impl.LineDAOImpl)")
    public void add(Line line) {
        if(constant.isService()) {
            client.addLine(line);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.insert(*))"
            + " && args(highVoltageSwitch) && target(com.gdut.dongjun.domain.dao.impl.HighVoltageSwitchDAOImpl)")
    public void add(HighVoltageSwitch highVoltageSwitch) {
        if(constant.isService()) {
            client.addHighVoltageSwitch(highVoltageSwitch);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.updateByPrimaryKey(*))"
            + " && args(substation) && target(com.gdut.dongjun.domain.dao.impl.SubstationDAOImpl)")
    public void update(Substation substation) {
        if(constant.isService()) {
            client.updateSubstation(substation);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.updateByPrimaryKey(*))"
            + " && args(line) && target(com.gdut.dongjun.domain.dao.impl.LineDAOImpl)")
    public void update(Line line) {
        if(constant.isService()) {
            client.updateLine(line);
        }
    }

    @After("execution(* com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl+.updateByPrimaryKey(*))"
            + " && args(highVoltageSwitch) && target(com.gdut.dongjun.domain.dao.impl.HighVoltageSwitchDAOImpl)")
    public void update(HighVoltageSwitch highVoltageSwitch) {
        if(constant.isService()) {
            client.updateVoltageSwitch(highVoltageSwitch);
        }
    }
}
*/