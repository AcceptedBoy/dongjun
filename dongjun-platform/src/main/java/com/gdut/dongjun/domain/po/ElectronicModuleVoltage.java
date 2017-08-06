package com.gdut.dongjun.domain.po;

import java.util.Date;

public class ElectronicModuleVoltage extends CommonBean {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.id
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.phase
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    private String phase;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.value
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    private Long value;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.time
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    private Date time;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electronic_module_voltage.submodule_id
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    private String submoduleId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.id
     *
     * @return the value of electronic_module_voltage.id
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.id
     *
     * @param id the value for electronic_module_voltage.id
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.phase
     *
     * @return the value of electronic_module_voltage.phase
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public String getPhase() {
        return phase;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.phase
     *
     * @param phase the value for electronic_module_voltage.phase
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public void setPhase(String phase) {
        this.phase = phase;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.value
     *
     * @return the value of electronic_module_voltage.value
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public Long getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.value
     *
     * @param value the value for electronic_module_voltage.value
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public void setValue(Long value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.time
     *
     * @return the value of electronic_module_voltage.time
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.time
     *
     * @param time the value for electronic_module_voltage.time
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electronic_module_voltage.submodule_id
     *
     * @return the value of electronic_module_voltage.submodule_id
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public String getSubmoduleId() {
        return submoduleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electronic_module_voltage.submodule_id
     *
     * @param submoduleId the value for electronic_module_voltage.submodule_id
     *
     * @mbggenerated Sat Jul 29 15:30:24 CST 2017
     */
    public void setSubmoduleId(String submoduleId) {
        this.submoduleId = submoduleId;
    }
}