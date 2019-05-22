package com.ken.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppIkt {
    private JPanel panelMain;
    private JButton btnClose;
    private JTextField textInput;
    private JLabel textLabel;
    private JButton btnExport;
    private JButton btnDelete;

    private static JFrame frameViewer;
    private static JFrame frameMain;
    private static JFrame frameParent;
    private static App app;
    private static AppList appList;
    //private static boolean first;

    public AppIkt() {
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameParent.setEnabled(true);
                frameMain.dispose();
                frameViewer.dispose();
                //frameMain.setVisible(false);
                //System.out.println("Hihihi");
                //hide();
            }
        });
        textInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textInput.setText(textInput.getText().replaceAll("\\s+",""));
                if (!textInput.getText().isEmpty()) {
                    Computer tmp = app.findInfo(textInput.getText());
                    if(tmp != null) {
                        if(!checkDuplicate(textInput.getText())) {
                            appList.addRow(tmp.getSerial(), tmp.getMac());
                            appList.selectRow();
                            //System.out.println("im inside duplicate");
                            //System.out.println(textInput.getText());
                        } else {
                            JOptionPane.showMessageDialog(frameMain, "Duplicate. Already exist");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frameMain, "Serial or MAC does not exist in database");
                    }
                    textInput.setText("");
                }
            }
        });
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Convert content of JTable to Object[][] data
                DefaultTableModel tmp = (DefaultTableModel) appList.getTable().getModel();
                int nRow = tmp.getRowCount() + 1;
                int nCol = tmp.getColumnCount() + 1;
                String [] types = {"ASSET", "SERIAL", "MAC"};
                Object [][] data = new Object[nRow][nCol];
                for (int i = 0; i < nRow; i++) {
                    for (int j = 0; j < nCol; j++) {
                        if(i == 0) {
                            data[i][j] = types[j];
                        } else if(j == 2) {
                            String str = (String) tmp.getValueAt(i-1, j-1);
                            str = removeSymbols(str);
                            //System.out.println(str);
                            data[i][j] = str;
                        } else if (j == 1){
                            data[i][j] = tmp.getValueAt(i-1, j-1);
                        }


                    }
                }
                if(new Export(data).exportToExcel()) {
                    JOptionPane.showMessageDialog(frameMain, "Successfully created excel file");
                } else {
                    JOptionPane.showMessageDialog(frameMain, "Error creating excel file");
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appList.removeSelectedRow();
            }
        });
    }

    public Boolean checkDuplicate(String text) {
        JTable table = appList.getTable();
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                //System.out.println(table.getValueAt(i,j));
                if( table.getValueAt(i ,j).equals(text)) {
                    //System.out.println( table.getValueAt(i ,j));

                    return true;
                }
            }
        }

        return false;
    }

    public String removeSymbols(String text) {
        String result = text.replaceAll("[-.: ]","");
        return result;
    }

    public void frameSetUp() {
        appList = new AppList();
        //first = true;

        frameMain = new JFrame("IKT");
        frameMain.setContentPane(new AppIkt().panelMain);
        frameMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameMain.pack();
        frameMain.setLocationRelativeTo(null);
        frameMain.setLocation(frameMain.getX() + 1, frameMain.getY() + 1);
        frameMain.setVisible(true);
        saveOnExit(frameMain);

        frameViewer = new JFrame("IKT");
        frameViewer.setContentPane(appList.getPanelMain());
        frameViewer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameViewer.pack();
        frameViewer.setLocation(frameMain.getX() + frameMain.getWidth(), frameMain  .getY());
        frameViewer.setVisible(true);
        saveOnExit(frameViewer);

    }

    public void saveOnExit(JFrame frame) {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frameParent.setEnabled(true);
                frameMain.dispose();
                frameViewer.dispose();
            }
        });
    }

    public void setApp(App app) {
        AppIkt.app = app;
    }

    public void setFrameParent(JFrame frameParent) {
        this.frameParent = frameParent;
    }
}
