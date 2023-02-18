package com.kpi.money.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IDModelAPI {
    @SerializedName("data")
    private List<IDSingle> mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Boolean mStatus;

    public List<IDSingle> getData() {
        return mData;
    }

    public void setData(List<IDSingle> data) {
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
