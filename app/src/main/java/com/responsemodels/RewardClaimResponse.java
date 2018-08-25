package com.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RewardClaimResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("activities")
    @Expose
    private List<Activity> activities = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }


    public static class Activity {

        @SerializedName("reward")
        @Expose
        private Reward reward;
        @SerializedName("claim_date")
        @Expose
        private String claimDate;

        public Reward getReward() {
            return reward;
        }

        public void setReward(Reward reward) {
            this.reward = reward;
        }

        public String getClaimDate() {
            return claimDate;
        }

        public void setClaimDate(String claimDate) {
            this.claimDate = claimDate;
        }


        public static class Reward {

            @SerializedName("POSCode")
            @Expose
            private String pOSCode;
            @SerializedName("additional_information")
            @Expose
            private Boolean additionalInformation;
            @SerializedName("adj_type")
            @Expose
            private String adjType;
            @SerializedName("adj_value")
            @Expose
            private Integer adjValue;
            @SerializedName("attachment_content_type")
            @Expose
            private Object attachmentContentType;
            @SerializedName("attachment_file_name")
            @Expose
            private Object attachmentFileName;
            @SerializedName("attachment_file_size")
            @Expose
            private Object attachmentFileSize;
            @SerializedName("attachment_updated_at")
            @Expose
            private Object attachmentUpdatedAt;
            @SerializedName("auto_queue_to_aloha")
            @Expose
            private Boolean autoQueueToAloha;
            @SerializedName("bp_bogo")
            @Expose
            private Object bpBogo;
            @SerializedName("bp_threshold")
            @Expose
            private Object bpThreshold;
            @SerializedName("bpid")
            @Expose
            private Object bpid;
            @SerializedName("chain_id")
            @Expose
            private Integer chainId;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("deleted_at")
            @Expose
            private Object deletedAt;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("effectiveDate")
            @Expose
            private String effectiveDate;
            @SerializedName("executed_at")
            @Expose
            private Object executedAt;
            @SerializedName("expired_day_user")
            @Expose
            private Integer expiredDayUser;
            @SerializedName("expiryDate")
            @Expose
            private String expiryDate;
            @SerializedName("fineprint")
            @Expose
            private String fineprint;
            @SerializedName("hours_delay")
            @Expose
            private Integer hoursDelay;
            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("isActive")
            @Expose
            private Object isActive;
            @SerializedName("is_ncr")
            @Expose
            private Boolean isNcr;
            @SerializedName("is_push_email")
            @Expose
            private Object isPushEmail;
            @SerializedName("is_push_phone")
            @Expose
            private Object isPushPhone;
            @SerializedName("last_notification_sent_at")
            @Expose
            private Object lastNotificationSentAt;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("ncr_description")
            @Expose
            private Object ncrDescription;
            @SerializedName("ncr_link_location")
            @Expose
            private Boolean ncrLinkLocation;
            @SerializedName("notify_sys_admin_for_pos_codes")
            @Expose
            private Boolean notifySysAdminForPosCodes;
            @SerializedName("number_of_times_gifted")
            @Expose
            private Integer numberOfTimesGifted;
            @SerializedName("points")
            @Expose
            private Integer points;
            @SerializedName("priority_number")
            @Expose
            private Integer priorityNumber;
            @SerializedName("reward_type")
            @Expose
            private Integer rewardType;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("survey_id")
            @Expose
            private Object surveyId;
            @SerializedName("targetId")
            @Expose
            private String targetId;
            @SerializedName("targetType")
            @Expose
            private String targetType;
            @SerializedName("thumbnail_content_type")
            @Expose
            private Object thumbnailContentType;
            @SerializedName("thumbnail_file_name")
            @Expose
            private Object thumbnailFileName;
            @SerializedName("thumbnail_file_size")
            @Expose
            private Object thumbnailFileSize;
            @SerializedName("thumbnail_updated_at")
            @Expose
            private Object thumbnailUpdatedAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("visible_dashboard")
            @Expose
            private Boolean visibleDashboard;
            @SerializedName("reward_redemption_code")
            @Expose
            private String rewardRedemptionCode;

            public String getPOSCode() {
                return pOSCode;
            }

            public void setPOSCode(String pOSCode) {
                this.pOSCode = pOSCode;
            }

            public Boolean getAdditionalInformation() {
                return additionalInformation;
            }

            public void setAdditionalInformation(Boolean additionalInformation) {
                this.additionalInformation = additionalInformation;
            }

            public String getAdjType() {
                return adjType;
            }

            public void setAdjType(String adjType) {
                this.adjType = adjType;
            }

            public Integer getAdjValue() {
                return adjValue;
            }

            public void setAdjValue(Integer adjValue) {
                this.adjValue = adjValue;
            }

            public Object getAttachmentContentType() {
                return attachmentContentType;
            }

            public void setAttachmentContentType(Object attachmentContentType) {
                this.attachmentContentType = attachmentContentType;
            }

            public Object getAttachmentFileName() {
                return attachmentFileName;
            }

            public void setAttachmentFileName(Object attachmentFileName) {
                this.attachmentFileName = attachmentFileName;
            }

            public Object getAttachmentFileSize() {
                return attachmentFileSize;
            }

            public void setAttachmentFileSize(Object attachmentFileSize) {
                this.attachmentFileSize = attachmentFileSize;
            }

            public Object getAttachmentUpdatedAt() {
                return attachmentUpdatedAt;
            }

            public void setAttachmentUpdatedAt(Object attachmentUpdatedAt) {
                this.attachmentUpdatedAt = attachmentUpdatedAt;
            }

            public Boolean getAutoQueueToAloha() {
                return autoQueueToAloha;
            }

            public void setAutoQueueToAloha(Boolean autoQueueToAloha) {
                this.autoQueueToAloha = autoQueueToAloha;
            }

            public Object getBpBogo() {
                return bpBogo;
            }

            public void setBpBogo(Object bpBogo) {
                this.bpBogo = bpBogo;
            }

            public Object getBpThreshold() {
                return bpThreshold;
            }

            public void setBpThreshold(Object bpThreshold) {
                this.bpThreshold = bpThreshold;
            }

            public Object getBpid() {
                return bpid;
            }

            public void setBpid(Object bpid) {
                this.bpid = bpid;
            }

            public Integer getChainId() {
                return chainId;
            }

            public void setChainId(Integer chainId) {
                this.chainId = chainId;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public Object getDeletedAt() {
                return deletedAt;
            }

            public void setDeletedAt(Object deletedAt) {
                this.deletedAt = deletedAt;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getEffectiveDate() {
                return effectiveDate;
            }

            public void setEffectiveDate(String effectiveDate) {
                this.effectiveDate = effectiveDate;
            }

            public Object getExecutedAt() {
                return executedAt;
            }

            public void setExecutedAt(Object executedAt) {
                this.executedAt = executedAt;
            }

            public Integer getExpiredDayUser() {
                return expiredDayUser;
            }

            public void setExpiredDayUser(Integer expiredDayUser) {
                this.expiredDayUser = expiredDayUser;
            }

            public String getExpiryDate() {
                return expiryDate;
            }

            public void setExpiryDate(String expiryDate) {
                this.expiryDate = expiryDate;
            }

            public String getFineprint() {
                return fineprint;
            }

            public void setFineprint(String fineprint) {
                this.fineprint = fineprint;
            }

            public Integer getHoursDelay() {
                return hoursDelay;
            }

            public void setHoursDelay(Integer hoursDelay) {
                this.hoursDelay = hoursDelay;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Object getIsActive() {
                return isActive;
            }

            public void setIsActive(Object isActive) {
                this.isActive = isActive;
            }

            public Boolean getIsNcr() {
                return isNcr;
            }

            public void setIsNcr(Boolean isNcr) {
                this.isNcr = isNcr;
            }

            public Object getIsPushEmail() {
                return isPushEmail;
            }

            public void setIsPushEmail(Object isPushEmail) {
                this.isPushEmail = isPushEmail;
            }

            public Object getIsPushPhone() {
                return isPushPhone;
            }

            public void setIsPushPhone(Object isPushPhone) {
                this.isPushPhone = isPushPhone;
            }

            public Object getLastNotificationSentAt() {
                return lastNotificationSentAt;
            }

            public void setLastNotificationSentAt(Object lastNotificationSentAt) {
                this.lastNotificationSentAt = lastNotificationSentAt;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getNcrDescription() {
                return ncrDescription;
            }

            public void setNcrDescription(Object ncrDescription) {
                this.ncrDescription = ncrDescription;
            }

            public Boolean getNcrLinkLocation() {
                return ncrLinkLocation;
            }

            public void setNcrLinkLocation(Boolean ncrLinkLocation) {
                this.ncrLinkLocation = ncrLinkLocation;
            }

            public Boolean getNotifySysAdminForPosCodes() {
                return notifySysAdminForPosCodes;
            }

            public void setNotifySysAdminForPosCodes(Boolean notifySysAdminForPosCodes) {
                this.notifySysAdminForPosCodes = notifySysAdminForPosCodes;
            }

            public Integer getNumberOfTimesGifted() {
                return numberOfTimesGifted;
            }

            public void setNumberOfTimesGifted(Integer numberOfTimesGifted) {
                this.numberOfTimesGifted = numberOfTimesGifted;
            }

            public Integer getPoints() {
                return points;
            }

            public void setPoints(Integer points) {
                this.points = points;
            }

            public Integer getPriorityNumber() {
                return priorityNumber;
            }

            public void setPriorityNumber(Integer priorityNumber) {
                this.priorityNumber = priorityNumber;
            }

            public Integer getRewardType() {
                return rewardType;
            }

            public void setRewardType(Integer rewardType) {
                this.rewardType = rewardType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Object getSurveyId() {
                return surveyId;
            }

            public void setSurveyId(Object surveyId) {
                this.surveyId = surveyId;
            }

            public String getTargetId() {
                return targetId;
            }

            public void setTargetId(String targetId) {
                this.targetId = targetId;
            }

            public String getTargetType() {
                return targetType;
            }

            public void setTargetType(String targetType) {
                this.targetType = targetType;
            }

            public Object getThumbnailContentType() {
                return thumbnailContentType;
            }

            public void setThumbnailContentType(Object thumbnailContentType) {
                this.thumbnailContentType = thumbnailContentType;
            }

            public Object getThumbnailFileName() {
                return thumbnailFileName;
            }

            public void setThumbnailFileName(Object thumbnailFileName) {
                this.thumbnailFileName = thumbnailFileName;
            }

            public Object getThumbnailFileSize() {
                return thumbnailFileSize;
            }

            public void setThumbnailFileSize(Object thumbnailFileSize) {
                this.thumbnailFileSize = thumbnailFileSize;
            }

            public Object getThumbnailUpdatedAt() {
                return thumbnailUpdatedAt;
            }

            public void setThumbnailUpdatedAt(Object thumbnailUpdatedAt) {
                this.thumbnailUpdatedAt = thumbnailUpdatedAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public Boolean getVisibleDashboard() {
                return visibleDashboard;
            }

            public void setVisibleDashboard(Boolean visibleDashboard) {
                this.visibleDashboard = visibleDashboard;
            }

            public String getRewardRedemptionCode() {
                return rewardRedemptionCode;
            }

            public void setRewardRedemptionCode(String rewardRedemptionCode) {
                this.rewardRedemptionCode = rewardRedemptionCode;
            }

        }
    }

}