package com.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchUserCodeResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("usercode")
    @Expose
    private String usercode;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

}