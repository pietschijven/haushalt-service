package de.pschijven.haushaltservice.domain;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransactionFormBean {

    @NotNull
    @Digits(integer = 20, fraction = 2)
    private BigDecimal amount;

    private String description;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TransactionFormBean{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}
