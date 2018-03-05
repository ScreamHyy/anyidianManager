package com.anyidian.model;

/**
 * 维修人
 * @author Administrator
 *
 */
public class RepairMan {
	private String repairmanId;
	private String name;
	private String mobile;
	private String community;
	
	
	public RepairMan() {
		super();
	}

	public RepairMan(String repairmanId, String name, String mobile,
			String community) {
		super();
		this.repairmanId = repairmanId;
		this.name = name;
		this.mobile = mobile;
		this.community = community;
	}

	public String getRepairmanId() {
		return repairmanId;
	}

	public void setRepairmanId(String repairmanId) {
		this.repairmanId = repairmanId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}
	
}
