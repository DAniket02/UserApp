package com.example.userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicationListResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("requestList")
    @Expose
    public List<RequestList> requestList = null;

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

    public List<RequestList> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestList> requestList) {
        this.requestList = requestList;
    }

    public class RequestList {

        @SerializedName("id")
        @Expose
        private Long id;
        @SerializedName("prajati")
        @Expose
        private String prajati;
        @SerializedName("tree_total")
        @Expose
        private String treeTotal;
        @SerializedName("reason")
        @Expose
        private String reason;
        @SerializedName("zone")
        @Expose
        private String zone;
        @SerializedName("department")
        @Expose
        private String department;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("last_status")
        @Expose
        private String lastStatus;
        @SerializedName("application_status")
        @Expose
        private Long applicationStatus;
        @SerializedName("created_by")
        @Expose
        private Long createdBy;
        @SerializedName("modified_by")
        @Expose
        private Long modifiedBy;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("zone_name")
        @Expose
        private String zoneName;

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

        public String getZoneName() {
            return zoneName;
        }

        public void setZoneName(String zoneName) {
            this.zoneName = zoneName;
        }
    }
}
