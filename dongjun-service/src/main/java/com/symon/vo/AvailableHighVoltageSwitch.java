package com.symon.vo;

import com.symon.po.HighVoltageSwitch;

import java.util.ArrayList;
import java.util.List;

/**
 * 高压开关vo类，包含是否激活的字符串
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

    public static AvailableHighVoltageSwitch change2Vo(HighVoltageSwitch hSwitch) {
        return new AvailableHighVoltageSwitch(hSwitch);
    }

    public static List<AvailableHighVoltageSwitch> change2VoList(List<HighVoltageSwitch> switchList) {
        //TODO 没有判空
        List<AvailableHighVoltageSwitch> resultList = new ArrayList<>(switchList.size());
        for(HighVoltageSwitch hSwitch : switchList) {
            resultList.add(change2Vo(hSwitch));
        }
        return resultList;

    }

    public AvailableHighVoltageSwitch(String id, String name, String address, String simNumber,
                                      String deviceNumber, Float longitude, Float latitude,
                                      Integer inlineIndex, String lineId, String showName,
                                      String onlineTime, String isAvailable) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.simNumber = simNumber;
        this.deviceNumber = deviceNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.inlineIndex = inlineIndex;
        this.lineId = lineId;
        this.showName = showName;
        this.onlineTime = onlineTime;
        this.isAvailable = isAvailable;
    }

    public AvailableHighVoltageSwitch(HighVoltageSwitch hSwitch) {
        this(hSwitch.getId(), hSwitch.getName(), hSwitch.getAddress(), hSwitch.getSimNumber(),
                hSwitch.getDeviceNumber(), hSwitch.getLongitude(), hSwitch.getLatitude(),
                hSwitch.getInlineIndex(), hSwitch.getLineId(), hSwitch.getShowName(),
                hSwitch.getOnlineTime(),
                getVoIsAvailable(hSwitch.getAvailableTime()));
    }

    private static final String NO_VALID = "未验证";

    private static final String HAS_VALID = "已验证";

    private static final String HAS_OUT_TIME = "已过期";
    /**
     * 根据数据库中的{@code availableTime}获取vo类中的isAvailable的情况：1. 未验证 2. 已验证 3. 已过期
     * @param availableTime
     * @return
     */
    private static String getVoIsAvailable(String availableTime) {
        if(availableTime == null || availableTime.length() == 0) {
            return NO_VALID;
        } else {
            if(availableTime.compareTo(String.valueOf(System.currentTimeMillis())) < 0) {
                return HAS_OUT_TIME;
            } else {
                return HAS_VALID;
            }
        }
    }

    public AvailableHighVoltageSwitch() {

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