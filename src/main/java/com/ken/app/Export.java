package com.ken.app;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Export {

    private Object [][] data;

    public Export(Object[][] data) {
        this.data = data;
    }

    public boolean exportToExcel() {
        //Check if data is empty or something


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        System.out.println("Creating Excel document");

        int rowNum = 0;
        for(Object[] datatype : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for(Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue((String) field);
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(new File("ExcelExport.xlsx"));
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
