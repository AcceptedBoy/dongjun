package com.gdut.dongjun.cxf.po;

import java.util.ArrayList;
import java.util.List;

import com.gdut.dongjun.po.HighVoltageSwitch;
import com.gdut.dongjun.po.Line;
import com.gdut.dongjun.po.Substation;
import com.sun.xml.txw2.annotation.XmlElement;

/**
 * Created by symon on 16-9-29.
 * @update: 地方系统上线时候将所有信息更新到系统中
 */
@XmlElement(value = "initialParam")
public class InitialParam {

    private List<Substation> substationList;

    private List<Line> lineList;

    private List<HighVoltageSwitchDTO> hvswitchList;

    @XmlElement(value = "substationList")
    public List<Substation> getSubstationList() {
        return substationList;
    }

    public void setSubstationList(List<Substation> substationList) {
        this.substationList = substationList;
    }

    @XmlElement(value = "lineList")
    public List<Line> getLineList() {
        return lineList;
    }

    public void setLineList(List<Line> lineList) {
        this.lineList = lineList;
    }

    @XmlElement(value = "hvswitchList")
    public List<HighVoltageSwitchDTO> getHvswitchList() {
        return hvswitchList;
    }

    public void setHvswitchList(List<HighVoltageSwitchDTO> hvswitchList) {
        this.hvswitchList = hvswitchList;
    }
    
    public List<HighVoltageSwitch> getHvswitchList0() {
        List<HighVoltageSwitch> list = new ArrayList<>();
        for (HighVoltageSwitchDTO d : hvswitchList) {
        	HighVoltageSwitch s = new HighVoltageSwitch();
        	s.setAddress(d.getAddress());
        	s.setDeviceNumber(d.getDeviceNumber());
        	s.setId(d.getId());
        	s.setInlineIndex(d.getInlineIndex());
        	s.setLatitude(d.getLatitude());
        	s.setLineId(d.getLineId());
        	s.setLongitude(d.getLongitude());
        	s.setName(d.getName());
        	s.setShowName(d.getShowName());
        	s.setSimNumber(d.getSimNumber());
        	list.add(s);
        }
        return list;
    }
}
