package com.anyidian.model;

public class FriendCircle {
	private String statusId;
	private String community;
	private String name;
	private String mobile;
	private String date;
	private String content;
	private String statusType;
	private Integer likeNum;
	private Integer commentNum;
	private String myImages;

	public FriendCircle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendCircle(String statusId, String community, String name,
			String mobile, String date, String content, String statusType,
			Integer likeNum, Integer commentNum) {
		super();
		// TODO Auto-generated constructor stub
		this.statusId = statusId;
		this.community = community;
		this.name = name;
		this.mobile = mobile;
		this.date = date;
		this.content = content;
		this.statusType = statusType;
		this.likeNum = likeNum;
		this.commentNum = commentNum;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public String getMyImages() {
		return myImages;
	}

	public void setMyImages(String myImages) {
		this.myImages = myImages;
	}
	

}
