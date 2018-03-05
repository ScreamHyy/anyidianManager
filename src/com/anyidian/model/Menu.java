package com.anyidian.model;

public class Menu {
	private int mid;
	private String text;
	private String miconCls;
	private int mparent;
	private String murl;
	
	public Menu(){
		super();
	}

	public Menu(int mid, String text, String miconCls, int mparent, String murl) {
		// TODO Auto-generated constructor stub
		super();
		this.mid = mid;
		this.text = text;
		this.mparent = mparent;
		this.murl = murl;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String gettext() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMiconCls() {
		return miconCls;
	}

	public void setMiconCls(String miconCls) {
		this.miconCls = miconCls;
	}

	public int getMparent() {
		return mparent;
	}

	public void setMparent(int mparent) {
		this.mparent = mparent;
	}

	public String getMurl() {
		return murl;
	}

	public void setMurl(String murl) {
		this.murl = murl;
	}
	
	

}
