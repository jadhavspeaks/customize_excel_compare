package com.recon.parser;

import com.recon.model.Attribute;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelParser {

    private static final DataFormatter dataFormatter = new DataFormatter();

    /**
     * Reads the header row (first row) of the first sheet in an Excel file.
     *
     * @param filePath The path to the Excel file.
     * @return A list of header strings.
     * @throws IOException If an I/O error occurs.
     */
    public static List<String> getHeaders(String filePath) throws IOException {
        List<String> headers = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return headers;

            Row headerRow = sheet.getRow(0);
            if (headerRow != null) {
                for (Cell cell : headerRow) {
                    headers.add(getCellValueAsString(cell));
                }
            }
        }
        return headers;
    }

    /**
     * Parses data from Excel 1 format.
     *
     * @param filePath       Path to the Excel file.
     * @param attributeCol   Name of the attribute column.
     * @param reportCols     List of names of report columns.
     * @param productCols    List of names of product columns.
     * @return A list of Attribute objects.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Attribute> parseDataFromExcel1(String filePath, String attributeCol, List<String> reportCols, List<String> productCols) throws IOException {
        List<Attribute> attributes = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> headerMap = getColumnIndexMap(sheet.getRow(0));

            Integer attrIndex = headerMap.get(attributeCol);
            if (attrIndex == null) throw new IOException("Attribute column '" + attributeCol + "' not found.");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String attrName = getCellValueAsString(row.getCell(attrIndex));
                if (attrName.isEmpty()) continue;

                List<String> selectedReports = new ArrayList<>();
                for (String reportColName : reportCols) {
                    Integer colIndex = headerMap.get(reportColName);
                    if (colIndex != null && !getCellValueAsString(row.getCell(colIndex)).isEmpty()) {
                        selectedReports.add(reportColName);
                    }
                }

                List<String> selectedProducts = new ArrayList<>();
                for (String productColName : productCols) {
                    Integer colIndex = headerMap.get(productColName);
                    if (colIndex != null && !getCellValueAsString(row.getCell(colIndex)).isEmpty()) {
                        selectedProducts.add(productColName);
                    }
                }
                attributes.add(new Attribute(attrName, selectedReports, selectedProducts));
            }
        }
        return attributes;
    }

    /**
     * Parses data from Excel 2 format.
     *
     * @param filePath        Path to the Excel file.
     * @param attributeCols   List of names of attribute name columns.
     * @param reportCol       Name of the report column.
     * @param productCol      Name of the product column.
     * @return A map where key is attribute name and value is another map containing Report and Product.
     * @throws IOException If an I/O error occurs.
     */
    public static Map<String, Map<String, String>> parseDataFromExcel2(String filePath, List<String> attributeCols, String reportCol, String productCol) throws IOException {
        Map<String, Map<String, String>> dataMap = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> headerMap = getColumnIndexMap(sheet.getRow(0));

            Integer reportIndex = headerMap.get(reportCol);
            Integer productIndex = headerMap.get(productCol);
            if (reportIndex == null) throw new IOException("Report column '" + reportCol + "' not found.");
            if (productIndex == null) throw new IOException("Product column '" + productCol + "' not found.");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String reportValue = getCellValueAsString(row.getCell(reportIndex));
                String productValue = getCellValueAsString(row.getCell(productIndex));

                for (String attrColName : attributeCols) {
                    Integer attrIndex = headerMap.get(attrColName);
                    if (attrIndex != null) {
                        String attrName = getCellValueAsString(row.getCell(attrIndex));
                        if (attrName != null && !attrName.isEmpty()) {
                            Map<String, String> values = new HashMap<>();
                            values.put("Report", reportValue);
                            values.put("Product", productValue);
                            dataMap.put(attrName, values);
                        }
                    }
                }
            }
        }
        return dataMap;
    }

    /**
     * Helper method to create a map of header names to their column indices.
     */
    private static Map<String, Integer> getColumnIndexMap(Row headerRow) {
        Map<String, Integer> headerMap = new HashMap<>();
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                headerMap.put(getCellValueAsString(cell), cell.getColumnIndex());
            }
        }
        return headerMap;
    }

    /**
     * Helper method to get cell value as a string, handling different cell types.
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return dataFormatter.formatCellValue(cell).trim();
    }
}
