package com.kpi.money.model.point_ads;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdsSetting{

	@SerializedName("Data")
	private List<DataItemAsd> Data;

	@SerializedName("Count")
	private int Count;

	public List<DataItemAsd> getData() {
		return Data;
	}

	public void setData(List<DataItemAsd> data) {
		Data = data;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}
}