package com.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferalProgramResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("email_title")
    @Expose
    private String emailTitle;
    @SerializedName("email_body")
    @Expose
    private String emailBody;
    @SerializedName("facebook_text")
    @Expose
    private String facebookText;
    @SerializedName("twitter_text")
    @Expose
    private String twitterText;
    @SerializedName("other_media_text")
    @Expose
    private String otherMediaText;
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("referral_program_title")
    @Expose
    private String referralProgramTitle;
    @SerializedName("incentive_title")
    @Expose
    private String incentiveTitle;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getFacebookText() {
        return facebookText;
    }

    public void setFacebookText(String facebookText) {
        this.facebookText = facebookText;
    }

    public String getTwitterText() {
        return twitterText;
    }

    public void setTwitterText(String twitterText) {
        this.twitterText = twitterText;
    }

    public String getOtherMediaText() {
        return otherMediaText;
    }

    public void setOtherMediaText(String otherMediaText) {
        this.otherMediaText = otherMediaText;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReferralProgramTitle() {
        return referralProgramTitle;
    }

    public void setReferralProgramTitle(String referralProgramTitle) {
        this.referralProgramTitle = referralProgramTitle;
    }

    public String getIncentiveTitle() {
        return incentiveTitle;
    }

    public void setIncentiveTitle(String incentiveTitle) {
        this.incentiveTitle = incentiveTitle;
    }

}