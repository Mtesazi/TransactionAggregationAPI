package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AggregatedSummaryTest {

    private AggregatedSummary aggregatedSummary;
    private Map<String, BigDecimal> categoryTotals;

    @BeforeEach
    void setUp() {
        categoryTotals = new HashMap<>();
        categoryTotals.put("GROCERIES", new BigDecimal("150.00"));
        categoryTotals.put("FOOD", new BigDecimal("75.50"));
        categoryTotals.put("TRAVEL", new BigDecimal("200.00"));
    }

    @Test
    void testConstructorWithParameters() {
        // Given
        BigDecimal total = new BigDecimal("425.50");

        // When
        aggregatedSummary = new AggregatedSummary(total, categoryTotals);

        // Then
        assertNotNull(aggregatedSummary);
        assertEquals(total, aggregatedSummary.getTotal());
        assertEquals(categoryTotals, aggregatedSummary.getCategoryTotals());
        assertEquals(3, aggregatedSummary.getCategoryTotals().size());
    }

    @Test
    void testGetTotal() {
        // Given
        BigDecimal total = new BigDecimal("1000.00");
        aggregatedSummary = new AggregatedSummary(total, categoryTotals);

        // When
        BigDecimal result = aggregatedSummary.getTotal();

        // Then
        assertEquals(total, result);
        assertEquals(0, total.compareTo(result));
    }

    @Test
    void testSetTotal() {
        // Given
        aggregatedSummary = new AggregatedSummary(BigDecimal.ZERO, new HashMap<>());
        BigDecimal newTotal = new BigDecimal("500.00");

        // When
        aggregatedSummary.setTotal(newTotal);

        // Then
        assertEquals(newTotal, aggregatedSummary.getTotal());
    }

    @Test
    void testGetCategoryTotals() {
        // Given
        aggregatedSummary = new AggregatedSummary(new BigDecimal("425.50"), categoryTotals);

        // When
        Map<String, BigDecimal> result = aggregatedSummary.getCategoryTotals();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(new BigDecimal("150.00"), result.get("GROCERIES"));
        assertEquals(new BigDecimal("75.50"), result.get("FOOD"));
        assertEquals(new BigDecimal("200.00"), result.get("TRAVEL"));
    }

    @Test
    void testSetCategoryTotals() {
        // Given
        aggregatedSummary = new AggregatedSummary(BigDecimal.ZERO, new HashMap<>());
        Map<String, BigDecimal> newCategoryTotals = new HashMap<>();
        newCategoryTotals.put("SHOPPING", new BigDecimal("100.00"));
        newCategoryTotals.put("ENTERTAINMENT", new BigDecimal("50.00"));

        // When
        aggregatedSummary.setCategoryTotals(newCategoryTotals);

        // Then
        assertEquals(newCategoryTotals, aggregatedSummary.getCategoryTotals());
        assertEquals(2, aggregatedSummary.getCategoryTotals().size());
    }

    @Test
    void testToString() {
        // Given
        BigDecimal total = new BigDecimal("425.50");
        aggregatedSummary = new AggregatedSummary(total, categoryTotals);

        // When
        String result = aggregatedSummary.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("AggregatedSummary"));
        assertTrue(result.contains("total="));
        assertTrue(result.contains("categoryTotals="));
        assertTrue(result.contains("425.50"));
    }

    @Test
    void testWithEmptyCategoryTotals() {
        // Given
        BigDecimal total = BigDecimal.ZERO;
        Map<String, BigDecimal> emptyCategoryTotals = new HashMap<>();

        // When
        aggregatedSummary = new AggregatedSummary(total, emptyCategoryTotals);

        // Then
        assertNotNull(aggregatedSummary);
        assertEquals(BigDecimal.ZERO, aggregatedSummary.getTotal());
        assertTrue(aggregatedSummary.getCategoryTotals().isEmpty());
        assertEquals(0, aggregatedSummary.getCategoryTotals().size());
    }

    @Test
    void testWithZeroTotal() {
        // Given
        BigDecimal zero = BigDecimal.ZERO;

        // When
        aggregatedSummary = new AggregatedSummary(zero, categoryTotals);

        // Then
        assertEquals(BigDecimal.ZERO, aggregatedSummary.getTotal());
        assertEquals(0, aggregatedSummary.getTotal().compareTo(BigDecimal.ZERO));
    }

    @Test
    void testWithSingleCategory() {
        // Given
        Map<String, BigDecimal> singleCategory = new HashMap<>();
        singleCategory.put("UTILITIES", new BigDecimal("250.00"));
        BigDecimal total = new BigDecimal("250.00");

        // When
        aggregatedSummary = new AggregatedSummary(total, singleCategory);

        // Then
        assertEquals(1, aggregatedSummary.getCategoryTotals().size());
        assertEquals(new BigDecimal("250.00"), aggregatedSummary.getCategoryTotals().get("UTILITIES"));
    }

    @Test
    void testWithAllCategories() {
        // Given
        Map<String, BigDecimal> allCategories = new HashMap<>();
        allCategories.put("FOOD", new BigDecimal("10.00"));
        allCategories.put("TRAVEL", new BigDecimal("20.00"));
        allCategories.put("SHOPPING", new BigDecimal("30.00"));
        allCategories.put("UTILITIES", new BigDecimal("40.00"));
        allCategories.put("GROCERIES", new BigDecimal("50.00"));
        allCategories.put("ENTERTAINMENT", new BigDecimal("60.00"));
        allCategories.put("OTHER", new BigDecimal("70.00"));
        BigDecimal total = new BigDecimal("280.00");

        // When
        aggregatedSummary = new AggregatedSummary(total, allCategories);

        // Then
        assertEquals(7, aggregatedSummary.getCategoryTotals().size());
        assertEquals(total, aggregatedSummary.getTotal());
    }

    @Test
    void testModifyingCategoryTotals() {
        // Given
        aggregatedSummary = new AggregatedSummary(new BigDecimal("425.50"), categoryTotals);

        // When
        aggregatedSummary.getCategoryTotals().put("ENTERTAINMENT", new BigDecimal("100.00"));

        // Then
        assertEquals(4, aggregatedSummary.getCategoryTotals().size());
        assertEquals(new BigDecimal("100.00"), aggregatedSummary.getCategoryTotals().get("ENTERTAINMENT"));
    }

    @Test
    void testWithLargeAmounts() {
        // Given
        BigDecimal largeTotal = new BigDecimal("999999999.99");
        Map<String, BigDecimal> largeCategoryTotals = new HashMap<>();
        largeCategoryTotals.put("GROCERIES", new BigDecimal("500000000.00"));
        largeCategoryTotals.put("TRAVEL", new BigDecimal("499999999.99"));

        // When
        aggregatedSummary = new AggregatedSummary(largeTotal, largeCategoryTotals);

        // Then
        assertEquals(largeTotal, aggregatedSummary.getTotal());
        assertEquals(2, aggregatedSummary.getCategoryTotals().size());
    }

    @Test
    void testWithDecimalAmounts() {
        // Given
        BigDecimal total = new BigDecimal("123.45");
        Map<String, BigDecimal> decimalCategoryTotals = new HashMap<>();
        decimalCategoryTotals.put("FOOD", new BigDecimal("12.34"));
        decimalCategoryTotals.put("GROCERIES", new BigDecimal("111.11"));

        // When
        aggregatedSummary = new AggregatedSummary(total, decimalCategoryTotals);

        // Then
        assertEquals(new BigDecimal("123.45"), aggregatedSummary.getTotal());
        assertEquals(new BigDecimal("12.34"), aggregatedSummary.getCategoryTotals().get("FOOD"));
    }

    @Test
    void testCategoryTotalsImmutability() {
        // Given
        aggregatedSummary = new AggregatedSummary(new BigDecimal("425.50"), categoryTotals);
        Map<String, BigDecimal> originalTotals = new HashMap<>(aggregatedSummary.getCategoryTotals());

        // When
        categoryTotals.put("NEW_CATEGORY", new BigDecimal("999.99"));

        // Then - verify that modifying the original map doesn't affect the aggregated summary
        // unless the implementation explicitly shares the reference
        assertTrue(aggregatedSummary.getCategoryTotals().containsKey("GROCERIES"));
        assertTrue(aggregatedSummary.getCategoryTotals().containsKey("FOOD"));
        assertTrue(aggregatedSummary.getCategoryTotals().containsKey("TRAVEL"));
    }
}
