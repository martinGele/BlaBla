package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FbSignInModel {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("android_id")
    @Expose
    public String android_id;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("register_type")
    @Expose
    public String register_type;
    @SerializedName("appkey")
    @Expose
    public String appkey;
    @SerializedName("sign_in_device_type")
    @Expose
    public String sign_in_device_type;
    @SerializedName("device_id")
    @Expose
    public String device_id;
    @SerializedName("device_token")
    @Expose
    public String device_token;
    @SerializedName("phone_model")
    @Expose
    public String phone_model;
    @SerializedName("os")
    @Expose
    public String os;


    public FbSignInModel(String email, String android_id, String password, String register_type, String appkey, String sign_in_device_type, String device_id, String device_token, String phone_model, String os) {
        this.email = email;
        this.android_id = android_id;
        this.password = password;
        this.register_type = register_type;
        this.appkey = appkey;
        this.sign_in_device_type = sign_in_device_type;
        this.device_id = device_id;
        this.device_token = device_token;
        this.phone_model = phone_model;
        this.os = os;
    }


}
