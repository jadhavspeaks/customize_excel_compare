package com.recon.model;

public class ComparisonResult {

    public enum Status {
        PASS,
        FAIL
    }

    private String attribute;
    private String reportOrProduct;
    private Status status;
    private String reason;

    public ComparisonResult(String attribute, String reportOrProduct, Status status, String reason) {
        this.attribute = attribute;
        this.reportOrProduct = reportOrProduct;
        this.status = status;
        this.reason = reason;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getReportOrProduct() {
        return reportOrProduct;
    }

    public void setReportOrProduct(String reportOrProduct) {
        this.reportOrProduct = reportOrProduct;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
