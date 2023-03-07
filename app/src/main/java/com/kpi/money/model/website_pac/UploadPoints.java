package com.kpi.money.model.website_pac;

import com.google.gson.annotations.SerializedName;
import com.kpi.money.model.point_ads.DataItemupload;

import java.util.List;

public class UploadPoints{

	@SerializedName("data")
	private List<DataItemupload> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	@SerializedName("times")
	private int times;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void setData(List<DataItemupload> data){
		this.data = data;
	}

	public List<DataItemupload> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}