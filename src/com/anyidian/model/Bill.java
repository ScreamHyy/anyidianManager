package com.anyidian.model;

/**
 * 账单
 * @author Administrator
 *
 */
public class Bill {
	private String billId;
	private String community;
	private String name;
	private String mobile;
	private String room;
	private String billType;
	private String date;
	private double price;
	private String state;
	
	public Bill() {
		super();
	}

	public Bill(String billId, String community, String name, String mobile, String room,
			String billType, String date, double price, String state) {
		super();
		this.billId = billId;
		this.community = community;
		this.name = name;
		this.mobile = mobile;
		this.room = room;
		this.billType = billType;
		this.date = date;
		this.price = price;
		this.state = state;
	}

	public Bill(String community, String name, String mobile, String room, String billType,
			String date, double price) {
		super();
		this.community = community;
		this.name = name;
		this.mobile = mobile;
		this.room = room;
		this.billType = billType;
		this.date = date;
		this.price = price;
	}
	
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
