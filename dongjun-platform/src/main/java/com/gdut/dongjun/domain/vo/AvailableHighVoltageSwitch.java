package com.gdut.dongjun.domain.vo;


import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 高压开关vo类,包含是否激活的字符串
 * Created by AcceptedBoy on 2016/9/6.
 */

public class AvailableHighVoltageSwitch {

    private String id;

    private String name;

    private String address;

    private String simNumber;

    private String deviceNumber;

    private Float longitude;

    private Float latitude;

    private Integer inlineIndex;

    private String lineId;

    private String showName;

    private String onlineTime;

    //包含有三种情况：未验证，已验证，已过期
    private String isAvailable;

    public static List<AvailableHighVoltageSwitch> change2default(List<HighVoltageSwitch> switchList) {
        if(CollectionUtils.isEmpty(switchList)) {
            return Collections.emptyList();
        }
        List<AvailableHighVoltageSwitch> activeHighSwitchList = new ArrayList<>(switchList.size());
        for(HighVoltageSwitch highVoltageSwitch : switchList) {
            activeHighSwitchList.add(change2defaultVo(highVoltageSwitch));
        }
        return activeHighSwitchList;
    }

    private static AvailableHighVoltageSwitch change2defaultVo(HighVoltageSwitch highVoltageSwitch) {
        AvailableHighVoltageSwitch availableHighVoltageSwitch = new AvailableHighVoltageSwitch();
        availableHighVoltageSwitch.setName(highVoltageSwitch.getName());
        availableHighVoltageSwitch.setAddress(highVoltageSwitch.getAddress());
        availableHighVoltageSwitch.setDeviceNumber(highVoltageSwitch.getDeviceNumber());
        availableHighVoltageSwitch.setId(highVoltageSwitch.getId());
        availableHighVoltageSwitch.setInlineIndex(highVoltageSwitch.getInlineIndex());
        availableHighVoltageSwitch.setIsAvailable("已验证");
        availableHighVoltageSwitch.setLatitude(highVoltageSwitch.getLatitude());
        availableHighVoltageSwitch.setLongitude(highVoltageSwitch.getLongitude());
        availableHighVoltageSwitch.setLineId(highVoltageSwitch.getLineId());
        availableHighVoltageSwitch.setOnlineTime(highVoltageSwitch.getOnlineTime());
        availableHighVoltageSwitch.setShowName(highVoltageSwitch.getShowName());
        availableHighVoltageSwitch.setSimNumber(highVoltageSwitch.getSimNumber());
        return availableHighVoltageSwitch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Integer getInlineIndex() {
        return inlineIndex;
    }

    public void setInlineIndex(Integer inlineIndex) {
        this.inlineIndex = inlineIndex;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }
}
