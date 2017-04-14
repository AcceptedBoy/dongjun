package com.gdut.dongjun.domain.po;

import java.util.Date;

public class PersistentHitchMessage extends AbstractBean {
	
    public PersistentHitchMessage() {
		super();
	}

	public PersistentHitchMessage(String id, Integer hitchType, String hitchEventId, Date date, String userId) {
		super();
		this.id = id;
		this.hitchType = hitchType;
		this.hitchEventId = hitchEventId;
		this.date = date;
		this.userId = userId;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column persistent_hitch_message.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column persistent_hitch_message.hitch_type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Integer hitchType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column persistent_hitch_message.hitch_event_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String hitchEventId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column persistent_hitch_message.date
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private Date date;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column persistent_hitch_message.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    private String userId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column persistent_hitch_message.id
     *
     * @return the value of persistent_hitch_message.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column persistent_hitch_message.id
     *
     * @param id the value for persistent_hitch_message.id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column persistent_hitch_message.hitch_type
     *
     * @return the value of persistent_hitch_message.hitch_type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Integer getHitchType() {
        return hitchType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column persistent_hitch_message.hitch_type
     *
     * @param hitchType the value for persistent_hitch_message.hitch_type
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setHitchType(Integer hitchType) {
        this.hitchType = hitchType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column persistent_hitch_message.hitch_event_id
     *
     * @return the value of persistent_hitch_message.hitch_event_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getHitchEventId() {
        return hitchEventId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column persistent_hitch_message.hitch_event_id
     *
     * @param hitchEventId the value for persistent_hitch_message.hitch_event_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setHitchEventId(String hitchEventId) {
        this.hitchEventId = hitchEventId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column persistent_hitch_message.date
     *
     * @return the value of persistent_hitch_message.date
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column persistent_hitch_message.date
     *
     * @param date the value for persistent_hitch_message.date
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column persistent_hitch_message.user_id
     *
     * @return the value of persistent_hitch_message.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column persistent_hitch_message.user_id
     *
     * @param userId the value for persistent_hitch_message.user_id
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column persistent_hitch_message.gmt_create
     *
     * @return the value of persistent_hitch_message.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column persistent_hitch_message.gmt_create
     *
     * @param gmtCreate the value for persistent_hitch_message.gmt_create
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column persistent_hitch_message.gmt_modified
     *
     * @return the value of persistent_hitch_message.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column persistent_hitch_message.gmt_modified
     *
     * @param gmtModified the value for persistent_hitch_message.gmt_modified
     *
     * @mbggenerated Thu Apr 13 21:13:41 CST 2017
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}