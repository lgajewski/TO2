package pl.edu.agh.iet.to2.employees.model;

import java.math.BigDecimal;

public class Occupation {

    private String name;
    private BigDecimal baseSalary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }
}
