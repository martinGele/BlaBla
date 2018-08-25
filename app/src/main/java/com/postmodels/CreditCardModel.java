package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditCardModel {

    @SerializedName("cardNumber")
    @Expose
    public String cardNumber;
    @SerializedName("cardholderName")
    @Expose
    public String cardholderName;
    @SerializedName("expMonth")
    @Expose
    public String expMonth;
    @SerializedName("expYear")
    @Expose
    public String expYear;
    @SerializedName("streetAddress")
    @Expose
    public String streetAddress;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("postalCode")
    @Expose
    public String postalCode;


    public CreditCardModel(String cardNumber, String cardholderName, String expMonth, String expYear, String streetAddress, String city, String state, String postalCode) {
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }



}
