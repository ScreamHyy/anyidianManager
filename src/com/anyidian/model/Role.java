package com.anyidian.model;

public class Role {
	private int rid;
	private String rname;
	private int uid;

	public Role() {
		super();
	}

	public Role(int rid, String rname, int uid) {
		// TODO Auto-generated constructor stub
		super();
		this.rid = rid;
		this.rname = rname;
		this.uid = uid;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

}
