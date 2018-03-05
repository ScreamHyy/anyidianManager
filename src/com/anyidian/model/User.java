package com.anyidian.model;

/**
 * 户主信息
 * @author Firefly
 *
 */
public class User {

	private String background;
	private String name;
	private String mobile;
	private String avatar;
	private String idNumber;
	private String city;
	private String community;
	private String floor;
	private String unit;
	private String houseNumber;
	private int integral;
	private String volunteer;
	
	public User() {
		super();
	}

	public User(String background, String name, String mobile, String avatar,
			String idNumber, String city, String community, String floor,
			String unit, String houseNumber, int integral, String volunteer) {
		super();
		this.background = background;
		this.name = name;
		this.mobile = mobile;
		this.avatar = avatar;
		this.idNumber = idNumber;
		this.city = city;
		this.community = community;
		this.floor = floor;
		this.unit = unit;
		this.houseNumber = houseNumber;
		this.integral = integral;
		this.volunteer = volunteer;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(String volunteer) {
		this.volunteer = volunteer;
	}
	
}
