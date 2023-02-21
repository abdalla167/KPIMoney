package com.kpi.money.model;

public class ModelBetweenApiSheet {

    String   uniq_id, offerid, app_name, description, icon_url, bg_image_url,  amount, OriginalAmount,  link,  partner, insTitle, first_text,  second_text,  third_text,  fourth_text;
    Boolean webview;

    public ModelBetweenApiSheet(  String uniq_id, String offerid, String app_name, String description, String icon_url, String bg_image_url, String amount, String originalAmount, String link, String partner, String insTitle, String first_text, String second_text, String third_text, String fourth_text, Boolean webview) {
        this.uniq_id = uniq_id;
        this.offerid = offerid;
        this.app_name = app_name;
        this.description = description;
        this.icon_url = icon_url;
        this.bg_image_url = bg_image_url;
        this.amount = amount;
        OriginalAmount = originalAmount;
        this.link = link;
        this.partner = partner;
        this.insTitle = insTitle;
        this.first_text = first_text;
        this.second_text = second_text;
        this.third_text = third_text;
        this.fourth_text = fourth_text;
        this.webview = webview;
    }




    public String getUniq_id() {
        return uniq_id;
    }

    public void setUniq_id(String uniq_id) {
        this.uniq_id = uniq_id;
    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getBg_image_url() {
        return bg_image_url;
    }

    public void setBg_image_url(String bg_image_url) {
        this.bg_image_url = bg_image_url;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOriginalAmount() {
        return OriginalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        OriginalAmount = originalAmount;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInsTitle() {
        return insTitle;
    }

    public void setInsTitle(String insTitle) {
        this.insTitle = insTitle;
    }

    public String getFirst_text() {
        return first_text;
    }

    public void setFirst_text(String first_text) {
        this.first_text = first_text;
    }

    public String getSecond_text() {
        return second_text;
    }

    public void setSecond_text(String second_text) {
        this.second_text = second_text;
    }

    public String getThird_text() {
        return third_text;
    }

    public void setThird_text(String third_text) {
        this.third_text = third_text;
    }

    public String getFourth_text() {
        return fourth_text;
    }

    public void setFourth_text(String fourth_text) {
        this.fourth_text = fourth_text;
    }

    public Boolean getWebview() {
        return webview;
    }

    public void setWebview(Boolean webview) {
        this.webview = webview;
    }


}
