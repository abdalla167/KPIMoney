package com.kpi.money.model.website_pac;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WebSiteModlePointUpload{

	@SerializedName("total")
	private String total;

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public String getTotal(){
		return total;
	}

	public List<DataItem> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public boolean isStatus(){
		return status;
	}
}