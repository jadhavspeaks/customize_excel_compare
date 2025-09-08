package com.recon.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class MappingPanel extends JPanel {

    // --- Excel 1 Components ---
    private JComboBox<String> excel1AttributeDropdown;
    private JList<String> excel1ReportList;
    private JList<String> excel1ProductList;

    // --- Excel 2 Components ---
    private JList<String> excel2AttributeList;
    private JComboBox<String> excel2ReportDropdown;
    private JComboBox<String> excel2ProductDropdown;

    // --- Models ---
    private DefaultComboBoxModel<String> excel1AttrModel;
    private DefaultListModel<String> excel1ReportModel;
    private DefaultListModel<String> excel1ProductModel;

    private DefaultListModel<String> excel2AttrModel;
    private DefaultComboBoxModel<String> excel2ReportModel;
    private DefaultComboBoxModel<String> excel2ProductModel;


    public MappingPanel() {
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        // Models
        excel1AttrModel = new DefaultComboBoxModel<>();
        excel1ReportModel = new DefaultListModel<>();
        excel1ProductModel = new DefaultListModel<>();
        excel2AttrModel = new DefaultListModel<>();
        excel2ReportModel = new DefaultComboBoxModel<>();
        excel2ProductModel = new DefaultComboBoxModel<>();

        // Excel 1
        excel1AttributeDropdown = new JComboBox<>(excel1AttrModel);
        excel1ReportList = new JList<>(excel1ReportModel);
        excel1ProductList = new JList<>(excel1ProductModel);
        excel1ReportList.setVisibleRowCount(5);
        excel1ProductList.setVisibleRowCount(5);


        // Excel 2
        excel2AttributeList = new JList<>(excel2AttrModel);
        excel2ReportDropdown = new JComboBox<>(excel2ReportModel);
        excel2ProductDropdown = new JComboBox<>(excel2ProductModel);
        excel2AttributeList.setVisibleRowCount(5);
    }

    private void layoutComponents() {
        setBorder(BorderFactory.createTitledBorder("Column Mapping"));
        setLayout(new GridLayout(1, 2, 10, 0)); // 1 row, 2 columns for the two panels

        // --- Excel 1 Panel ---
        JPanel excel1Panel = new JPanel(new GridBagLayout());
        excel1Panel.setBorder(BorderFactory.createTitledBorder("From Excel 1"));
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(5, 5, 5, 5);
        gbc1.fill = GridBagConstraints.HORIZONTAL;
        gbc1.anchor = GridBagConstraints.NORTHWEST;

        gbc1.gridx = 0; gbc1.gridy = 0; gbc1.gridwidth=2;
        excel1Panel.add(new JLabel("Attribute Column:"), gbc1);
        gbc1.gridy++;
        excel1Panel.add(excel1AttributeDropdown, gbc1);

        gbc1.gridy++;
        excel1Panel.add(new JLabel("Report Columns:"), gbc1);
        gbc1.gridy++;
        gbc1.weightx = 1.0; gbc1.weighty = 1.0; gbc1.fill = GridBagConstraints.BOTH;
        excel1Panel.add(new JScrollPane(excel1ReportList), gbc1);

        gbc1.gridy++;
        gbc1.weighty = 0; gbc1.fill = GridBagConstraints.HORIZONTAL;
        excel1Panel.add(new JLabel("Product Columns:"), gbc1);
        gbc1.gridy++;
        gbc1.weightx = 1.0; gbc1.weighty = 1.0; gbc1.fill = GridBagConstraints.BOTH;
        excel1Panel.add(new JScrollPane(excel1ProductList), gbc1);

        add(excel1Panel);

        // --- Excel 2 Panel ---
        JPanel excel2Panel = new JPanel(new GridBagLayout());
        excel2Panel.setBorder(BorderFactory.createTitledBorder("From Excel 2"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.anchor = GridBagConstraints.NORTHWEST;

        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.gridwidth=2;
        excel2Panel.add(new JLabel("Attribute Name Columns:"), gbc2);
        gbc2.gridy++;
        gbc2.weightx = 1.0; gbc2.weighty = 1.0; gbc2.fill = GridBagConstraints.BOTH;
        excel2Panel.add(new JScrollPane(excel2AttributeList), gbc2);

        gbc2.gridy++;
        gbc2.weighty = 0; gbc2.fill = GridBagConstraints.HORIZONTAL;
        excel2Panel.add(new JLabel("Report Column:"), gbc2);
        gbc2.gridy++;
        excel2Panel.add(excel2ReportDropdown, gbc2);

        gbc2.gridy++;
        excel2Panel.add(new JLabel("Product Column:"), gbc2);
        gbc2.gridy++;
        excel2Panel.add(excel2ProductDropdown, gbc2);

        add(excel2Panel);
    }

    private void populateModel(ComboBoxModel<String> model, List<String> items) {
        ((DefaultComboBoxModel<String>) model).removeAllElements();
        if (items != null) {
            for (String item : items) {
                ((DefaultComboBoxModel<String>) model).addElement(item);
            }
        }
    }

    private void populateModel(ListModel<String> model, List<String> items) {
        ((DefaultListModel<String>) model).removeAllElements();
        if (items != null) {
            for (String item : items) {
                ((DefaultListModel<String>) model).addElement(item);
            }
        }
    }

    public void setExcel1Headers(List<String> headers) {
        populateModel(excel1AttrModel, headers);
        populateModel(excel1ReportModel, headers);
        populateModel(excel1ProductModel, headers);
    }

    public void setExcel2Headers(List<String> headers) {
        populateModel(excel2AttrModel, headers);
        populateModel(excel2ReportModel, headers);
        populateModel(excel2ProductModel, headers);
    }

    // --- Getters for user selections ---

    public String getExcel1AttributeColumn() {
        return (String) excel1AttributeDropdown.getSelectedItem();
    }

    public List<String> getExcel1ReportColumns() {
        return excel1ReportList.getSelectedValuesList();
    }

    public List<String> getExcel1ProductColumns() {
        return excel1ProductList.getSelectedValuesList();
    }

    public List<String> getExcel2AttributeNameColumns() {
        return excel2AttributeList.getSelectedValuesList();
    }

    public String getExcel2ReportColumn() {
        return (String) excel2ReportDropdown.getSelectedItem();
    }

    public String getExcel2ProductColumn() {
        return (String) excel2ProductDropdown.getSelectedItem();
    }
}
