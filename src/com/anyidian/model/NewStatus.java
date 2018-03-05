package com.anyidian.model;

public class NewStatus {
	private String statusId;
	private String statusType;
	private String image;
	private String title;
	private String date;
	private String detail;

	public NewStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewStatus(String statusId, String statusType, String image,
			String title, String date, String detail) {
		super();
		this.statusId = statusId;
		this.statusType = statusType;
		this.image = image;
		this.title = title;
		this.date = date;
		this.detail = detail;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	

}
