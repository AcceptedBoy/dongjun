package com.gdut.dongjun.domain.po;

public class DeviceGroupMapping {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group_mapping.id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group_mapping.device_group_id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    private Integer deviceGroupId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group_mapping.device_id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    private String deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_group_mapping.type
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    private Integer type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.id
     *
     * @return the value of device_group_mapping.id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.id
     *
     * @param id the value for device_group_mapping.id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.device_group_id
     *
     * @return the value of device_group_mapping.device_group_id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    public Integer getDeviceGroupId() {
        return deviceGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.device_group_id
     *
     * @param deviceGroupId the value for device_group_mapping.device_group_id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    public void setDeviceGroupId(Integer deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.device_id
     *
     * @return the value of device_group_mapping.device_id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.device_id
     *
     * @param deviceId the value for device_group_mapping.device_id
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_group_mapping.type
     *
     * @return the value of device_group_mapping.type
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_group_mapping.type
     *
     * @param type the value for device_group_mapping.type
     *
     * @mbggenerated Fri Nov 25 22:28:55 CST 2016
     */
    public void setType(Integer type) {
        this.type = type;
    }
}