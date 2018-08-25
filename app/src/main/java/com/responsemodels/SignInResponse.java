package com.responsemodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("notice")
    @Expose
    private String notice;
    @SerializedName("client_set_status_value")
    @Expose
    private Object clientSetStatusValue;
    @SerializedName("client_set_status_label")
    @Expose
    private Object clientSetStatusLabel;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Object getClientSetStatusValue() {
        return clientSetStatusValue;
    }

    public void setClientSetStatusValue(Object clientSetStatusValue) {
        this.clientSetStatusValue = clientSetStatusValue;
    }

    public Object getClientSetStatusLabel() {
        return clientSetStatusLabel;
    }

    public void setClientSetStatusLabel(Object clientSetStatusLabel) {
        this.clientSetStatusLabel = clientSetStatusLabel;
    }

}