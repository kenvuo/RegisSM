package com.ken.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AppView {
    private JPanel panelMain;
    private JButton btnRegister;
    private JButton btnIkt;
    private JButton btnDelete;
    private JTextField textMac;
    private JTextField textSerial;
    private JTextField textInput;
    private JButton btnImport;

    private static JFrame frameMain;
    private static JFrame frameViewer;

    private static AppList appList;
    private static App app;


    public AppView() {
        textInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textInput.setText(textInput.getText().replaceAll("\\s+",""));
                if(!textInput.getText().isEmpty()) {
                    if(textInput.getText().matches("^(?:[0-9A-Fa-f]{2}([-: ]?))(?:[0-9A-Fa-f]{2}\\1){4}[0-9A-Fa-f]{2}|([0-9A-Fa-f]{4}\\.){2}[0-9A-Fa-f]{4}$"))
                        //^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$
                        //^(?:[0-9A-Fa-f]{2}([-: ]?))(?:[0-9A-Fa-f]{2}\1){4}[0-9A-Fa-f]{2}|([0-9A-Fa-f]{4}\.){2}[0-9A-Fa-f]{4}$
                        textMac.setText(textInput.getText());
                    else
                        textSerial.setText(textInput.getText());

                    textInput.setText("");

                    if(!textMac.getText().isEmpty() && !textSerial.getText().isEmpty()) {
                        if(app.findKey(textSerial.getText(), textMac.getText())) {
                            JOptionPane.showMessageDialog(frameMain, "Serial or MAC already exist");
                        } else {
                            app.add(textSerial.getText(), textMac.getText());
                            appList.addRow(textSerial.getText(), textMac.getText());
                            appList.selectRow();
                        }
                        textMac.setText("");
                        textSerial.setText("");
                    }
                }
            }
        });
        btnIkt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameMain.setEnabled(false);
                AppIkt tmp = new AppIkt();
                tmp.frameSetUp();
                tmp.setApp(app);
                tmp.setFrameParent(frameMain);

            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = appList.getTable().getSelectedRow();
                int col = appList.getTable().getSelectedColumn();
                System.out.println(appList.getTable().getValueAt(row, col));

                app.deleteKey((String) appList.getTable().getValueAt(row, col));
                appList.removeSelectedRow();
                //app.print();
            }
        });
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){
            public boolean dispatchKeyEvent(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    /*try {
                        Robot r = new Robot();
                        r.keyPress(KeyEvent.VK_ENTER);
                    } catch (AWTException err) {
                        err.printStackTrace();
                    }*/

                    return true;
                }
                return false;
            }
        });
        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Computer> exlData = new Import().importFromExcel();
                int duplicates = 0;
                if(exlData!= null) {

                    for(Computer compData : exlData) {
                        if(app.findKey(compData.getSerial(), compData.getMac())) {
                            duplicates++;
                            //JOptionPane.showMessageDialog(frameMain, "Serial or MAC already exist");
                        } else {
                            app.add(compData.getSerial(), compData.getMac());
                            appList.addRow(compData.getSerial(), compData.getMac());
                            appList.selectRow();
                        }
                    }

                    JOptionPane.showMessageDialog(frameMain, "Import completed with " + duplicates + " duplicates");


                } else {
                    JOptionPane.showMessageDialog(frameMain, "Error importing excel file");
                }



            }
        });
    }


    public void startUp() {
        //App and AppList
        app = new App();
        appList = new AppList();
        appList.readTable(app.getData());

        //Main window
        frameMain = new JFrame("REGISTRATION");
        frameMain.setContentPane(new AppView().panelMain);
        frameMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameMain.pack();
        frameMain.setLocationRelativeTo(null);
        frameMain.setVisible(true);
        saveOnExit(frameMain);

        //Viewer
        frameViewer = new JFrame("REGISTRATION");
        frameViewer.setContentPane(appList.getPanelMain());
        frameViewer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameViewer.pack();
        frameViewer.setLocation(frameMain.getX() + frameMain.getWidth(), frameMain  .getY());
        frameViewer.setVisible(true);
        saveOnExit(frameViewer);

        //app.print();
    }

    public void saveOnExit(JFrame frame) {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    app.saveDatabase();
                    System.exit(0);
                }
            }
        });
    }



    public static void main(String[] args) {
        new AppView().startUp();
    }
}
