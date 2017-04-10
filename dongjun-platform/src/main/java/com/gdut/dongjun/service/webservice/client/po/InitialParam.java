package com.gdut.dongjun.service.webservice.client.po;


import java.util.List;

import javax.sound.sampled.Line;

public class InitialParam {

    private List<Substation> substationList;

    private List<Line> lineList;

    private List<HighVoltageSwitch> hvswitchList;

    public List<Substation> getSubstationList() {
        return substationList;
    }

    public void setSubstationList(List<Substation> substationList) {
        this.substationList = substationList;
    }

    public List<Line> getLineList() {
        return lineList;
    }

    public void setLineList(List<Line> lineList) {
        this.lineList = lineList;
    }

    public List<HighVoltageSwitch> getHvswitchList() {
        return hvswitchList;
    }

    public void setHvswitchList(List<HighVoltageSwitch> hvswitchList) {
        this.hvswitchList = hvswitchList;
    }
}
