package com.anyidian.model;

/**
 * 投诉
 * @author Administrator
 *
 */
public class Complaints {
	private String listId;
	private String state;
	private String community;
	private String name;
	private String mobile;
	private String address;
	private String date;
	private String complaintType;
	private String introduce;
	private String image;
	private String evaluate;
	private int attitudeScore;
	private int timeScore;
	
	public Complaints() {
		super();
	}

	public Complaints(String listId, String state, String community,
			String name, String mobile, String address, String date,
			String complaintType, String introduce, String image, String evaluate,
			int attitudeScore, int timeScore) {
		super();
		this.listId = listId;
		this.state = state;
		this.community = community;
		this.name = name;
		this.mobile = mobile;
		this.address = address;
		this.date = date;
		this.complaintType = complaintType;
		this.introduce = introduce;
		this.image = image;
		this.evaluate = evaluate;
		this.attitudeScore = attitudeScore;
		this.timeScore = timeScore;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public int getAttitudeScore() {
		return attitudeScore;
	}

	public void setAttitudeScore(int attitudeScore) {
		this.attitudeScore = attitudeScore;
	}

	public int getTimeScore() {
		return timeScore;
	}

	public void setTimeScore(int timeScore) {
		this.timeScore = timeScore;
	}

	
}
