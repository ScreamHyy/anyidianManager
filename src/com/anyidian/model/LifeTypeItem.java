package com.anyidian.model;

/**
 * 本地生活圈条目
 * @author Administrator
 *
 */
public class LifeTypeItem {
	private String itemId;
	private String icon;
	private String name;
	
	public LifeTypeItem() {
		super();
	}
	
	public LifeTypeItem(String icon, String name) {
		super();
		this.icon = icon;
		this.name = name;
	}

	public LifeTypeItem(String itemId, String icon, String name) {
		super();
		this.itemId = itemId;
		this.icon = icon;
		this.name = name;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
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
