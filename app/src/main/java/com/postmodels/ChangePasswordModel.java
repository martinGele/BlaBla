package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel {

    @SerializedName("appkey")
    @Expose
    public String appkey;
    @SerializedName("auth_token")
    @Expose
    public String auth_token;
    @SerializedName("current_password")
    @Expose
    public String current_password;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("password_confirmation")
    @Expose
    public String password_confirmation;


    public ChangePasswordModel(String aappkey, String auth_token, String current_password, String password, String password_confirmation) {
        this.appkey = aappkey;
        this.auth_token = auth_token;
        this.current_password = current_password;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

}
