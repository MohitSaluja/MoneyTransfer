package com.assignment.moneytransfer.api;

public class TransferResponse {

    private String referenceId;

    public TransferResponse() {
    }

    public TransferResponse(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
