package com.gdut.dongjun.cxf.service;

import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.po.Substation;

import javax.jws.WebMethod;
import javax.jws.WebService;


/**
 * 暴露substationService服务
 * Created by symon on 16-9-27.
 */
@WebService
public interface CommonService {

    @WebMethod
    public void addSubstation(Substation substation);

    @WebMethod
    public void deleteSubstation(String substationId);

    @WebMethod
    public void updateSubstation(Substation substation);

    @WebMethod
    public void addLine(Line line);

    @WebMethod
    public void deleteLine(String lineId);

    @WebMethod
    public void updateLine(Line line);

    @WebMethod
    public void addHighVoltageSwitch(HighVoltageSwitch hvSwitch);

    @WebMethod
    public void deleteHighVoltageSwitch(String switchId);

    @WebMethod
    public void updateVoltageSwitch(HighVoltageSwitch hvSwitch);
}
