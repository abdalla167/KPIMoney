package com.kpi.money.model.point_ads;

import com.google.gson.annotations.SerializedName;

public class GetTimes{

	@SerializedName("error")
	private String error;

	@SerializedName("message")
	private String message;
	@SerializedName("times")
	private int times;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}