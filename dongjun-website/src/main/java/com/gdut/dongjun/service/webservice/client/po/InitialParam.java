package com.gdut.dongjun.service.webservice.client.po;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.domain.po.Substation;

@XmlRootElement(name="initialParam")
@XmlAccessorType(XmlAccessType.FIELD)
public class InitialParam {

    private List<Substation> substationList;

    private List<Line> lineList;

    private List<HighVoltageSwitchDTO> hvswitchList;

    @XmlElement(name = "substationList")
    public List<Substation> getSubstationList() {
        return substationList;
    }

    public void setSubstationList(List<Substation> substationList) {
        this.substationList = substationList;
    }

    @XmlElement(name = "lineList")
    public List<Line> getLineList() {
        return lineList;
    }

    public void setLineList(List<Line> lineList) {
        this.lineList = lineList;
    }

    @XmlElement(name = "hvswitchList")
    public List<HighVoltageSwitchDTO> getHvswitchList() {
        return hvswitchList;
    }

    public void setHvswitchList(List<HighVoltageSwitchDTO> hvswitchList) {
        this.hvswitchList = hvswitchList;
    }
}
