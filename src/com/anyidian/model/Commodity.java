package com.anyidian.model;

/**
 * 商家商品信息
 * @author Firefly
 *
 */
public class Commodity {
	private String commodityId;
	private String name;
	private String supplier;
	private String shop;
	private double nowPrice;
	private double oldPrice;
	private double supplyPrice;
	private double fee;
	private String introduce;
	private int stockNum;
	private String state;
	private String images;
	private String commentNum;
	
	public Commodity() {
		super();
	}

	public Commodity(String commodityId, String name, String supplier,
			String shop, double nowPrice, double oldPrice, double supplyPrice,
			double fee, String introduce, int stockNum, String state,
			String images, String commentNum) {
		super();
		this.commodityId = commodityId;
		this.name = name;
		this.supplier = supplier;
		this.shop = shop;
		this.nowPrice = nowPrice;
		this.oldPrice = oldPrice;
		this.supplyPrice = supplyPrice;
		this.fee = fee;
		this.introduce = introduce;
		this.stockNum = stockNum;
		this.state = state;
		this.images = images;
		this.commentNum = commentNum;
	}
	
	public Commodity(String commodityId, String name, String supplier,
			String shop, double nowPrice, double oldPrice, double supplyPrice,
			double fee, String introduce, int stockNum, String state,
			String images) {
		super();
		this.commodityId = commodityId;
		this.name = name;
		this.supplier = supplier;
		this.shop = shop;
		this.nowPrice = nowPrice;
		this.oldPrice = oldPrice;
		this.supplyPrice = supplyPrice;
		this.fee = fee;
		this.introduce = introduce;
		this.stockNum = stockNum;
		this.state = state;
		this.images = images;
	}

	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public double getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(double nowPrice) {
		this.nowPrice = nowPrice;
	}

	public double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public double getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(double supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public int getStockNum() {
		return stockNum;
	}
	
	public void setStockNum(int stockNum) {
		this.stockNum = stockNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}
	
}
