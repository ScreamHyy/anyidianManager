package com.anyidian.model;

public class Comment {
	private String statusId;
	private String name;
	private String content;

	public Comment(String statusId, String name, String content) {
		// TODO Auto-generated constructor stub
		super();
		this.statusId = statusId;
		this.name = name;
		this.content = content;
	}

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}
