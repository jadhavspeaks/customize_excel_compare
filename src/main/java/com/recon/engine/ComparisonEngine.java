package com.recon.engine;

import com.recon.model.Attribute;
import com.recon.model.ComparisonResult;
import com.recon.model.SummaryStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ComparisonEngine {

    private final List<ComparisonResult> results = new ArrayList<>();
    private final SummaryStats stats = new SummaryStats();

    public void runComparison(List<Attribute> excel1Data, Map<String, Map<String, String>> excel2Data) {
        if (excel1Data == null || excel2Data == null) {
            return;
        }
        stats.setTotalAttributes(excel1Data.size());

        for (Attribute attr1 : excel1Data) {
            String attr1Name = attr1.getName();
            // Find the corresponding attribute in Excel 2 data, ignoring case.
            Optional<String> matchingKey = findCaseInsensitiveKey(excel2Data, attr1Name);

            if (!matchingKey.isPresent()) {
                // Attribute from Excel 1 does not exist in Excel 2
                results.add(new ComparisonResult(attr1Name, "N/A", ComparisonResult.Status.FAIL, "Attribute missing in Excel2"));
                stats.incrementMissingAttributes();
                stats.incrementFailed();
                continue;
            }

            // Attribute found, now compare reports and products
            Map<String, String> attr2Data = excel2Data.get(matchingKey.get());
            String report2 = attr2Data.get("Report");
            String product2 = attr2Data.get("Product");

            boolean attributeOverallPassed = true;

            // 1. Compare Reports
            // The check passes if any of the selected reports in Excel 1 match the report in Excel 2.
            boolean reportMatch = attr1.getReports().stream().anyMatch(r1 -> r1.equalsIgnoreCase(report2));
            if (reportMatch) {
                results.add(new ComparisonResult(attr1Name, "Report: " + report2, ComparisonResult.Status.PASS, ""));
            } else {
                results.add(new ComparisonResult(attr1Name, "Report", ComparisonResult.Status.FAIL, "Mismatch. E1 Reports: " + attr1.getReports() + ", E2 Report: " + report2));
                stats.incrementReportMismatches();
                attributeOverallPassed = false;
            }

            // 2. Compare Products
            // The check passes if any of the selected products in Excel 1 match the product in Excel 2.
            boolean productMatch = attr1.getProducts().stream().anyMatch(p1 -> p1.equalsIgnoreCase(product2));
            if (productMatch) {
                results.add(new ComparisonResult(attr1Name, "Product: " + product2, ComparisonResult.Status.PASS, ""));
            } else {
                results.add(new ComparisonResult(attr1Name, "Product", ComparisonResult.Status.FAIL, "Mismatch. E1 Products: " + attr1.getProducts() + ", E2 Product: " + product2));
                stats.incrementProductMismatches();
                attributeOverallPassed = false;
            }

            // Update summary stats for the attribute
            if (attributeOverallPassed) {
                stats.incrementPassed();
            } else {
                stats.incrementFailed();
            }
        }
    }

    /**
     * Finds a key in a map in a case-insensitive manner.
     * @param map The map to search in.
     * @param key The key to find.
     * @return An Optional containing the actual key from the map, or empty if not found.
     */
    private Optional<String> findCaseInsensitiveKey(Map<String, ?> map, String key) {
        return map.keySet().stream()
                .filter(k -> k.equalsIgnoreCase(key))
                .findFirst();
    }

    public List<ComparisonResult> getComparisonResults() {
        return results;
    }

    public SummaryStats getSummaryStats() {
        return stats;
    }
}
