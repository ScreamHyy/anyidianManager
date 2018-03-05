package com.anyidian.model;

public class NewHouse {
	private String houseId;
	private String image;
	private String title;
	private String introduce;
	private String detail;

	public NewHouse(String houseId, String image, String title, String introduce, String detail) {
		// TODO Auto-generated constructor stub
		this.houseId = houseId;
		this.image = image;
		this.title = title;
		this.introduce = introduce;
		this.detail = detail;
	}

	public NewHouse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	

}
