package com.recon.ui;

import com.recon.config.MappingConfig;
import com.recon.engine.ComparisonEngine;
import com.recon.model.Attribute;
import com.recon.parser.ExcelParser;
import com.recon.report.ExcelReportGenerator;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ReconUI extends JFrame implements PropertyChangeListener {

    private FileChooserPanel fileChooserPanel;
    private MappingPanel mappingPanel;
    private JButton runButton;

    public ReconUI() {
        super("Excel Reconciliation Engine");
        initComponents();
        layoutComponents();
        addListeners();
        setupFrame();
    }

    private void initComponents() {
        fileChooserPanel = new FileChooserPanel();
        mappingPanel = new MappingPanel();
        runButton = new JButton("Run Reconciliation");
    }

    private void layoutComponents() {
        // Create a main panel to hold all components and set it as the content pane
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(fileChooserPanel, BorderLayout.NORTH);
        mainPanel.add(mappingPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(runButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void addListeners() {
        fileChooserPanel.addPropertyChangeListener(this);
        runButton.addActionListener(e -> runReconciliation());
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        String filePath = (String) evt.getNewValue();
        try {
            List<String> headers = (filePath != null && !filePath.isEmpty())
                    ? ExcelParser.getHeaders(filePath)
                    : Collections.emptyList();
            if (FileChooserPanel.EXCEL1_PATH_PROPERTY.equals(propertyName)) {
                mappingPanel.setExcel1Headers(headers);
            } else if (FileChooserPanel.EXCEL2_PATH_PROPERTY.equals(propertyName)) {
                mappingPanel.setExcel2Headers(headers);
            }
        } catch (IOException e) {
            showError("Error reading Excel file: " + e.getMessage(), "File Read Error");
            if (FileChooserPanel.EXCEL1_PATH_PROPERTY.equals(propertyName)) {
                mappingPanel.setExcel1Headers(Collections.emptyList());
            } else if (FileChooserPanel.EXCEL2_PATH_PROPERTY.equals(propertyName)) {
                mappingPanel.setExcel2Headers(Collections.emptyList());
            }
        }
    }

    private void runReconciliation() {
        MappingConfig config = getMappingConfigFromUI();
        if (!validateConfig(config)) {
            return;
        }

        setBusy(true);

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                List<Attribute> excel1Data = ExcelParser.parseDataFromExcel1(config.getExcel1Path(),
                        config.getExcel1AttributeColumn(), config.getExcel1ReportColumns(), config.getExcel1ProductColumns());
                Map<String, Map<String, String>> excel2Data = ExcelParser.parseDataFromExcel2(config.getExcel2Path(),
                        config.getExcel2AttributeNameColumns(), config.getExcel2ReportColumn(), config.getExcel2ProductColumn());

                ComparisonEngine engine = new ComparisonEngine();
                engine.runComparison(excel1Data, excel2Data);

                ExcelReportGenerator reportGenerator = new ExcelReportGenerator();
                return reportGenerator.generateReport(engine.getSummaryStats(), engine.getComparisonResults());
            }

            @Override
            protected void done() {
                setBusy(false);
                try {
                    String reportPath = get();
                    showCompletionDialog(reportPath);
                } catch (InterruptedException | ExecutionException e) {
                    showError("An error occurred during reconciliation: " + e.getCause().getMessage(), "Execution Error");
                }
            }
        };

        worker.execute();
    }

    private MappingConfig getMappingConfigFromUI() {
        MappingConfig config = new MappingConfig();
        config.setExcel1Path(fileChooserPanel.getExcel1Path());
        config.setExcel2Path(fileChooserPanel.getExcel2Path());
        config.setExcel1AttributeColumn(mappingPanel.getExcel1AttributeColumn());
        config.setExcel1ReportColumns(mappingPanel.getExcel1ReportColumns());
        config.setExcel1ProductColumns(mappingPanel.getExcel1ProductColumns());
        config.setExcel2AttributeNameColumns(mappingPanel.getExcel2AttributeNameColumns());
        config.setExcel2ReportColumn(mappingPanel.getExcel2ReportColumn());
        config.setExcel2ProductColumn(mappingPanel.getExcel2ProductColumn());
        return config;
    }

    private boolean validateConfig(MappingConfig config) {
        if (config.getExcel1Path().isEmpty() || config.getExcel2Path().isEmpty()) {
            showError("Please select both Excel files.", "Validation Error");
            return false;
        }
        if (config.getExcel1AttributeColumn() == null) {
            showError("Please select an Attribute column for Excel 1.", "Validation Error");
            return false;
        }
        if (config.getExcel2ReportColumn() == null || config.getExcel2ProductColumn() == null) {
            showError("Please select Report and Product columns for Excel 2.", "Validation Error");
            return false;
        }
        return true;
    }

    private void setBusy(boolean busy) {
        runButton.setEnabled(!busy);
        setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
    }

    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void showCompletionDialog(String reportPath) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.add(new JLabel("Reconciliation complete. Report generated at:"), BorderLayout.NORTH);
        JTextField pathField = new JTextField(reportPath);
        pathField.setEditable(false);
        panel.add(pathField, BorderLayout.CENTER);

        Object[] options = {"Open Report", "Close"};
        int choice = JOptionPane.showOptionDialog(this, panel, "Success",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) { // "Open Report" was clicked
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(reportPath));
                } else {
                    showError("Desktop operations not supported on this system.", "Compatibility Error");
                }
            } catch (IOException ex) {
                showError("Could not open the report file: " + ex.getMessage(), "File Open Error");
            }
        }
    }
}
