package com.fundraiser.fundraiser.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payments {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fund_id")
    @Expose
    private Integer fundId;
    @SerializedName("mpesa_phone")
    @Expose
    private String mpesaPhone;
    @SerializedName("mpesa_paybill")
    @Expose
    private Object mpesaPaybill;
    @SerializedName("mpesa_account_number")
    @Expose
    private Object mpesaAccountNumber;
    @SerializedName("bank_account")
    @Expose
    private Object bankAccount;
    @SerializedName("paypal_account")
    @Expose
    private Object paypalAccount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public String getMpesaPhone() {
        return mpesaPhone;
    }

    public void setMpesaPhone(String mpesaPhone) {
        this.mpesaPhone = mpesaPhone;
    }

    public Object getMpesaPaybill() {
        return mpesaPaybill;
    }

    public void setMpesaPaybill(Object mpesaPaybill) {
        this.mpesaPaybill = mpesaPaybill;
    }

    public Object getMpesaAccountNumber() {
        return mpesaAccountNumber;
    }

    public void setMpesaAccountNumber(Object mpesaAccountNumber) {
        this.mpesaAccountNumber = mpesaAccountNumber;
    }

    public Object getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Object bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Object getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(Object paypalAccount) {
        this.paypalAccount = paypalAccount;
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
