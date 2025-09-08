package com.recon;

import com.recon.ui.ReconUI;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Excel Reconciliation Engine.
 */
public class App {
    public static void main(String[] args) {
        // It's best practice to initialize and show Swing GUIs on the
        // Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(() -> new ReconUI());
    }
}
