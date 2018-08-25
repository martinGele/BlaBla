package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordModel {
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("register_type")
    @Expose
    public String register_type;
    @SerializedName("appkey")
    @Expose
    public String appkey;

    public ForgotPasswordModel(String email, String register_type, String appkey) {
        this.email = email;
        this.register_type = register_type;
        this.appkey = appkey;
    }

}
