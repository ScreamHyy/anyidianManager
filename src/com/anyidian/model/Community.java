package com.anyidian.model;

/**
 * 小区类
 * @author Administrator
 *
 */
public class Community {
	
	private String communityId;
	private String communityName;
	private String committee;
	private String city;
	private String propertyName;
	private String mobile;
	private String address;
	private String landArea;
	private String buildArea;
	
	public Community() {
		super();
	}

	public Community(String communityId, String communityName,
			String committee, String city, String propertyName, String mobile,
			String address, String landArea, String buildArea) {
		super();
		this.communityId = communityId;
		this.communityName = communityName;
		this.committee = committee;
		this.city = city;
		this.propertyName = propertyName;
		this.mobile = mobile;
		this.address = address;
		this.landArea = landArea;
		this.buildArea = buildArea;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getCommittee() {
		return committee;
	}

	public void setCommittee(String committee) {
		this.committee = committee;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLandArea() {
		return landArea;
	}

	public void setLandArea(String landArea) {
		this.landArea = landArea;
	}

	public String getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(String buildArea) {
		this.buildArea = buildArea;
	}

}
