package com.recon.report;

import com.recon.model.ComparisonResult;
import com.recon.model.SummaryStats;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ExcelReportGenerator {

    public String generateReport(SummaryStats stats, List<ComparisonResult> results) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create cell styles for headers and pass/fail results
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle passStyle = createColorStyle(workbook, IndexedColors.LIGHT_GREEN.getIndex());
            CellStyle failStyle = createColorStyle(workbook, IndexedColors.PALE_BLUE.getIndex()); // Using PALE_BLUE for red text contrast

            // Create the two sheets
            createSummarySheet(workbook, stats, headerStyle);
            createDetailedSheet(workbook, results, headerStyle, passStyle, failStyle);

            // Write the output to a file
            String outputFilePath = Paths.get("Recon_Report.xlsx").toAbsolutePath().toString();
            try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
                workbook.write(fileOut);
            }
            return outputFilePath;
        }
    }

    private void createSummarySheet(Workbook workbook, SummaryStats stats, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Summary");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Metric", "Count"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate Data Rows
        int rowNum = 1;
        createSummaryRow(sheet, rowNum++, "Total Attributes Compared", stats.getTotalAttributes());
        createSummaryRow(sheet, rowNum++, "Passed Attributes", stats.getPassed());
        createSummaryRow(sheet, rowNum++, "Failed Attributes", stats.getFailed());
        createSummaryRow(sheet, rowNum++, "Missing Attributes in Excel2", stats.getMissingAttributes());
        createSummaryRow(sheet, rowNum++, "Report Mismatches", stats.getReportMismatches());
        createSummaryRow(sheet, rowNum++, "Product Mismatches", stats.getProductMismatches());

        // Adjust column widths
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void createDetailedSheet(Workbook workbook, List<ComparisonResult> results, CellStyle headerStyle, CellStyle passStyle, CellStyle failStyle) {
        Sheet sheet = workbook.createSheet("Detailed Report");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Attribute", "Compared Item", "Status", "Reason"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate Data Rows
        int rowNum = 1;
        for (ComparisonResult result : results) {
            Row row = sheet.createRow(rowNum++);
            // Choose style based on the result status
            CellStyle style = (result.getStatus() == ComparisonResult.Status.PASS) ? passStyle : failStyle;

            createCell(row, 0, result.getAttribute(), style);
            createCell(row, 1, result.getReportOrProduct(), style);
            createCell(row, 2, result.getStatus().toString(), style);
            createCell(row, 3, result.getReason(), style);
        }

        // Adjust column widths
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    // --- Helper Methods for Cell Creation and Styling ---

    private void createSummaryRow(Sheet sheet, int rowNum, String metric, int count) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(metric);
        row.createCell(1).setCellValue(count);
    }

    private void createCell(Row row, int colNum, String value, CellStyle style) {
        Cell cell = row.createCell(colNum);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle createColorStyle(Workbook workbook, short colorIndex) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(colorIndex);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
