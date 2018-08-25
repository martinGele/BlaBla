package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitPaymentAddBalanceModel {

    @SerializedName("amount")
    @Expose
    String amount;
    @SerializedName("cvv")
    @Expose
    String cvv;
    public SubmitPaymentAddBalanceModel(String  amount, String cvv) {
        this.amount = amount;
        this.cvv = cvv;
    }


}
