package com.wts.chillarpay.model;

public class MyCreditDebitModel {
    String DrUser;
    String CrUser;
    String id;
    String Amount;
    String PaymentType;
    String PaymentDate;
    String Remarks;

    public String getDrUser() {
        return DrUser;
    }

    public void setDrUser(String drUser) {
        DrUser = drUser;
    }

    public String getCrUser() {
        return CrUser;
    }

    public void setCrUser(String crUser) {
        CrUser = crUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
