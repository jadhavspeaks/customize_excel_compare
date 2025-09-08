package com.recon;

import com.recon.engine.ComparisonEngine;
import com.recon.model.Attribute;
import com.recon.model.ComparisonResult;
import com.recon.model.SummaryStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonEngineTest {

    private ComparisonEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ComparisonEngine();
    }

    @Test
    void testSuccessfulMatch() {
        // Arrange
        Attribute attr1 = new Attribute("Attr1", Arrays.asList("Report A"), Arrays.asList("Product X"));
        List<Attribute> excel1Data = Collections.singletonList(attr1);

        Map<String, String> attr2Values = new HashMap<>();
        attr2Values.put("Report", "Report A");
        attr2Values.put("Product", "Product X");
        Map<String, Map<String, String>> excel2Data = new HashMap<>();
        excel2Data.put("Attr1", attr2Values);

        // Act
        engine.runComparison(excel1Data, excel2Data);
        SummaryStats stats = engine.getSummaryStats();
        List<ComparisonResult> results = engine.getComparisonResults();

        // Assert
        assertEquals(1, stats.getTotalAttributes());
        assertEquals(1, stats.getPassed());
        assertEquals(0, stats.getFailed());
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(r -> r.getStatus() == ComparisonResult.Status.PASS));
    }

    @Test
    void testProductMismatch() {
        // Arrange
        Attribute attr1 = new Attribute("Attr1", Arrays.asList("Report A"), Arrays.asList("Product X"));
        List<Attribute> excel1Data = Collections.singletonList(attr1);

        Map<String, String> attr2Values = new HashMap<>();
        attr2Values.put("Report", "Report A");
        attr2Values.put("Product", "Product Z"); // Mismatch
        Map<String, Map<String, String>> excel2Data = new HashMap<>();
        excel2Data.put("Attr1", attr2Values);

        // Act
        engine.runComparison(excel1Data, excel2Data);
        SummaryStats stats = engine.getSummaryStats();
        List<ComparisonResult> results = engine.getComparisonResults();

        // Assert
        assertEquals(1, stats.getFailed());
        assertEquals(0, stats.getPassed());
        assertEquals(1, stats.getProductMismatches());
        assertEquals(0, stats.getReportMismatches());
        assertEquals(ComparisonResult.Status.PASS, results.get(0).getStatus()); // Report should pass
        assertEquals(ComparisonResult.Status.FAIL, results.get(1).getStatus()); // Product should fail
    }

    @Test
    void testAttributeMissingInExcel2() {
        // Arrange
        Attribute attr1 = new Attribute("Attr1", Collections.emptyList(), Collections.emptyList());
        List<Attribute> excel1Data = Collections.singletonList(attr1);
        Map<String, Map<String, String>> excel2Data = Collections.emptyMap(); // Attr1 is missing

        // Act
        engine.runComparison(excel1Data, excel2Data);
        SummaryStats stats = engine.getSummaryStats();
        List<ComparisonResult> results = engine.getComparisonResults();

        // Assert
        assertEquals(1, stats.getFailed());
        assertEquals(1, stats.getMissingAttributes());
        assertEquals(1, results.size());
        assertEquals("Attribute missing in Excel2", results.get(0).getReason());
    }

    @Test
    void testCaseInsensitiveMatch() {
        // Arrange
        Attribute attr1 = new Attribute("attr1", Arrays.asList("report a"), Arrays.asList("product x"));
        List<Attribute> excel1Data = Collections.singletonList(attr1);

        Map<String, String> attr2Values = new HashMap<>();
        attr2Values.put("Report", "Report A");
        attr2Values.put("Product", "Product X");
        Map<String, Map<String, String>> excel2Data = new HashMap<>();
        excel2Data.put("Attr1", attr2Values); // Different case for all keys and values

        // Act
        engine.runComparison(excel1Data, excel2Data);
        SummaryStats stats = engine.getSummaryStats();

        // Assert
        assertEquals(1, stats.getPassed());
        assertEquals(0, stats.getFailed());
    }
}
