package com.anyidian.model;

public class TopBanner {
	private String bannerId;
	private String community;
	private String bannerType;
	private String image;
	private String title;
	private String introduce;
	private String myImage;

	public TopBanner(String bannerId, String community, String bannerType, String image, String title, String introduce){
		super();
		this.bannerId = bannerId;
		this.community = community;
		this.bannerType = bannerType;
		this.image = image;
		this.title = title;
		this.introduce = introduce;
	}
	public TopBanner() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getBannerId() {
		return bannerId;
	}
	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public String getBannerType() {
		return bannerType;
	}
	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
	public String getMyImage(){
		return myImage;
	}
	
	public void setMyImage(String myImage){
		this.myImage = myImage;
	}
	
	

}
