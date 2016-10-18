package com.gdut.dongjun.domain.vo;



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
