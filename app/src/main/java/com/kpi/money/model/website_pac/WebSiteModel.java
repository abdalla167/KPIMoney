package com.kpi.money.model.website_pac;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WebSiteModel {

    @SerializedName("data")
    private List<WebSiteSingleModle> mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Boolean mStatus;

    public List<WebSiteSingleModle> getData() {
        return mData;
    }

    public void setData(List<WebSiteSingleModle> data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}
