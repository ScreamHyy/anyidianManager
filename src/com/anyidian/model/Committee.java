package com.anyidian.model;

/**
 * 小区居委会
 * @author Administrator
 *
 */
public class Committee {
	private String committeeId;
	private String committeeName;
	private String header;
	private String mobile;
	private String address;
	
	public Committee() {
		super();
	}

	public Committee(String committeeId, String committeeName, 
			String header, String mobile, String address) {
		super();
		this.committeeId = committeeId;
		this.committeeName = committeeName;
		this.header = header;
		this.mobile = mobile;
		this.address = address;
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	public String getCommitteeName() {
		return committeeName;
	}

	public void setCommitteeName(String committeeName) {
		this.committeeName = committeeName;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
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
	
}
