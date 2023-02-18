package com.kpi.money.model.website_pac;

import com.google.gson.annotations.SerializedName;

public class uploadCode {
    @SerializedName("user_id")
    String user_id;
    @SerializedName("link_id")
    String link_id;

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
