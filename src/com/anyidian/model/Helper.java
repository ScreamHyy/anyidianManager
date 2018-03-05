package com.anyidian.model;

/**
 * 生助手条目
 * @author Administrator
 *
 */
public class Helper {
	private String helperId;
	private String icon;
	private String helperType;
	private String website;

	public Helper() {
		super();
	}

	public Helper(String helperId, String icon, String helperType, String website) {
		super();
		this.helperId = helperId;
		this.icon = icon;
		this.helperType = helperType;
		this.website = website;
	}
	
	public String getHelperId() {
		return helperId;
	}

	public void setHelperId(String helperId) {
		this.helperId = helperId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHelperType() {
		return helperType;
	}

	public void setHelperType(String helperType) {
		this.helperType = helperType;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
}
