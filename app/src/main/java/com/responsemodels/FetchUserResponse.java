package com.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchUserResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("user")
    @Expose
    private User user;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public class User {

        @SerializedName("active")
        @Expose
        private Boolean active;
        @SerializedName("address1")
        @Expose
        private Object address1;
        @SerializedName("address2")
        @Expose
        private Object address2;
        @SerializedName("allow_to_update_goals")
        @Expose
        private Boolean allowToUpdateGoals;
        @SerializedName("app_usage_purpose")
        @Expose
        private Object appUsagePurpose;
        @SerializedName("braintree_identifier")
        @Expose
        private Object braintreeIdentifier;
        @SerializedName("braintree_user_created")
        @Expose
        private Boolean braintreeUserCreated;
        @SerializedName("chain_app_key")
        @Expose
        private String chainAppKey;
        @SerializedName("chain_id")
        @Expose
        private Integer chainId;
        @SerializedName("city")
        @Expose
        private Object city;
        @SerializedName("client_set_status_label")
        @Expose
        private Object clientSetStatusLabel;
        @SerializedName("client_set_status_value")
        @Expose
        private Object clientSetStatusValue;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("device_id")
        @Expose
        private String deviceId;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("dob_day")
        @Expose
        private Object dobDay;
        @SerializedName("dob_month")
        @Expose
        private Object dobMonth;
        @SerializedName("dob_year")
        @Expose
        private Object dobYear;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("facebookID")
        @Expose
        private Object facebookID;
        @SerializedName("favorite_location")
        @Expose
        private Object favoriteLocation;
        @SerializedName("favorite_menu_item")
        @Expose
        private String favoriteMenuItem;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("gender")
        @Expose
        private Object gender;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("last_name")
        @Expose
        private Object lastName;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("locale_id")
        @Expose
        private Integer localeId;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("marketing_optin")
        @Expose
        private Boolean marketingOptin;
        @SerializedName("marketing_optin_texting")
        @Expose
        private Object marketingOptinTexting;
        @SerializedName("milestone")
        @Expose
        private Object milestone;
        @SerializedName("miss_you_date")
        @Expose
        private Object missYouDate;
        @SerializedName("ncr_aoo_payment_created")
        @Expose
        private Boolean ncrAooPaymentCreated;
        @SerializedName("new_reward_notification")
        @Expose
        private Boolean newRewardNotification;
        @SerializedName("no_scan_date")
        @Expose
        private Object noScanDate;
        @SerializedName("os_version")
        @Expose
        private String osVersion;
        @SerializedName("phone_model")
        @Expose
        private String phoneModel;
        @SerializedName("phone_number")
        @Expose
        private Object phoneNumber;
        @SerializedName("point_threshold")
        @Expose
        private Object pointThreshold;
        @SerializedName("points")
        @Expose
        private Integer points;
        @SerializedName("push_device")
        @Expose
        private Boolean pushDevice;
        @SerializedName("push_email")
        @Expose
        private Boolean pushEmail;
        @SerializedName("ref_code")
        @Expose
        private String refCode;
        @SerializedName("register_device_type")
        @Expose
        private String registerDeviceType;
        @SerializedName("register_type")
        @Expose
        private Integer registerType;
        @SerializedName("selected_device_info")
        @Expose
        private String selectedDeviceInfo;
        @SerializedName("sign_in_device_type")
        @Expose
        private String signInDeviceType;
        @SerializedName("sign_in_device_type_before")
        @Expose
        private Object signInDeviceTypeBefore;
        @SerializedName("signup_device_status")
        @Expose
        private Integer signupDeviceStatus;
        @SerializedName("special_occassion")
        @Expose
        private Object specialOccassion;
        @SerializedName("state")
        @Expose
        private Object state;
        @SerializedName("step_milestone_receipt_loop")
        @Expose
        private Boolean stepMilestoneReceiptLoop;
        @SerializedName("total_points_earned")
        @Expose
        private Integer totalPointsEarned;
        @SerializedName("twitterID")
        @Expose
        private Object twitterID;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("username")
        @Expose
        private Object username;
        @SerializedName("week_generated_at")
        @Expose
        private Object weekGeneratedAt;
        @SerializedName("zipcode")
        @Expose
        private Object zipcode;
        @SerializedName("profile_pic")
        @Expose
        private Object profilePic;
        @SerializedName("favorite_location_name")
        @Expose
        private Object favoriteLocationName;
        @SerializedName("milestone_points")
        @Expose
        private Integer milestonePoints;
        @SerializedName("weekly_receipt_approved")
        @Expose
        private Integer weeklyReceiptApproved;
        @SerializedName("monthly_receipt_approved")
        @Expose
        private Integer monthlyReceiptApproved;
        @SerializedName("yearly_receipt_approved")
        @Expose
        private Integer yearlyReceiptApproved;
        @SerializedName("mall_employee")
        @Expose
        private Object mallEmployee;
        @SerializedName("retailer")
        @Expose
        private Object retailer;

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public Object getAddress1() {
            return address1;
        }

        public void setAddress1(Object address1) {
            this.address1 = address1;
        }

        public Object getAddress2() {
            return address2;
        }

        public void setAddress2(Object address2) {
            this.address2 = address2;
        }

        public Boolean getAllowToUpdateGoals() {
            return allowToUpdateGoals;
        }

        public void setAllowToUpdateGoals(Boolean allowToUpdateGoals) {
            this.allowToUpdateGoals = allowToUpdateGoals;
        }

        public Object getAppUsagePurpose() {
            return appUsagePurpose;
        }

        public void setAppUsagePurpose(Object appUsagePurpose) {
            this.appUsagePurpose = appUsagePurpose;
        }

        public Object getBraintreeIdentifier() {
            return braintreeIdentifier;
        }

        public void setBraintreeIdentifier(Object braintreeIdentifier) {
            this.braintreeIdentifier = braintreeIdentifier;
        }

        public Boolean getBraintreeUserCreated() {
            return braintreeUserCreated;
        }

        public void setBraintreeUserCreated(Boolean braintreeUserCreated) {
            this.braintreeUserCreated = braintreeUserCreated;
        }

        public String getChainAppKey() {
            return chainAppKey;
        }

        public void setChainAppKey(String chainAppKey) {
            this.chainAppKey = chainAppKey;
        }

        public Integer getChainId() {
            return chainId;
        }

        public void setChainId(Integer chainId) {
            this.chainId = chainId;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getClientSetStatusLabel() {
            return clientSetStatusLabel;
        }

        public void setClientSetStatusLabel(Object clientSetStatusLabel) {
            this.clientSetStatusLabel = clientSetStatusLabel;
        }

        public Object getClientSetStatusValue() {
            return clientSetStatusValue;
        }

        public void setClientSetStatusValue(Object clientSetStatusValue) {
            this.clientSetStatusValue = clientSetStatusValue;
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

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public Object getDobDay() {
            return dobDay;
        }

        public void setDobDay(Object dobDay) {
            this.dobDay = dobDay;
        }

        public Object getDobMonth() {
            return dobMonth;
        }

        public void setDobMonth(Object dobMonth) {
            this.dobMonth = dobMonth;
        }

        public Object getDobYear() {
            return dobYear;
        }

        public void setDobYear(Object dobYear) {
            this.dobYear = dobYear;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getFacebookID() {
            return facebookID;
        }

        public void setFacebookID(Object facebookID) {
            this.facebookID = facebookID;
        }

        public Object getFavoriteLocation() {
            return favoriteLocation;
        }

        public void setFavoriteLocation(Object favoriteLocation) {
            this.favoriteLocation = favoriteLocation;
        }

        public String getFavoriteMenuItem() {
            return favoriteMenuItem;
        }

        public void setFavoriteMenuItem(String favoriteMenuItem) {
            this.favoriteMenuItem = favoriteMenuItem;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Object getLastName() {
            return lastName;
        }

        public void setLastName(Object lastName) {
            this.lastName = lastName;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public Integer getLocaleId() {
            return localeId;
        }

        public void setLocaleId(Integer localeId) {
            this.localeId = localeId;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public Boolean getMarketingOptin() {
            return marketingOptin;
        }

        public void setMarketingOptin(Boolean marketingOptin) {
            this.marketingOptin = marketingOptin;
        }

        public Object getMarketingOptinTexting() {
            return marketingOptinTexting;
        }

        public void setMarketingOptinTexting(Object marketingOptinTexting) {
            this.marketingOptinTexting = marketingOptinTexting;
        }

        public Object getMilestone() {
            return milestone;
        }

        public void setMilestone(Object milestone) {
            this.milestone = milestone;
        }

        public Object getMissYouDate() {
            return missYouDate;
        }

        public void setMissYouDate(Object missYouDate) {
            this.missYouDate = missYouDate;
        }

        public Boolean getNcrAooPaymentCreated() {
            return ncrAooPaymentCreated;
        }

        public void setNcrAooPaymentCreated(Boolean ncrAooPaymentCreated) {
            this.ncrAooPaymentCreated = ncrAooPaymentCreated;
        }

        public Boolean getNewRewardNotification() {
            return newRewardNotification;
        }

        public void setNewRewardNotification(Boolean newRewardNotification) {
            this.newRewardNotification = newRewardNotification;
        }

        public Object getNoScanDate() {
            return noScanDate;
        }

        public void setNoScanDate(Object noScanDate) {
            this.noScanDate = noScanDate;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getPhoneModel() {
            return phoneModel;
        }

        public void setPhoneModel(String phoneModel) {
            this.phoneModel = phoneModel;
        }

        public Object getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(Object phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Object getPointThreshold() {
            return pointThreshold;
        }

        public void setPointThreshold(Object pointThreshold) {
            this.pointThreshold = pointThreshold;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

        public Boolean getPushDevice() {
            return pushDevice;
        }

        public void setPushDevice(Boolean pushDevice) {
            this.pushDevice = pushDevice;
        }

        public Boolean getPushEmail() {
            return pushEmail;
        }

        public void setPushEmail(Boolean pushEmail) {
            this.pushEmail = pushEmail;
        }

        public String getRefCode() {
            return refCode;
        }

        public void setRefCode(String refCode) {
            this.refCode = refCode;
        }

        public String getRegisterDeviceType() {
            return registerDeviceType;
        }

        public void setRegisterDeviceType(String registerDeviceType) {
            this.registerDeviceType = registerDeviceType;
        }

        public Integer getRegisterType() {
            return registerType;
        }

        public void setRegisterType(Integer registerType) {
            this.registerType = registerType;
        }

        public String getSelectedDeviceInfo() {
            return selectedDeviceInfo;
        }

        public void setSelectedDeviceInfo(String selectedDeviceInfo) {
            this.selectedDeviceInfo = selectedDeviceInfo;
        }

        public String getSignInDeviceType() {
            return signInDeviceType;
        }

        public void setSignInDeviceType(String signInDeviceType) {
            this.signInDeviceType = signInDeviceType;
        }

        public Object getSignInDeviceTypeBefore() {
            return signInDeviceTypeBefore;
        }

        public void setSignInDeviceTypeBefore(Object signInDeviceTypeBefore) {
            this.signInDeviceTypeBefore = signInDeviceTypeBefore;
        }

        public Integer getSignupDeviceStatus() {
            return signupDeviceStatus;
        }

        public void setSignupDeviceStatus(Integer signupDeviceStatus) {
            this.signupDeviceStatus = signupDeviceStatus;
        }

        public Object getSpecialOccassion() {
            return specialOccassion;
        }

        public void setSpecialOccassion(Object specialOccassion) {
            this.specialOccassion = specialOccassion;
        }

        public Object getState() {
            return state;
        }

        public void setState(Object state) {
            this.state = state;
        }

        public Boolean getStepMilestoneReceiptLoop() {
            return stepMilestoneReceiptLoop;
        }

        public void setStepMilestoneReceiptLoop(Boolean stepMilestoneReceiptLoop) {
            this.stepMilestoneReceiptLoop = stepMilestoneReceiptLoop;
        }

        public Integer getTotalPointsEarned() {
            return totalPointsEarned;
        }

        public void setTotalPointsEarned(Integer totalPointsEarned) {
            this.totalPointsEarned = totalPointsEarned;
        }

        public Object getTwitterID() {
            return twitterID;
        }

        public void setTwitterID(Object twitterID) {
            this.twitterID = twitterID;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }

        public Object getWeekGeneratedAt() {
            return weekGeneratedAt;
        }

        public void setWeekGeneratedAt(Object weekGeneratedAt) {
            this.weekGeneratedAt = weekGeneratedAt;
        }

        public Object getZipcode() {
            return zipcode;
        }

        public void setZipcode(Object zipcode) {
            this.zipcode = zipcode;
        }

        public Object getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(Object profilePic) {
            this.profilePic = profilePic;
        }

        public Object getFavoriteLocationName() {
            return favoriteLocationName;
        }

        public void setFavoriteLocationName(Object favoriteLocationName) {
            this.favoriteLocationName = favoriteLocationName;
        }

        public Integer getMilestonePoints() {
            return milestonePoints;
        }

        public void setMilestonePoints(Integer milestonePoints) {
            this.milestonePoints = milestonePoints;
        }

        public Integer getWeeklyReceiptApproved() {
            return weeklyReceiptApproved;
        }

        public void setWeeklyReceiptApproved(Integer weeklyReceiptApproved) {
            this.weeklyReceiptApproved = weeklyReceiptApproved;
        }

        public Integer getMonthlyReceiptApproved() {
            return monthlyReceiptApproved;
        }

        public void setMonthlyReceiptApproved(Integer monthlyReceiptApproved) {
            this.monthlyReceiptApproved = monthlyReceiptApproved;
        }

        public Integer getYearlyReceiptApproved() {
            return yearlyReceiptApproved;
        }

        public void setYearlyReceiptApproved(Integer yearlyReceiptApproved) {
            this.yearlyReceiptApproved = yearlyReceiptApproved;
        }

        public Object getMallEmployee() {
            return mallEmployee;
        }

        public void setMallEmployee(Object mallEmployee) {
            this.mallEmployee = mallEmployee;
        }

        public Object getRetailer() {
            return retailer;
        }

        public void setRetailer(Object retailer) {
            this.retailer = retailer;
        }

    }
}

