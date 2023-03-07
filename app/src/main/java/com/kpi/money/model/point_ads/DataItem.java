package com.kpi.money.model.point_ads;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("times")
	private String times;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("points")
	private String points;

	public String getTimes(){
		return times;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public String getPoints(){
		return points;
	}
}