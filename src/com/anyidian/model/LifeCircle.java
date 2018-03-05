package com.anyidian.model;

/**
 * 本地生活圈
 * @author Administrator
 *
 */
public class LifeCircle {
	private String lifeId;
	private String icon;
	private String image;
	private String typeId;
	private String lifeType;
	private String name;
	private String tel;
	private String address;
	private String detail;
	
	public LifeCircle() {
		super();
	}
	
	public LifeCircle(String lifeId, String icon, String image,
			String typeId, String name, String tel, String address,
			String detail) {
		super();
		this.lifeId = lifeId;
		this.icon = icon;
		this.image = image;
		this.typeId = typeId;
		this.name = name;
		this.tel = tel;
		this.address = address;
		this.detail = detail;
	}

	public LifeCircle(String lifeId, String image, String typeId, String name,
			String tel, String address, String detail) {
		super();
		this.lifeId = lifeId;
		this.image = image;
		this.typeId = typeId;
		this.name = name;
		this.tel = tel;
		this.address = address;
		this.detail = detail;
	}
	
	public LifeCircle(String lifeId, String icon, String image, String typeId,
			String lifeType, String name, String tel, String address,
			String detail) {
		super();
		this.lifeId = lifeId;
		this.icon = icon;
		this.image = image;
		this.typeId = typeId;
		this.lifeType = lifeType;
		this.name = name;
		this.tel = tel;
		this.address = address;
		this.detail = detail;
	}

	public String getLifeId() {
		return lifeId;
	}

	public void setLifeId(String lifeId) {
		this.lifeId = lifeId;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
