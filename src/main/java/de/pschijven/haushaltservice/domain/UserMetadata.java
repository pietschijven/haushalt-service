package de.pschijven.haushaltservice.domain;

import java.math.BigDecimal;

public class UserMetadata {
    private String name;
    private BigDecimal salary;

    public UserMetadata(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }
}
