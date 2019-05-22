package com.ken.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;
import java.util.TreeMap;

public class AppList {
    private JPanel panelMain;
    private JTable table;
    private JScrollPane scrollPane;

    public JPanel getPanelMain() {
        return panelMain;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        table = createTable();
        scrollToVisible(table);
    }

    public void scrollToVisible(JTable _table) {
        _table.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                _table.scrollRectToVisible(_table.getCellRect(_table.getRowCount()-1, 0, true));
            }
        });
    }

    public JTable createTable() {
        String []columnNames = {"Serial", "MAC"};
        Object[][] data = null;
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        return new JTable(model);
    }

    public void readTable(TreeMap<Integer, Computer> data) {
        for(Map.Entry<Integer, Computer> entry : data.entrySet()) {
            addRow(entry.getValue().getSerial(), entry.getValue().getMac());
        }
    }

    public void removeSelectedRow() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int [] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            model.removeRow(rows[i] - i);
        }
    }

    public void selectRow() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int lastRow = table.convertRowIndexToView(model.getRowCount() - 1);
        table.setRowSelectionInterval(lastRow, lastRow);
    }

    public void addRow(String serial, String mac) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{serial, mac.toUpperCase()});
    }

    public JTable getTable() {
        return table;
    }
}
