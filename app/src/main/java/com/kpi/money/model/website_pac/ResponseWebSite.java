package com.kpi.money.model.website_pac;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseWebSite {
	@SerializedName("Data")
	private List<DataItemWebSite> Data;
	@SerializedName("Count")
	private int Count;

	public List<DataItemWebSite> getData(){
		return Data;
	}

	public int getCount(){
		return Count;
	}
}