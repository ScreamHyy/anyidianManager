package com.anyidian.model;

import java.util.List;

/**
 * 积分商城
 * @author Firefly
 *
 */
public class IntegralMall {
	private String mallId;
	private String community;
	private String title;
	private String mall;
	private int integral;
	private double marketPrice;
	private String introduce;
	private String images;
	
	public IntegralMall() {
		super();
	}
	
	public IntegralMall(String mallId, String community, String title,
			String mall, int integral, double marketPrice, String introduce) {
		super();
		this.mallId = mallId;
		this.community = community;
		this.title = title;
		this.mall = mall;
		this.integral = integral;
		this.marketPrice = marketPrice;
		this.introduce = introduce;
	}

	public IntegralMall(String mallId, String community, String title,
			String mall, int integral, double marketPrice, String introduce,
			String images) {
		super();
		this.mallId = mallId;
		this.community = community;
		this.title = title;
		this.mall = mall;
		this.integral = integral;
		this.marketPrice = marketPrice;
		this.introduce = introduce;
		this.images = images;
	}

	public String getMallId() {
		return mallId;
	}

	public void setMallId(String mallId) {
		this.mallId = mallId;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMall() {
		return mall;
	}

	public void setMall(String mall) {
		this.mall = mall;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}
	
}
