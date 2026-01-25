package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;


@Setter
@Getter
public class AggregatedSummary {

    // Setters
    // Getters
    private BigDecimal total;
    private Map<String, BigDecimal> categoryTotals;

    // All-args constructor
    public AggregatedSummary(BigDecimal total, Map<String, BigDecimal> categoryTotals) {
        this.total = total;
        this.categoryTotals = categoryTotals;
    }

    @Override
    public String toString() {
        return "AggregatedSummary{" + "total=" + total + ", categoryTotals=" + categoryTotals +'}';
    }
}
