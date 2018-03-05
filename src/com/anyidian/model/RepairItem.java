package com.anyidian.model;

/**
 * 报修条目
 * @author Administrator
 *
 */
public class RepairItem {
	
	private String itemId;
	private String repairType;
	private String price;
	private String remark;
	
	public RepairItem() {
		super();
	}

	public RepairItem(String itemId, String repairType) {
		super();
		this.itemId = itemId;
		this.repairType = repairType;
	}

	public RepairItem(String itemId, String repairType, String price, String remark) {
		super();
		this.itemId = itemId;
		this.repairType = repairType;
		this.price = price;
		this.remark = remark;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getRepairType() {
		return repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
