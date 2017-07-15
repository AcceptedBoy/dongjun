package com.gdut.dongjun.cxf.po;

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

    private List<HighVoltageSwitch> hvswitchList;

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
    public List<HighVoltageSwitch> getHvswitchList() {
        return hvswitchList;
    }

    public void setHvswitchList(List<HighVoltageSwitch> hvswitchList) {
        this.hvswitchList = hvswitchList;
    }
}
