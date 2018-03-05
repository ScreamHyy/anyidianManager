package com.anyidian.model;

public class OldHouse {
	private String houseId;
	private String houseType;
	private String where;
	private String image;
	private String title;
	private String mobile;
	private String area;
	private String price;
	private String introduce;
	

	public OldHouse(String houseId, String houseType, String where, String image, String title, String mobile, String area, String price, String introduce) {
		// TODO Auto-generated constructor stub
		this.houseId = houseId;
		this.houseType = houseType;
		this.where = where;
		this.image = image;
		this.title = title;
		this.mobile = mobile;
		this.area = area;
		this.price = price;
		this.introduce = introduce;
	}

	public OldHouse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
}
