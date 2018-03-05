package com.anyidian.model;

/**
 * 小区通知
 * @author Administrator
 *
 */
public class Notice {
	private String noticeId;
	private String community;
	private String noticeType;
	private String committee;
	private String date;
	private String title;
	private String detail;
	private String image;
	private String publisher;
	
	public Notice() {
		super();
	}

	public Notice(String noticeId, String community, String noticeType,
			String committee, String date, String title, String detail,
			String image, String publisher) {
		super();
		this.noticeId = noticeId;
		this.community = community;
		this.noticeType = noticeType;
		this.committee = committee;
		this.date = date;
		this.title = title;
		this.detail = detail;
		this.image = image;
		this.publisher = publisher;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getCommittee() {
		return committee;
	}

	public void setCommittee(String committee) {
		this.committee = committee;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
}
