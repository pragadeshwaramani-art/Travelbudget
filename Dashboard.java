package com.travelbudget.ui;

import com.travelbudget.dao.ExpenseDAO;
import com.travelbudget.dao.TripDAO;
import com.travelbudget.model.Expense;
import com.travelbudget.model.Trip;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Dashboard extends JFrame {
    private final TripDAO tripDAO = new TripDAO();
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    private final DefaultTableModel tripModel = new DefaultTableModel(
            new String[]{"ID", "Trip", "Destination", "Start Date", "End Date", "Budget", "Spent", "Remaining"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final DefaultTableModel expenseModel = new DefaultTableModel(
            new String[]{"Expense ID", "Trip", "Trip ID", "Category", "Amount", "Date", "Description"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final JTable tripTable = new JTable(tripModel);
    private final JTable expenseTable = new JTable(expenseModel);
    private final JLabel status = new JLabel("Ready");

    public Dashboard() {
        setTitle("Travel Budget and Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 620));
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JLabel heading = new JLabel("Travel Budget and Expense Tracker");
        heading.setFont(new Font("SansSerif", Font.BOLD, 23));
        heading.setBorder(BorderFactory.createEmptyBorder(16, 18, 10, 18));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Trips", buildTripsPanel());
        tabs.addTab("Expenses", buildExpensesPanel());

        status.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        add(heading, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        refreshAll();
    }

    private JPanel buildTripsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add Trip");
        JButton refresh = new JButton("Refresh");
        add.addActionListener(e -> addTrip());
        refresh.addActionListener(e -> refreshAll());
        buttons.add(add);
        buttons.add(refresh);

        tripTable.setRowHeight(25);
        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(tripTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildExpensesPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add Expense");
        JButton refresh = new JButton("Refresh");
        add.addActionListener(e -> addExpense());
        refresh.addActionListener(e -> refreshAll());
        buttons.add(add);
        buttons.add(refresh);

        expenseTable.setRowHeight(25);
        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);
        return panel;
    }

    private void addTrip() {
        JTextField name = new JTextField();
        JTextField destination = new JTextField();
        JTextField start = new JTextField(LocalDate.now().toString());
        JTextField end = new JTextField(LocalDate.now().toString());
        JTextField budget = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Trip name:")); form.add(name);
        form.add(new JLabel("Destination:")); form.add(destination);
        form.add(new JLabel("Start date (YYYY-MM-DD):")); form.add(start);
        form.add(new JLabel("End date (YYYY-MM-DD):")); form.add(end);
        form.add(new JLabel("Budget:")); form.add(budget);

        if (JOptionPane.showConfirmDialog(this, form, "Add Trip",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        try {
            String tripName = required(name.getText(), "Trip name");
            String place = required(destination.getText(), "Destination");
            LocalDate startDate = LocalDate.parse(start.getText().trim());
            LocalDate endDate = LocalDate.parse(end.getText().trim());
            BigDecimal amount = new BigDecimal(budget.getText().trim());
            if (endDate.isBefore(startDate)) throw new IllegalArgumentException("End date cannot be before start date.");
            if (amount.signum() < 0) throw new IllegalArgumentException("Budget cannot be negative.");

            tripDAO.add(new Trip(0, tripName, place, startDate, endDate, amount));
            status.setText("Trip added successfully.");
            refreshAll();
        } catch (DateTimeParseException ex) {
            showError("Enter dates in YYYY-MM-DD format.");
        } catch (NumberFormatException ex) {
            showError("Enter a valid number for the budget.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void addExpense() {
        List<Trip> trips;
        try {
            trips = tripDAO.findAll();
        } catch (Exception ex) {
            showError(ex.getMessage());
            return;
        }
        if (trips.isEmpty()) {
            showError("Add a trip before adding an expense.");
            return;
        }

        JComboBox<Trip> tripBox = new JComboBox<>(trips.toArray(new Trip[0]));
        JComboBox<String> category = new JComboBox<>(new String[]{
                "Transport", "Food", "Accommodation", "Shopping", "Activities", "Other"});
        JTextField amount = new JTextField();
        JTextField date = new JTextField(LocalDate.now().toString());
        JTextField description = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Trip:")); form.add(tripBox);
        form.add(new JLabel("Category:")); form.add(category);
        form.add(new JLabel("Amount:")); form.add(amount);
        form.add(new JLabel("Date (YYYY-MM-DD):")); form.add(date);
        form.add(new JLabel("Description:")); form.add(description);

        if (JOptionPane.showConfirmDialog(this, form, "Add Expense",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        try {
            Trip selected = (Trip) tripBox.getSelectedItem();
            if (selected == null) throw new IllegalArgumentException("Select a trip.");
            BigDecimal value = new BigDecimal(amount.getText().trim());
            if (value.signum() <= 0) throw new IllegalArgumentException("Expense amount must be greater than zero.");
            LocalDate expenseDate = LocalDate.parse(date.getText().trim());

            expenseDAO.add(new Expense(0, selected.getId(), (String) category.getSelectedItem(),
                    value, expenseDate, description.getText().trim()));
            status.setText("Expense added successfully.");
            refreshAll();
        } catch (DateTimeParseException ex) {
            showError("Enter the date in YYYY-MM-DD format.");
        } catch (NumberFormatException ex) {
            showError("Enter a valid number for the amount.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void refreshAll() {
        try {
            tripModel.setRowCount(0);
            for (Trip trip : tripDAO.findAll()) {
                BigDecimal spent = expenseDAO.totalForTrip(trip.getId());
                BigDecimal remaining = trip.getBudget().subtract(spent);
                tripModel.addRow(new Object[]{trip.getId(), trip.getName(), trip.getDestination(),
                        trip.getStartDate(), trip.getEndDate(), trip.getBudget(), spent, remaining});
            }

            expenseModel.setRowCount(0);
            for (Object[] row : expenseDAO.findAllWithTripName()) expenseModel.addRow(row);
            status.setText("Data refreshed.");
        } catch (Exception ex) {
            status.setText("Could not load data.");
            showError("Could not connect to MySQL. Check DBConnection.java and make sure MySQL is running.\n\n" + ex.getMessage());
        }
    }

    private String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }
        return value.trim();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message == null ? "An unexpected error occurred." : message,
                "Travel Tracker", JOptionPane.ERROR_MESSAGE);
    }
}
