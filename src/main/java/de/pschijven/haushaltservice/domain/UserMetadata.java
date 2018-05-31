package de.pschijven.haushaltservice.domain;

import java.math.BigDecimal;

public class UserMetadata {
    private String name;
    private BigDecimal salary;

    public UserMetadata(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }
}
