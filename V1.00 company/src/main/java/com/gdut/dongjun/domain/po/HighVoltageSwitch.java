package com.gdut.dongjun.domain.po;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class HighVoltageSwitch {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.id
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.name
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    @Length(max = 40, message = "开关名字应在40个字符以内")
    @NotNull
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.address
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private String address;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.sim_number
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private String simNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.device_number
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private String deviceNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.longitude
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private Float longitude;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.latitude
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private Float latitude;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.inline_index
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private Integer inlineIndex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.line_id
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private String lineId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.show_name
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private String showName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column high_voltage_switch.online_time
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    private String onlineTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.id
     *
     * @return the value of high_voltage_switch.id
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.id
     *
     * @param id the value for high_voltage_switch.id
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.name
     *
     * @return the value of high_voltage_switch.name
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.name
     *
     * @param name the value for high_voltage_switch.name
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.address
     *
     * @return the value of high_voltage_switch.address
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.address
     *
     * @param address the value for high_voltage_switch.address
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.sim_number
     *
     * @return the value of high_voltage_switch.sim_number
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public String getSimNumber() {
        return simNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.sim_number
     *
     * @param simNumber the value for high_voltage_switch.sim_number
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.device_number
     *
     * @return the value of high_voltage_switch.device_number
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public String getDeviceNumber() {
        return deviceNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.device_number
     *
     * @param deviceNumber the value for high_voltage_switch.device_number
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.longitude
     *
     * @return the value of high_voltage_switch.longitude
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.longitude
     *
     * @param longitude the value for high_voltage_switch.longitude
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.latitude
     *
     * @return the value of high_voltage_switch.latitude
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.latitude
     *
     * @param latitude the value for high_voltage_switch.latitude
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.inline_index
     *
     * @return the value of high_voltage_switch.inline_index
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public Integer getInlineIndex() {
        return inlineIndex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.inline_index
     *
     * @param inlineIndex the value for high_voltage_switch.inline_index
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setInlineIndex(Integer inlineIndex) {
        this.inlineIndex = inlineIndex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.line_id
     *
     * @return the value of high_voltage_switch.line_id
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public String getLineId() {
        return lineId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.line_id
     *
     * @param lineId the value for high_voltage_switch.line_id
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.show_name
     *
     * @return the value of high_voltage_switch.show_name
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public String getShowName() {
        return showName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.show_name
     *
     * @param showName the value for high_voltage_switch.show_name
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setShowName(String showName) {
        this.showName = showName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column high_voltage_switch.online_time
     *
     * @return the value of high_voltage_switch.online_time
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public String getOnlineTime() {
        return onlineTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column high_voltage_switch.online_time
     *
     * @param onlineTime the value for high_voltage_switch.online_time
     *
     * @mbggenerated Fri Mar 11 10:53:17 CST 2016
     */
    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }
}