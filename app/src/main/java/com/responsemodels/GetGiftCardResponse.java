package com.responsemodels;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetGiftCardResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("expiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("card_status")
    @Expose
    private String cardStatus;

    public String getMessage() {
        return message;
    }

    @SerializedName("message")
    @Expose
    private String message;


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

}



