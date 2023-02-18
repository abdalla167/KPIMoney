package com.kpi.money.model;

public class ModelIDAds {
    String admob_appId,banner_ad_unit_id,interstitial_ad_unit_id,reward_id;

    public ModelIDAds(String admob_appId, String banner_ad_unit_id, String interstitial_ad_unit_id, String reward_id) {
        this.admob_appId = admob_appId;
        this.banner_ad_unit_id = banner_ad_unit_id;
        this.interstitial_ad_unit_id = interstitial_ad_unit_id;
        this.reward_id = reward_id;
    }

    public ModelIDAds() {
    }

    public String getAdmob_appId() {
        return admob_appId;
    }

    public void setAdmob_appId(String admob_appId) {
        this.admob_appId = admob_appId;
    }

    public String getBanner_ad_unit_id() {
        return banner_ad_unit_id;
    }

    public void setBanner_ad_unit_id(String banner_ad_unit_id) {
        this.banner_ad_unit_id = banner_ad_unit_id;
    }

    public String getInterstitial_ad_unit_id() {
        return interstitial_ad_unit_id;
    }

    public void setInterstitial_ad_unit_id(String interstitial_ad_unit_id) {
        this.interstitial_ad_unit_id = interstitial_ad_unit_id;
    }

    public String getReward_id() {
        return reward_id;
    }

    public void setReward_id(String reward_id) {
        this.reward_id = reward_id;
    }
}
