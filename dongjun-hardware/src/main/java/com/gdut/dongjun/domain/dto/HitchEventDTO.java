package com.gdut.dongjun.domain.dto;

import java.io.Serializable;

/**
 * 报警事件推送
 * @author Gordan_Deng
 * @date 2017年5月18日
 */
public class HitchEventDTO implements Serializable {
	
	private static final long serialVersionUID = 7492353823737332433L;

	private String id;	//报警id
	private Integer type;	//报警类型
	private String moduleId;	//报警子模块id
	private String companyId;		//公司id
	private Object text;			//附加字段
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public Object getText() {
		return text;
	}
	public void setText(Object text) {
		this.text = text;
	}

}
