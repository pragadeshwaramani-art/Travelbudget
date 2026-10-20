package com.travelbudget;

import com.travelbudget.ui.Dashboard;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use the default Swing look and feel if the system style is unavailable.
            }
            new Dashboard().setVisible(true);
        });
    }
}
