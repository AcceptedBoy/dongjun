package com.gdut.dongjun.domain.po.authc;

public class RoleModuleKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role_module.id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role_module.role_id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    private String roleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role_module.module_id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    private String moduleId;

    public RoleModuleKey() {
		super();
	}

	public RoleModuleKey(String roleId2, String m) {
    	
    	this.roleId = roleId2;
    	this.moduleId = m;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role_module.id
     *
     * @return the value of role_module.id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role_module.id
     *
     * @param id the value for role_module.id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role_module.role_id
     *
     * @return the value of role_module.role_id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role_module.role_id
     *
     * @param roleId the value for role_module.role_id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role_module.module_id
     *
     * @return the value of role_module.module_id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    public String getModuleId() {
        return moduleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role_module.module_id
     *
     * @param moduleId the value for role_module.module_id
     *
     * @mbggenerated Sat Nov 14 14:40:29 CST 2015
     */
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}