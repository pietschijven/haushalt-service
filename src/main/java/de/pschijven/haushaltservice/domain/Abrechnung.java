package de.pschijven.haushaltservice.domain;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.stream.Collectors.reducing;

public class Abrechnung {
    private Map<String, BigDecimal> subtotals;
    private Map<String, BigDecimal> amountToPay;

    public Abrechnung(Map<String, BigDecimal> subtotals, Map<String, BigDecimal> amountToPay) {
        this.subtotals = subtotals;
        this.amountToPay = amountToPay;
    }

    public Map<String, BigDecimal> getSubtotals() {
        return subtotals;
    }

    public Map<String, BigDecimal> getAmountToPay() {
        return amountToPay;
    }

    public BigDecimal getTotal() {
        return subtotals.values().stream()
                .collect(reducing(BigDecimal.ZERO, BigDecimal::add));
    }
}
