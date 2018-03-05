package com.anyidian.model;

/**
 * 商家分类
 * @author Firefly
 *
 */
public class Business {
	private String businessId;
	private String icon;
	private String name;
	
	public Business() {
		super();
	}
	
	public Business(String icon, String name) {
		super();
		this.icon = icon;
		this.name = name;
	}

	public Business(String businessId, String icon,
			String name) {
		super();
		this.businessId = businessId;
		this.icon = icon;
		this.name = name;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
