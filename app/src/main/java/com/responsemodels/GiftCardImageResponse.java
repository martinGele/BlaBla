package com.responsemodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftCardImageResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }


    public static class Image {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("app_image_url")
        @Expose
        private String appImageUrl;
        @SerializedName("email_header_image_url")
        @Expose
        private String emailHeaderImageUrl;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAppImageUrl() {
            return appImageUrl;
        }

        public void setAppImageUrl(String appImageUrl) {
            this.appImageUrl = appImageUrl;
        }

        public String getEmailHeaderImageUrl() {
            return emailHeaderImageUrl;
        }

        public void setEmailHeaderImageUrl(String emailHeaderImageUrl) {
            this.emailHeaderImageUrl = emailHeaderImageUrl;
        }

    }
}