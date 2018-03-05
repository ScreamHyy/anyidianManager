package com.anyidian.model;

public class Marketing {

	String marketingId;
	String houseType;
	String houseNum;
	String name;
	String mobile;
	String toName;
	String toMobile;

	public Marketing() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Marketing(String marketingId, String houseType, String houseNum,
			String name, String mobile, String toName, String toMobile) {
		super();
		this.marketingId = marketingId;
		this.houseType = houseType;
		this.houseNum = houseNum;
		this.name = name;
		this.mobile = mobile;
		this.toName = toName;
		this.toMobile = toMobile;
	}

	public String getMarketingId() {
		return marketingId;
	}

	public void setMarketingId(String marketingId) {
		this.marketingId = marketingId;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
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

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToMobile() {
		return toMobile;
	}

	public void setToMobile(String toMobile) {
		this.toMobile = toMobile;
	}

}
