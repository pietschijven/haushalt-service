package de.pschijven.haushaltservice.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.reducing;

public class Abrechnung {
    private Map<String, BigDecimal> subtotals;
    private Map<String, BigDecimal> amountToPay;
    private List<UserMetadata> metadata;

    public Abrechnung(Map<String, BigDecimal> subtotals,
                      Map<String, BigDecimal> amountToPay,
                      List<UserMetadata> metadata) {
        this.subtotals = subtotals;
        this.amountToPay = amountToPay;
        this.metadata = metadata;
    }

    public List<UserMetadata> getMetadata() {
        return metadata;
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
