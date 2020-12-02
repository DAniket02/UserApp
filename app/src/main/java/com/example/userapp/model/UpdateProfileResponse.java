package com.example.userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public UpdatedData updatedData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UpdatedData getUpdatedData() {
        return updatedData;
    }

    public void setUpdatedData(UpdatedData updatedData) {
        this.updatedData = updatedData;
    }

    public class UpdatedData {

        @SerializedName("profile_pic")
        @Expose
        public String profilePic;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("mobile_number")
        @Expose
        public String mobileNumber;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("marathi_name")
        @Expose
        public String marathiName;
        @SerializedName("marathi_address")
        @Expose
        public String marathiAddress;
        @SerializedName("email_verified_at")
        @Expose
        private Object emailVerifiedAt;
        @SerializedName("password_token")
        @Expose
        private String passwordToken;
        @SerializedName("pass_token_status")
        @Expose
        private Long passTokenStatus;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMarathiName() {
            return marathiName;
        }

        public void setMarathiName(String marathiName) {
            this.marathiName = marathiName;
        }

        public String getMarathiAddress() {
            return marathiAddress;
        }

        public void setMarathiAddress(String marathiAddress) {
            this.marathiAddress = marathiAddress;
        }

        public Object getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(Object emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
        }

        public String getPasswordToken() {
            return passwordToken;
        }

        public void setPasswordToken(String passwordToken) {
            this.passwordToken = passwordToken;
        }

        public Long getPassTokenStatus() {
            return passTokenStatus;
        }

        public void setPassTokenStatus(Long passTokenStatus) {
            this.passTokenStatus = passTokenStatus;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
