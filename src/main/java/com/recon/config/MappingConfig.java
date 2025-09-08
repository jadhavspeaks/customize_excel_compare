package com.recon.config;

import java.util.List;

public class MappingConfig {

    private String excel1Path;
    private String excel2Path;

    // Excel 1 column selections
    private String excel1AttributeColumn;
    private List<String> excel1ReportColumns;
    private List<String> excel1ProductColumns;

    // Excel 2 column selections
    private List<String> excel2AttributeNameColumns;
    private String excel2ReportColumn;
    private String excel2ProductColumn;

    // Getters and Setters

    public String getExcel1Path() {
        return excel1Path;
    }

    public void setExcel1Path(String excel1Path) {
        this.excel1Path = excel1Path;
    }

    public String getExcel2Path() {
        return excel2Path;
    }

    public void setExcel2Path(String excel2Path) {
        this.excel2Path = excel2Path;
    }

    public String getExcel1AttributeColumn() {
        return excel1AttributeColumn;
    }

    public void setExcel1AttributeColumn(String excel1AttributeColumn) {
        this.excel1AttributeColumn = excel1AttributeColumn;
    }

    public List<String> getExcel1ReportColumns() {
        return excel1ReportColumns;
    }

    public void setExcel1ReportColumns(List<String> excel1ReportColumns) {
        this.excel1ReportColumns = excel1ReportColumns;
    }

    public List<String> getExcel1ProductColumns() {
        return excel1ProductColumns;
    }

    public void setExcel1ProductColumns(List<String> excel1ProductColumns) {
        this.excel1ProductColumns = excel1ProductColumns;
    }

    public List<String> getExcel2AttributeNameColumns() {
        return excel2AttributeNameColumns;
    }

    public void setExcel2AttributeNameColumns(List<String> excel2AttributeNameColumns) {
        this.excel2AttributeNameColumns = excel2AttributeNameColumns;
    }

    public String getExcel2ReportColumn() {
        return excel2ReportColumn;
    }

    public void setExcel2ReportColumn(String excel2ReportColumn) {
        this.excel2ReportColumn = excel2ReportColumn;
    }

    public String getExcel2ProductColumn() {
        return excel2ProductColumn;
    }

    public void setExcel2ProductColumn(String excel2ProductColumn) {
        this.excel2ProductColumn = excel2ProductColumn;
    }
}
