package com.gdut.dongjun.domain.po;

public class AbnormalDevice {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column abnormal_device.id
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column abnormal_device.switch_id
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    private String switchId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column abnormal_device.reason
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    private Integer reason;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column abnormal_device.id
     *
     * @return the value of abnormal_device.id
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column abnormal_device.id
     *
     * @param id the value for abnormal_device.id
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column abnormal_device.switch_id
     *
     * @return the value of abnormal_device.switch_id
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    public String getSwitchId() {
        return switchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column abnormal_device.switch_id
     *
     * @param switchId the value for abnormal_device.switch_id
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    public void setSwitchId(String switchId) {
        this.switchId = switchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column abnormal_device.reason
     *
     * @return the value of abnormal_device.reason
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    public Integer getReason() {
        return reason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column abnormal_device.reason
     *
     * @param reason the value for abnormal_device.reason
     *
     * @mbggenerated Fri Jan 13 12:13:04 CST 2017
     */
    public void setReason(Integer reason) {
        this.reason = reason;
    }
}