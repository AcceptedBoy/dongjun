package com.gdut.dongjun.dto;

import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;

public class TemperatureMeasureHitchEventDTO {
	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.id
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    private String id;
    
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.value
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    private String value;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.tag
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    private Integer tag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.hitch_reason
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    private String hitchReason;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column temperature_measure_hitch_event.hitch_time
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    private String hitchTime;
    
    private String maxHitchValue;
    
    private String minHitchValue;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event.id
     *
     * @return the value of temperature_measure_hitch_event.id
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event.id
     *
     * @param id the value for temperature_measure_hitch_event.id
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event.value
     *
     * @return the value of temperature_measure_hitch_event.value
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public String getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event.value
     *
     * @param value the value for temperature_measure_hitch_event.value
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event.tag
     *
     * @return the value of temperature_measure_hitch_event.tag
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public Integer getTag() {
        return tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event.tag
     *
     * @param tag the value for temperature_measure_hitch_event.tag
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public void setTag(Integer tag) {
        this.tag = tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event.hitch_reason
     *
     * @return the value of temperature_measure_hitch_event.hitch_reason
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public String getHitchReason() {
        return hitchReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event.hitch_reason
     *
     * @param hitchReason the value for temperature_measure_hitch_event.hitch_reason
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public void setHitchReason(String hitchReason) {
        this.hitchReason = hitchReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column temperature_measure_hitch_event.hitch_time
     *
     * @return the value of temperature_measure_hitch_event.hitch_time
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public String getHitchTime() {
        return hitchTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column temperature_measure_hitch_event.hitch_time
     *
     * @param hitchTime the value for temperature_measure_hitch_event.hitch_time
     *
     * @mbggenerated Wed Mar 08 20:40:52 CST 2017
     */
    public void setHitchTime(String hitchTime) {
        this.hitchTime = hitchTime;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaxHitchValue() {
		return maxHitchValue;
	}

	public void setMaxHitchValue(String maxHitchValue) {
		this.maxHitchValue = maxHitchValue;
	}

	public String getMinHitchValue() {
		return minHitchValue;
	}

	public void setMinHitchValue(String minHitchValue) {
		this.minHitchValue = minHitchValue;
	}
	
}
