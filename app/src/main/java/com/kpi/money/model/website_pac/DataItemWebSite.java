package com.kpi.money.model.website_pac;

import com.google.gson.annotations.SerializedName;

public class DataItemWebSite {
	@SerializedName("reward")
	private String reward;
	@SerializedName("site_url")
	private String site_url;
	@SerializedName("updated_at")
	private String updated_at;
	@SerializedName("user_id")
	private String user_id;
	@SerializedName("active")
	private String active;
	@SerializedName("created_at")
	private String created_at;
	@SerializedName("id")
	private int id;
	@SerializedName("title")
	private String title;
	@SerializedName("code_number")
	private String code_number;


	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getSite_url() {
		return site_url;
	}

	public void setSite_url(String site_url) {
		this.site_url = site_url;
	}

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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode_number() {
		return code_number;
	}

	public void setCode_number(String code_number) {
		this.code_number = code_number;
	}
}
