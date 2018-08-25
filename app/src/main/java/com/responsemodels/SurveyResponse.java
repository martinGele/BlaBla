package com.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurveyResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("notice")
    @Expose
    private String notice;
    @SerializedName("receipt_id")
    @Expose
    private Object receiptId;
    @SerializedName("restaurant_name")
    @Expose
    private Object restaurantName;
    @SerializedName("receipt_date")
    @Expose
    private Object receiptDate;
    @SerializedName("survey_id")
    @Expose
    private Object surveyId;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Object getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Object receiptId) {
        this.receiptId = receiptId;
    }

    public Object getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(Object restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Object getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Object receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Object getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Object surveyId) {
        this.surveyId = surveyId;
    }

}