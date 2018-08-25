package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoCodeModel {

    @SerializedName("appkey")
    @Expose
    String appkey;
    @SerializedName("auth_token")
    @Expose
    String auth_token;
    @SerializedName("code")
    @Expose
    String code;
    @SerializedName("force")
    @Expose
    String force;
    public PromoCodeModel(String appkey, String auth_token, String code, String force) {
        this.appkey = appkey;
        this.auth_token = auth_token;
        this.code = code;
        this.force = force;
    }


}
