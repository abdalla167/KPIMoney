package com.kpi.money.model.point_ads;

import com.google.gson.annotations.SerializedName;

public class DataItemupload {

	@SerializedName("updated_at")
	private String updated_at;

	@SerializedName("user_id")
	private String user_id;

	@SerializedName("earn")
	private String earn;

	@SerializedName("created_at")
	private String created_at;

	@SerializedName("id")
	private int id;

	@SerializedName("link_id")
	private String link_id;

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

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

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
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