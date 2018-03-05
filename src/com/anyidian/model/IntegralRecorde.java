package com.anyidian.model;

/**
 * 积分兑换记录
 * @author Firefly
 *
 */
public class IntegralRecorde {
	private String user;
	private String title;
	private String date;
	private String integral;

	public IntegralRecorde() {
		super();
	}
	
	public IntegralRecorde(String user, String title, String integralFlag) {
		super();
		this.user = user;
		this.title = title;
		this.integral = integralFlag;
	}

	public IntegralRecorde(String user, String title, String date,
			String integral) {
		super();
		this.user = user;
		this.title = title;
		this.date = date;
		this.integral = integral;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

}
