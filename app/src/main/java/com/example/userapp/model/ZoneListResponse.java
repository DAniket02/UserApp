package com.example.userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ZoneListResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("zonelist")
    @Expose
    public List<Zonelist> zonelist = null;

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

    public List<Zonelist> getZonelist() {
        return zonelist;
    }

    public void setZonelist(List<Zonelist> zonelist) {
        this.zonelist = zonelist;
    }

    public class Zonelist {

        @SerializedName("id")
        @Expose
        public Long id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("created_at")
        @Expose
        public Object createdAt;
        @SerializedName("updated_at")
        @Expose
        public Object updatedAt;

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

        public Object getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Object createdAt) {
            this.createdAt = createdAt;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Object updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
