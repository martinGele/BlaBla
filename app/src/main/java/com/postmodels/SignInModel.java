package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInModel {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("android_id")
    @Expose
    public String android_id;
    @SerializedName("sign_in_device_type")
    @Expose
    public String sign_in_device_type;
    @SerializedName("register_type")
    @Expose
    public String register_type;
    @SerializedName("appkey")
    @Expose
    public String appkey;
    @SerializedName("device_token")
    @Expose
    public String device_token;
    @SerializedName("device_id")
    @Expose
    public String device_id;
    @SerializedName("phone_model")
    @Expose
    public String phone_model;
    @SerializedName("os")
    @Expose
    public String os;

    public SignInModel(String email, String password, String android_id, String sign_in_device_type, String register_type, String appkey, String device_token, String device_id, String phone_model, String os) {
        this.email = email;
        this.password = password;
        this.android_id = android_id;
        this.sign_in_device_type = sign_in_device_type;
        this.register_type = register_type;
        this.appkey = appkey;
        this.device_token = device_token;
        this.device_id = device_id;
        this.phone_model = phone_model;
        this.os = os;
    }


}
