package com.anyidian.model;

public class Interest {
	private String interestId;
	private String community;
	private String interestType;
	public Interest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Interest(String interestId, String community, String interestType) {
		super();
		this.interestId = interestId;
		this.community = community;
		this.interestType = interestType;
		// TODO Auto-generated constructor stub
	}
	public String getInterestId() {
		return interestId;
	}
	public void setInterestId(String interestId) {
		this.interestId = interestId;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public String getInterestType() {
		return interestType;
	}
	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}
	
	
	
	

}
