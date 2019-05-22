package com.ken.app;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Import {


    public ArrayList<Computer> importFromExcel() {
        File file = fileChooser();
        if(file == null) {
            //No file found
            return null;
        }
        Workbook workbook;
        DataFormatter dataFormatter = new DataFormatter();
        try {
            FileInputStream excelFile = new FileInputStream(file);
            workbook = new XSSFWorkbook(excelFile);

            ArrayList<Computer> data = new ArrayList<Computer>();

            for(Sheet sheet : workbook) {
                boolean first = true;
                for(Row row : sheet) {
                    if(first) {
                        first = false;
                        continue;
                    }
                    int i = 0;
                    String serial = "";
                    for(Cell cell : row) {
                        String cellValue = dataFormatter.formatCellValue(cell);
                        //System.out.println(cellValue);
                        if(i == 1) {
                            Computer tmp = new Computer(serial, cellValue);
                            data.add(tmp);
                            break;
                        } else {
                            serial = cellValue;
                        }
                        i++;
                    }
                }
            }
            return data;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public File fileChooser() {
        JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jFileChooser.setDialogTitle("Select an xlsx file");
        jFileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel workbooks", "xlsx");
        jFileChooser.addChoosableFileFilter(filter);

        int returnValve = jFileChooser.showOpenDialog(null);
        if (returnValve == JFileChooser.APPROVE_OPTION) {
            System.out.println(jFileChooser.getSelectedFile().getPath());
            File selectedFile = jFileChooser.getSelectedFile();
            return selectedFile;
        }
        return null;
    }


}
