package de.pschijven.haushaltservice.domain;

public class TransactionFormBean {

    private Long amount;
    private String description;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
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
