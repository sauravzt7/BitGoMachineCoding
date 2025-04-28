package com.machinecoding.Solution.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Transaction {
    public String txnId;
    public List<String> parentIds;

    public Transaction() {
        // Default constructor for Jackson
    }

    public Transaction(String txnId, List<String> parentIds) {
        this.txnId = txnId;
        this.parentIds = parentIds;
    }

    @JsonProperty("txnId")
    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    @JsonProperty("parentIds")
    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }
}