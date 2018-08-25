package com.responsemodels;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnjoyRewardResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("notice")
    @Expose
    private String notice;
    @SerializedName("reward_code")
    @Expose
    private String rewardCode;
    @SerializedName("reward_timer")
    @Expose
    private Integer rewardTimer;

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

    public String getRewardCode() {
        return rewardCode;
    }

    public void setRewardCode(String rewardCode) {
        this.rewardCode = rewardCode;
    }

    public Integer getRewardTimer() {
        return rewardTimer;
    }

    public void setRewardTimer(Integer rewardTimer) {
        this.rewardTimer = rewardTimer;
    }

}