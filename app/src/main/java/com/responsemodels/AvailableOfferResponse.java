package com.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvailableOfferResponse {

    @SerializedName("chain_id")
    @Expose
    private Integer chainId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("effectiveDate")
    @Expose
    private String effectiveDate;
    @SerializedName("enable_ncr_survey")
    @Expose
    private Boolean enableNcrSurvey;
    @SerializedName("expiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("having_rule")
    @Expose
    private Boolean havingRule;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_multiplier")
    @Expose
    private Boolean isMultiplier;
    @SerializedName("is_online_order")
    @Expose
    private Boolean isOnlineOrder;
    @SerializedName("max_amount")
    @Expose
    private Double maxAmount;
    @SerializedName("min_amount")
    @Expose
    private Double minAmount;
    @SerializedName("min_subtotal_criteria_for_receipt_approval")
    @Expose
    private Double minSubtotalCriteriaForReceiptApproval;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ncr_assign_loyalty_offer")
    @Expose
    private Boolean ncrAssignLoyaltyOffer;
    @SerializedName("survey_id")
    @Expose
    private Integer surveyId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Boolean getEnableNcrSurvey() {
        return enableNcrSurvey;
    }

    public void setEnableNcrSurvey(Boolean enableNcrSurvey) {
        this.enableNcrSurvey = enableNcrSurvey;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getHavingRule() {
        return havingRule;
    }

    public void setHavingRule(Boolean havingRule) {
        this.havingRule = havingRule;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsMultiplier() {
        return isMultiplier;
    }

    public void setIsMultiplier(Boolean isMultiplier) {
        this.isMultiplier = isMultiplier;
    }

    public Boolean getIsOnlineOrder() {
        return isOnlineOrder;
    }

    public void setIsOnlineOrder(Boolean isOnlineOrder) {
        this.isOnlineOrder = isOnlineOrder;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMinSubtotalCriteriaForReceiptApproval() {
        return minSubtotalCriteriaForReceiptApproval;
    }

    public void setMinSubtotalCriteriaForReceiptApproval(Double minSubtotalCriteriaForReceiptApproval) {
        this.minSubtotalCriteriaForReceiptApproval = minSubtotalCriteriaForReceiptApproval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNcrAssignLoyaltyOffer() {
        return ncrAssignLoyaltyOffer;
    }

    public void setNcrAssignLoyaltyOffer(Boolean ncrAssignLoyaltyOffer) {
        this.ncrAssignLoyaltyOffer = ncrAssignLoyaltyOffer;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public static class OperatingHour {

        @SerializedName("close_at")
        @Expose
        private String closeAt;
        @SerializedName("day_of_week")
        @Expose
        private Integer dayOfWeek;
        @SerializedName("open_at")
        @Expose
        private String openAt;

        public String getCloseAt() {
            return closeAt;
        }

        public void setCloseAt(String closeAt) {
            this.closeAt = closeAt;
        }

        public Integer getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getOpenAt() {
            return openAt;
        }

        public void setOpenAt(String openAt) {
            this.openAt = openAt;
        }


        public static class Restaurant {

            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("app_display_text")
            @Expose
            private String appDisplayText;
            @SerializedName("external_partner_id")
            @Expose
            private String externalPartnerId;
            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("latitude")
            @Expose
            private Double latitude;
            @SerializedName("longitude")
            @Expose
            private Double longitude;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("online_order_support_status")
            @Expose
            private String orderLink;
            @SerializedName("online_order_link")
            @Expose
            private Boolean onlineOrderSupportStatus;
            @SerializedName("phone_number")
            @Expose
            private String phoneNumber;
            @SerializedName("zipcode")
            @Expose
            private String zipcode;
            @SerializedName("today_open_hour")
            @Expose
            private Restaurants.TodayOpenHour todayOpenHour;
            @SerializedName("restaurant_distance")
            @Expose
            private Double restaurantDistance;
            @SerializedName("is_open")
            @Expose
            private Boolean isOpen;
            @SerializedName("city_label")
            @Expose
            private String cityLabel;
            @SerializedName("state_label")
            @Expose
            private String stateLabel;
            @SerializedName("country_label")
            @Expose
            private String countryLabel;
            @SerializedName("available_offers")
            @Expose
            private List<AvailableOfferResponse> availableOffers = null;
            @SerializedName("operating_hours")
            @Expose
            private List<OperatingHour> operatingHours = null;
            @SerializedName("online_partner")
            @Expose
            private Integer onlinePartner;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAppDisplayText() {
                return appDisplayText;
            }

            public void setAppDisplayText(String appDisplayText) {
                this.appDisplayText = appDisplayText;
            }

            public String getExternalPartnerId() {
                return externalPartnerId;
            }

            public void setExternalPartnerId(String externalPartnerId) {
                this.externalPartnerId = externalPartnerId;
            }

            public String getOrderLink() {
                return orderLink;
            }

            public void setOrderLink(String orderLink) {
                this.orderLink = orderLink;
            }
            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Double getLatitude() {
                return latitude;
            }

            public void setLatitude(Double latitude) {
                this.latitude = latitude;
            }

            public Double getLongitude() {
                return longitude;
            }

            public void setLongitude(Double longitude) {
                this.longitude = longitude;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Boolean getOnlineOrderSupportStatus() {
                return onlineOrderSupportStatus;
            }

            public void setOnlineOrderSupportStatus(Boolean onlineOrderSupportStatus) {
                this.onlineOrderSupportStatus = onlineOrderSupportStatus;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public String getZipcode() {
                return zipcode;
            }

            public void setZipcode(String zipcode) {
                this.zipcode = zipcode;
            }

            public Restaurants.TodayOpenHour getTodayOpenHour() {
                return todayOpenHour;
            }

            public void setTodayOpenHour(Restaurants.TodayOpenHour todayOpenHour) {
                this.todayOpenHour = todayOpenHour;
            }

            public Double getRestaurantDistance() {
                return restaurantDistance;
            }

            public void setRestaurantDistance(Double restaurantDistance) {
                this.restaurantDistance = restaurantDistance;
            }

            public Boolean getIsOpen() {
                return isOpen;
            }

            public void setIsOpen(Boolean isOpen) {
                this.isOpen = isOpen;
            }

            public String getCityLabel() {
                return cityLabel;
            }

            public void setCityLabel(String cityLabel) {
                this.cityLabel = cityLabel;
            }

            public String getStateLabel() {
                return stateLabel;
            }

            public void setStateLabel(String stateLabel) {
                this.stateLabel = stateLabel;
            }

            public String getCountryLabel() {
                return countryLabel;
            }

            public void setCountryLabel(String countryLabel) {
                this.countryLabel = countryLabel;
            }

            public List<AvailableOfferResponse> getAvailableOffers() {
                return availableOffers;
            }

            public void setAvailableOffers(List<AvailableOfferResponse> availableOffers) {
                this.availableOffers = availableOffers;
            }

            public List<OperatingHour> getOperatingHours() {
                return operatingHours;
            }

            public void setOperatingHours(List<OperatingHour> operatingHours) {
                this.operatingHours = operatingHours;
            }

            public Integer getOnlinePartner() {
                return onlinePartner;
            }

            public void setOnlinePartner(Integer onlinePartner) {
                this.onlinePartner = onlinePartner;
            }


            public static class Restaurants {

                @SerializedName("status")
                @Expose
                private Boolean status;
                @SerializedName("restaurants")
                @Expose
                private List<Restaurant> restaurants = null;

                public Boolean getStatus() {
                    return status;
                }

                public void setStatus(Boolean status) {
                    this.status = status;
                }

                public List<Restaurant> getRestaurants() {
                    return restaurants;
                }

                public void setRestaurants(List<Restaurant> restaurants) {
                    this.restaurants = restaurants;
                }

                public class TodayOpenHour {

                    @SerializedName("close_at")
                    @Expose
                    private String closeAt;
                    @SerializedName("day_of_week")
                    @Expose
                    private Integer dayOfWeek;
                    @SerializedName("open_at")
                    @Expose
                    private String openAt;

                    public String getCloseAt() {
                        return closeAt;
                    }

                    public void setCloseAt(String closeAt) {
                        this.closeAt = closeAt;
                    }

                    public Integer getDayOfWeek() {
                        return dayOfWeek;
                    }

                    public void setDayOfWeek(Integer dayOfWeek) {
                        this.dayOfWeek = dayOfWeek;
                    }

                    public String getOpenAt() {
                        return openAt;
                    }

                    public void setOpenAt(String openAt) {
                        this.openAt = openAt;
                    }

                }
            }
        }
    }
}