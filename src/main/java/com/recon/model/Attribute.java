package com.recon.model;

import java.util.List;

public class Attribute {
    private String name;
    private List<String> reports;
    private List<String> products;

    public Attribute(String name, List<String> reports, List<String> products) {
        this.name = name;
        this.reports = reports;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getReports() {
        return reports;
    }

    public void setReports(List<String> reports) {
        this.reports = reports;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
