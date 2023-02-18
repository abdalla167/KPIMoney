package com.kpi.money.model.point_ads;

import com.google.gson.annotations.SerializedName;

public class UploadpointParmater {
    @SerializedName("user_id")
    String user_id;
    @SerializedName("point_id")
    String point_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPoint_id() {
        return point_id;
    }

    public void setPoint_id(String point_id) {
        this.point_id = point_id;
    }
}
