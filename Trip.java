package com.travelbudget.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Trip {
    private final int id;
    private final String name;
    private final String destination;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal budget;

    public Trip(int id, String name, String destination, LocalDate startDate,
                LocalDate endDate, BigDecimal budget) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDestination() { return destination; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getBudget() { return budget; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + destination + ")";
    }
}
