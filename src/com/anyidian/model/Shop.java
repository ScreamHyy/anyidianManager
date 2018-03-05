package com.anyidian.model;

/**
 * 商家店铺
 * @author Firefly
 *
 */
public class Shop {
	private String shopId;
	private String community;
	private String businessId;
	private String businessIcon;
	private String businessName;
	private String image;
	private String shopName;
	private String tel;
	private String address;
	
	public Shop() {
		super();
	}

	public Shop(String shopId, String community, String businessId, String image,
			String shopName, String tel, String address) {
		super();
		this.shopId = shopId;
		this.community = community;
		this.businessId = businessId;
		this.image = image;
		this.shopName = shopName;
		this.tel = tel;
		this.address = address;
	}
	
	public Shop(String shopId, String community, String businessId,
			String businessIcon, String businessName, String image,
			String shopName, String tel, String address) {
		super();
		this.shopId = shopId;
		this.community = community;
		this.businessId = businessId;
		this.businessIcon = businessIcon;
		this.businessName = businessName;
		this.image = image;
		this.shopName = shopName;
		this.tel = tel;
		this.address = address;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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
	
}
