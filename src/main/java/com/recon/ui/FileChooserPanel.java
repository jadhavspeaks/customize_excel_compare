package com.recon.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class FileChooserPanel extends JPanel {

    public static final String EXCEL1_PATH_PROPERTY = "excel1Path";
    public static final String EXCEL2_PATH_PROPERTY = "excel2Path";

    private JTextField excel1PathField;
    private JTextField excel2PathField;

    public FileChooserPanel() {
        initComponents();
    }

    private void initComponents() {
        setBorder(BorderFactory.createTitledBorder("File Selection"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Excel 1 Components ---
        JLabel excel1Label = new JLabel("Excel File 1:");
        excel1PathField = new JTextField(40);
        excel1PathField.setEditable(false);
        JButton browse1Button = new JButton("Browse...");

        // --- Excel 2 Components ---
        JLabel excel2Label = new JLabel("Excel File 2:");
        excel2PathField = new JTextField(40);
        excel2PathField.setEditable(false);
        JButton browse2Button = new JButton("Browse...");

        // --- Layout ---
        // Row 1: Excel 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(excel1Label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(excel1PathField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        add(browse1Button, gbc);

        // Row 2: Excel 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(excel2Label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(excel2PathField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        add(browse2Button, gbc);

        // --- Listeners ---
        browse1Button.addActionListener(e -> chooseFile(excel1PathField, EXCEL1_PATH_PROPERTY));
        browse2Button.addActionListener(e -> chooseFile(excel2PathField, EXCEL2_PATH_PROPERTY));
    }

    private void chooseFile(JTextField pathField, String propertyName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx, *.xls)", "xlsx", "xls"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String oldPath = pathField.getText();
            String newPath = selectedFile.getAbsolutePath();
            pathField.setText(newPath);
            firePropertyChange(propertyName, oldPath, newPath);
        }
    }

    public String getExcel1Path() {
        return excel1PathField.getText();
    }

    public String getExcel2Path() {
        return excel2PathField.getText();
    }
}
