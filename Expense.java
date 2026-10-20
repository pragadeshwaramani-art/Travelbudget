package com.travelbudget.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private final int id;
    private final int tripId;
    private final String category;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String description;

    public Expense(int id, int tripId, String category, BigDecimal amount,
                   LocalDate date, String description) {
        this.id = id;
        this.tripId = tripId;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public int getId() { return id; }
    public int getTripId() { return tripId; }
    public String getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
}
