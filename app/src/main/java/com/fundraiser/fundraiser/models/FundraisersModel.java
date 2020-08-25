package com.fundraiser.fundraiser.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FundraisersModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("passcode")
    @Expose
    private Integer passcode;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("pin")
    @Expose
    private Integer pin;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("contribution")
    @Expose
    private Contribution contribution;
    @SerializedName("payments")
    @Expose
    private Payments payments;

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

    public Integer getPasscode() {
        return passcode;
    }

    public void setPasscode(Integer passcode) {
        this.passcode = passcode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Contribution getContribution() {
        return contribution;
    }

    public void setContribution(Contribution contribution) {
        this.contribution = contribution;
    }

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }
}
