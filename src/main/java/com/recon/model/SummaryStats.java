package com.recon.model;

public class SummaryStats {

    private int totalAttributes;
    private int passed;
    private int failed;
    private int missingAttributes;
    private int reportMismatches;
    private int productMismatches;

    public void incrementPassed() {
        passed++;
    }

    public void incrementFailed() {
        failed++;
    }

    public void incrementMissingAttributes() {
        missingAttributes++;
    }

    public void incrementReportMismatches() {
        reportMismatches++;
    }

    public void incrementProductMismatches() {
        productMismatches++;
    }

    // Getters and Setters

    public int getTotalAttributes() {
        return totalAttributes;
    }

    public void setTotalAttributes(int totalAttributes) {
        this.totalAttributes = totalAttributes;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getMissingAttributes() {
        return missingAttributes;
    }

    public void setMissingAttributes(int missingAttributes) {
        this.missingAttributes = missingAttributes;
    }

    public int getReportMismatches() {
        return reportMismatches;
    }

    public void setReportMismatches(int reportMismatches) {
        this.reportMismatches = reportMismatches;
    }

    public int getProductMismatches() {
        return productMismatches;
    }

    public void setProductMismatches(int productMismatches) {
        this.productMismatches = productMismatches;
    }
}
