package com.wts.chillarpay.model;

public class MyLedgerModel {
    String old_bal,new_bal,transaction_type,remarks,transaction_date,cr_dr_type,amount;

    public String getOld_bal() {
        return old_bal;
    }

    public void setOld_bal(String old_bal) {
        this.old_bal = old_bal;
    }

    public String getNew_bal() {
        return new_bal;
    }

    public void setNew_bal(String new_bal) {
        this.new_bal = new_bal;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getCr_dr_type() {
        return cr_dr_type;
    }

    public void setCr_dr_type(String cr_dr_type) {
        this.cr_dr_type = cr_dr_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
