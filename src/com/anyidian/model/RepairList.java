package com.anyidian.model;

/**
 * 报修单
 * @author Administrator
 *
 */
public class RepairList {

	private String listId;
	private String state;
	private String mobile;
	private String name;
	private String address;
	private String date;
	private String repairType;
	private String introduce;
	private String image;
	private String community;
	private String repairmanName;
	private double cost;
	private String evaluate;
	private int qualityScore;
	private int attitudeScore;
	private int timeScore;
	private int priceScore;

	public RepairList() {
		super();
	}

	public RepairList(String listId, String state, String mobile, String name,
			String address, String date, String repairType, String introduce,
			String image, String community, String repairmanName, double cost,
			String evaluate, int qualityScore, int attitudeScore,
			int timeScore, int priceScore) {
		super();
		this.listId = listId;
		this.state = state;
		this.mobile = mobile;
		this.name = name;
		this.address = address;
		this.date = date;
		this.repairType = repairType;
		this.introduce = introduce;
		this.image = image;
		this.community = community;
		this.repairmanName = repairmanName;
		this.cost = cost;
		this.evaluate = evaluate;
		this.qualityScore = qualityScore;
		this.attitudeScore = attitudeScore;
		this.timeScore = timeScore;
		this.priceScore = priceScore;
	}
	
	/**
	 * 维修服务评价
	 * @param evaluate
	 * @param qualityScore
	 * @param attitudeScore
	 * @param timeScore
	 * @param priceScore
	 */
	public RepairList(String evaluate, int qualityScore, int attitudeScore,
			int timeScore, int priceScore) {
		super();
		this.evaluate = evaluate;
		this.qualityScore = qualityScore;
		this.attitudeScore = attitudeScore;
		this.timeScore = timeScore;
		this.priceScore = priceScore;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRepairType() {
		return repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}
	
}
