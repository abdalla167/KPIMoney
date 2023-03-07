package com.kpi.money.model.website_pac;

public class WebSiteSingleModle {

    String URL;
    String Titel;
    String code;
    String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public WebSiteSingleModle(String URL, String titel, String code, String amount) {
        this.URL = URL;
        Titel = titel;
        this.code = code;
        this.amount = amount;
    }

    public WebSiteSingleModle(String URL, String titel, String code) {
        this.URL = URL;
        Titel = titel;
        this.code = code;
    }

    public WebSiteSingleModle() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTitel() {
        return Titel;
    }

    public void setTitel(String titel) {
        Titel = titel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
