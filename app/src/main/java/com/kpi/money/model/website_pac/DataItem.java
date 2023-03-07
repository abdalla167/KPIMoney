package com.kpi.money.model.website_pac;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("user_id")
	private String user_id;

	@SerializedName("earn")
	private String earn;

	@SerializedName("id")
	private int id;

	@SerializedName("link_id")
	private String link_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getEarn() {
		return earn;
	}

	public void setEarn(String earn) {
		this.earn = earn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLink_id() {
		return link_id;
	}

	public void setLink_id(String link_id) {
		this.link_id = link_id;
	}
}