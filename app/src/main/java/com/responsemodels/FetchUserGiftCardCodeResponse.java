package com.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchUserGiftCardCodeResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("giftcard_code")
    @Expose
    private String giftcardCode;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getGiftcardCode() {
        return giftcardCode;
    }

    public void setGiftcardCode(String giftcardCode) {
        this.giftcardCode = giftcardCode;
    }

}