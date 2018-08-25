package com.responsemodels;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LastTransactionResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("total_points_earned")
    @Expose
    private String totalPointsEarned;
    @SerializedName("base_points_earned")
    @Expose
    private String basePointsEarned;
    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public void setTotalPointsEarned(String totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }

    public String getBasePointsEarned() {
        return basePointsEarned;
    }

    public void setBasePointsEarned(String basePointsEarned) {
        this.basePointsEarned = basePointsEarned;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public static class Receipt {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("activity_type")
        @Expose
        private String activityType;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("restaurant")
        @Expose
        private String restaurant;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("amount")
        @Expose
        private Double amount;
        @SerializedName("last_transaction")
        @Expose
        private LastTransactionResponse lastTransaction;


        public String getRestaurant() {
            return restaurant;
        }

        public void setRestaurant(String restaurant) {
            this.restaurant = restaurant;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public LastTransactionResponse getLastTransaction() {
            return lastTransaction;
        }

        public void setLastTransaction(LastTransactionResponse lastTransaction) {
            this.lastTransaction = lastTransaction;
        }


        public static class RewardActivityResponse {

            @SerializedName("status")
            @Expose
            private Boolean status;
            @SerializedName("receipts")
            @Expose
            private List<Receipt> receipts = null;

            public Boolean getStatus() {
                return status;
            }

            public void setStatus(Boolean status) {
                this.status = status;
            }

            public List<Receipt> getReceipts() {
                return receipts;
            }

            public void setReceipts(List<Receipt> receipts) {
                this.receipts = receipts;
            }
        }
    }

}