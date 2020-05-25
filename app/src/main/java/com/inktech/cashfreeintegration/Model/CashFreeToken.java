package com.inktech.cashfreeintegration.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CashFreeToken {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cftoken")
    @Expose
    private String cftoken;

    public CashFreeToken() {
    }

    public CashFreeToken(String status, String message, String cftoken) {
        this.status = status;
        this.message = message;
        this.cftoken = cftoken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCftoken() {
        return cftoken;
    }

    public void setCftoken(String cftoken) {
        this.cftoken = cftoken;
    }
}
