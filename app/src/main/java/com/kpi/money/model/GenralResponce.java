package com.kpi.money.model;

public class GenralResponce {

    private Object data;
    private String message;
    private boolean status;

    public Object getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}
