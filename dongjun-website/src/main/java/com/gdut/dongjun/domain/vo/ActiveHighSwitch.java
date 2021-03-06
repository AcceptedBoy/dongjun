package com.gdut.dongjun.domain.vo;

import java.io.Serializable;

/**
 *@Author link xiaoMian <972192420@qq.com>
 *@ClassName ActiveHighSwitch.java
 *@Time 2016年2月28日下午8:16:34
 *@Description TODO
 *@Version 1.0 Topview
 */
public class ActiveHighSwitch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private boolean open = false;
	
	private String status;
	
	private String hitchEventId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getHitchEventId() {
		return hitchEventId;
	}
	
	public void setHitchEventId(String hitchEventId) {
		this.hitchEventId = hitchEventId;
	}

	public ActiveHighSwitch(String id, boolean open, String status,
			String hitchEventId) {
		super();
		this.id = id;
		this.open = open;
		this.status = status;
		this.hitchEventId = hitchEventId;
	}
	
	public ActiveHighSwitch() {
		super();
	}

	@Override
	public String toString() {
		return "ActiveHighSwitch [id=" + id + ", open=" + open + ", status="
				+ status + ", hitchEventId=" + hitchEventId + "]";
	}
}
