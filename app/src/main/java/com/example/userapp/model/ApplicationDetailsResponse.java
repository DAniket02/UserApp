package com.example.userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicationDetailsResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public ApplicationDetail applicationDetail;

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

    public ApplicationDetail getData() {
        return applicationDetail;
    }

    public void setData(ApplicationDetail applicationDetail) {
        this.applicationDetail = applicationDetail;
    }

    public class ApplicationDetail {

        @SerializedName("id")
        @Expose
        public Long id;
        @SerializedName("prajati")
        @Expose
        public String prajati;
        @SerializedName("tree_total")
        @Expose
        public String treeTotal;
        @SerializedName("reason")
        @Expose
        public String reason;
        @SerializedName("zone")
        @Expose
        public String zone;
        @SerializedName("department")
        @Expose
        public String department;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("last_status")
        @Expose
        public String lastStatus;
        @SerializedName("application_status")
        @Expose
        public Long applicationStatus;
        @SerializedName("created_by")
        @Expose
        public Long createdBy;
        @SerializedName("modified_by")
        @Expose
        public Long modifiedBy;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("tree_pic_name")
        @Expose
        public Object treePicName;
        @SerializedName("ownership_pic_name")
        @Expose
        public Object ownershipPicName;
        @SerializedName("joint_owner_pic_name")
        @Expose
        public Object jointOwnerPicName;
        @SerializedName("construction_pic_name")
        @Expose
        public Object constructionPicName;
        @SerializedName("location_pic_name")
        @Expose
        public Object locationPicName;
        @SerializedName("payment_status")
        @Expose
        public Long paymentStatus;
        @SerializedName("expected_completed_date")
        @Expose
        public String expectedCompletedDate;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPrajati() {
            return prajati;
        }

        public void setPrajati(String prajati) {
            this.prajati = prajati;
        }

        public String getTreeTotal() {
            return treeTotal;
        }

        public void setTreeTotal(String treeTotal) {
            this.treeTotal = treeTotal;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLastStatus() {
            return lastStatus;
        }

        public void setLastStatus(String lastStatus) {
            this.lastStatus = lastStatus;
        }

        public Long getApplicationStatus() {
            return applicationStatus;
        }

        public void setApplicationStatus(Long applicationStatus) {
            this.applicationStatus = applicationStatus;
        }

        public Long getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Long createdBy) {
            this.createdBy = createdBy;
        }

        public Long getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(Long modifiedBy) {
            this.modifiedBy = modifiedBy;
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

        public Object getTreePicName() {
            return treePicName;
        }

        public void setTreePicName(Object treePicName) {
            this.treePicName = treePicName;
        }

        public Object getOwnershipPicName() {
            return ownershipPicName;
        }

        public void setOwnershipPicName(Object ownershipPicName) {
            this.ownershipPicName = ownershipPicName;
        }

        public Object getJointOwnerPicName() {
            return jointOwnerPicName;
        }

        public void setJointOwnerPicName(Object jointOwnerPicName) {
            this.jointOwnerPicName = jointOwnerPicName;
        }

        public Object getConstructionPicName() {
            return constructionPicName;
        }

        public void setConstructionPicName(Object constructionPicName) {
            this.constructionPicName = constructionPicName;
        }

        public Object getLocationPicName() {
            return locationPicName;
        }

        public void setLocationPicName(Object locationPicName) {
            this.locationPicName = locationPicName;
        }

        public Long getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(Long paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getExpectedCompletedDate() {
            return expectedCompletedDate;
        }

        public void setExpectedCompletedDate(String expectedCompletedDate) {
            this.expectedCompletedDate = expectedCompletedDate;
        }

    }
}
