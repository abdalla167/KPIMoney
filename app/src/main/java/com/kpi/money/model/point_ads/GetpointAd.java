package com.kpi.money.model.point_ads;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetpointAd{

	@SerializedName("Data")
	private List<DataItem>Data ;

	@SerializedName("Count")
	private int Count;

	public List<DataItem> getData() {
		return Data;
	}

	public void setData(List<DataItem> data) {
		Data = data;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}
}