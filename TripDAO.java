package com.travelbudget.dao;

import com.travelbudget.DBConnection;
import com.travelbudget.model.Trip;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {
    public void add(Trip trip) throws SQLException {
        String sql = "INSERT INTO trips (trip_name, destination, start_date, end_date, budget) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, trip.getName());
            p.setString(2, trip.getDestination());
            p.setDate(3, Date.valueOf(trip.getStartDate()));
            p.setDate(4, Date.valueOf(trip.getEndDate()));
            p.setBigDecimal(5, trip.getBudget());
            p.executeUpdate();
        }
    }

    public List<Trip> findAll() throws SQLException {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT trip_id, trip_name, destination, start_date, end_date, budget FROM trips ORDER BY trip_id DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                trips.add(new Trip(r.getInt("trip_id"), r.getString("trip_name"),
                        r.getString("destination"), r.getDate("start_date").toLocalDate(),
                        r.getDate("end_date").toLocalDate(), r.getBigDecimal("budget")));
            }
        }
        return trips;
    }
}
