package com.symon.cxf.po;

import com.sun.xml.txw2.annotation.XmlElement;
import com.symon.po.HighVoltageSwitch;
import com.symon.po.Line;
import com.symon.po.Substation;

import java.util.List;

/**
 * Created by symon on 16-9-29.
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
