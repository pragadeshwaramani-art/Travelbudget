package com.travelbudget.dao;

import com.travelbudget.DBConnection;
import com.travelbudget.model.Expense;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    public void add(Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses (trip_id, category, amount, expense_date, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, expense.getTripId());
            p.setString(2, expense.getCategory());
            p.setBigDecimal(3, expense.getAmount());
            p.setDate(4, Date.valueOf(expense.getDate()));
            p.setString(5, expense.getDescription());
            p.executeUpdate();
        }
    }

    public List<Object[]> findAllWithTripName() throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT e.expense_id, t.trip_name, e.trip_id, e.category, e.amount, e.expense_date, e.description " +
                     "FROM expenses e JOIN trips t ON e.trip_id = t.trip_id ORDER BY e.expense_id DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                rows.add(new Object[] {
                    r.getInt("expense_id"), r.getString("trip_name"), r.getInt("trip_id"),
                    r.getString("category"), r.getBigDecimal("amount"),
                    r.getDate("expense_date"), r.getString("description")
                });
            }
        }
        return rows;
    }

    public BigDecimal totalForTrip(int tripId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM expenses WHERE trip_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, tripId);
            try (ResultSet r = p.executeQuery()) {
                r.next();
                return r.getBigDecimal("total");
            }
        }
    }
}
