package com.example.userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    public UserDetails userDetails;
    @SerializedName("token")
    @Expose
    public String token;

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

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class UserDetails {

        @SerializedName("id")
        @Expose
        public Long id;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("marathi_name")
        @Expose
        public String marathiName;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("mobile_number")
        @Expose
        public String mobileNumber;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("marathi_address")
        @Expose
        public String marathiAddress;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("email_verified_at")
        @Expose
        public Object emailVerifiedAt;
        @SerializedName("password_token")
        @Expose
        public Object passwordToken;
        @SerializedName("pass_token_status")
        @Expose
        public Object passTokenStatus;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMarathiName() {
            return marathiName;
        }

        public void setMarathiName(String marathiName) {
            this.marathiName = marathiName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMarathiAddress() {
            return marathiAddress;
        }

        public void setMarathiAddress(String marathiAddress) {
            this.marathiAddress = marathiAddress;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(Object emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
        }

        public Object getPasswordToken() {
            return passwordToken;
        }

        public void setPasswordToken(Object passwordToken) {
            this.passwordToken = passwordToken;
        }

        public Object getPassTokenStatus() {
            return passTokenStatus;
        }

        public void setPassTokenStatus(Object passTokenStatus) {
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

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }
    }
}
