package com.gdut.dongjun.web.vo;

import com.gdut.dongjun.po.Company;

public class CompanyVO {

	private String id;
    private String name;
    private String description;
    private String chief;
    private String address;
    private String phone;
    private String ipAddr;
    
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getChief() {
		return chief;
	}
	public void setChief(String chief) {
		this.chief = chief;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
    
	public Company getCompany() {
		Company c = new Company();
		c.setId(this.getId());
		c.setAddress(this.getAddress());
		c.setChief(this.getChief());
		c.setDescription(this.getDescription());
		c.setName(this.getName());
		c.setPhone(this.getPhone());
		return c;
	}
	
}
