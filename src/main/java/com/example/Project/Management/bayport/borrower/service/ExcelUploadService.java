package com.dflex.emkopo.app.borrower.service;

import com.dflex.emkopo.app.borrower.entity.Borrower;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ExcelUploadService {



    static {
        IOUtils.setByteArrayMaxOverride(150_000_000);
    }
//    public static boolean isValidExcelFile(MultipartFile file){
//        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
//    }
    public static List<Borrower> getCustomersDataFromExcel(InputStream inputStream){
        List<Borrower> borrowers = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheet("BANK");
            int rowIndex =0;
            for (Row row : sheet){
                if (rowIndex ==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Borrower borrower = new Borrower();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 -> borrower.setCheckNumber(String.valueOf((int) cell.getNumericCellValue()));
                        case 1 -> borrower.setFullName(cell.getStringCellValue());
                        case 2 -> borrower.setBankAccountNumber(String.valueOf((int) cell.getNumericCellValue()));
                        case 3 -> borrower.setNearestBranchName(cell.getStringCellValue());

                        default -> {
                        }
                    }
                    cellIndex++;
                }
                borrowers.add(borrower);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return borrowers;
    }

}