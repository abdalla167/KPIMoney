package com.kpi.money.model.point_ads;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("point_id")
	private String point_id;



	@SerializedName("user_id")
	private String user_id;

	@SerializedName("earn")
	private String earn;



	@SerializedName("id")
	private int id;

	public String getPoint_id() {
		return point_id;
	}

	public void setPoint_id(String point_id) {
		this.point_id = point_id;
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


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}