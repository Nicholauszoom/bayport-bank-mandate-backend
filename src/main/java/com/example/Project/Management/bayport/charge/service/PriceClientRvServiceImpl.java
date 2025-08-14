package com.example.Project.Management.bayport.charge.service;

import com.example.Project.Management.bayport.charge.entity.ClientRv;
import com.example.Project.Management.bayport.charge.entity.Price;
import com.example.Project.Management.bayport.charge.repository.ClientRvRepository;
import com.example.Project.Management.bayport.charge.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;

    private final ClientRvRepository clientRvRepository;

    public PriceServiceImpl(PriceRepository priceRepository, ClientRvRepository clientRvRepository) {
        this.priceRepository = priceRepository;
        this.clientRvRepository = clientRvRepository;
    }

    @Override
    public void savePriceScheduleFromExcel(MultipartFile file) throws Exception {
        List<Price> prices = new ArrayList<>();

        // More efficient existence check
        if (priceRepository.count() > 0) {
            priceRepository.deleteAll();
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                try {
                    Price price = new Price();
                    price.setProductName(getStringValue(row.getCell(0), evaluator));
                    price.setStartAmount(getBigDecimalValue(row.getCell(1), evaluator));
                    price.setEndAmount(getBigDecimalValue(row.getCell(2), evaluator));
                    price.setIncrement(getDoubleValue(row.getCell(3), evaluator));
                    price.setInterestRate(getBigDecimalValue(row.getCell(4), evaluator));
                    price.setLoanTerm(getIntValue(row.getCell(5), evaluator));
                    price.setDescription(getStringValue(row.getCell(6), evaluator));
                    price.setAdminPercentage(getDoubleValue(row.getCell(7), evaluator));
                    price.setProductClassDescription(getStringValue(row.getCell(8), evaluator));
                    price.setInsurancePercentage(getDoubleValue(row.getCell(9), evaluator));
                    price.setAnnualInterestRate(getDoubleValue(row.getCell(10), evaluator));
                    price.setTcocCpp(getDoubleValue(row.getCell(11), evaluator));
                    price.setFactor(getDoubleValue(row.getCell(12), evaluator));

                    prices.add(price);
                } catch (Exception e) {
                    log.warn("Error processing row {}: {}", i, e.getMessage());
                    // Continue processing other rows
                }
            }
        }

        if (!prices.isEmpty()) {
            priceRepository.saveAll(prices);
            log.info("Successfully saved {} price records", prices.size());
        } else {
            log.warn("No valid price records found in the Excel file");
        }
    }

    @Override
    public void saveClientRvFromExcel(MultipartFile file) throws Exception {
        List<ClientRv> clientRvs = new ArrayList<>();

        if (clientRvRepository.count() > 0) {
            clientRvRepository.deleteAll();
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                try {
                    ClientRv clientRv = new ClientRv();
                    clientRv.setCheckNumber(getStringValue(row.getCell(0), evaluator));
                    clientRv.setLoanNumber(getStringValue(row.getCell(1), evaluator));
                    clientRv.setReplaceBalance(getBigDecimalValue(row.getCell(2), evaluator));
                    clientRvs.add(clientRv);
                } catch (Exception e) {
                    log.warn("Error processing row {}: {}", i, e.getMessage());
                }
            }
        }

        if (!clientRvs.isEmpty()) {
            clientRvRepository.saveAll(clientRvs);
            log.info("Successfully saved {} client rv records", clientRvs.size());
        } else {
            log.warn("No valid client rv records found in the Excel file");
        }
    }


    private boolean isRowEmpty(Row row) {
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getStringValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        double numericValue = cell.getNumericCellValue();
                        if (numericValue == Math.floor(numericValue)) {
                            return String.valueOf((long) numericValue);
                        } else {
                            return String.valueOf(numericValue);
                        }
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    switch (evaluatedValue.getCellType()) {
                        case STRING:
                            return evaluatedValue.getStringValue().trim();
                        case NUMERIC:
                            double value = evaluatedValue.getNumberValue();
                            if (value == Math.floor(value)) {
                                return String.valueOf((long) value);
                            } else {
                                return String.valueOf(value);
                            }
                        case BOOLEAN:
                            return String.valueOf(evaluatedValue.getBooleanValue());
                        default:
                            return null;
                    }
                default:
                    return null;
            }
        } catch (Exception e) {
            log.warn("Error reading string value from cell: {}", e.getMessage());
            return null;
        }
    }

    private BigDecimal getBigDecimalValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP);
                case STRING:
                    String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return null;
                    return new BigDecimal(strValue).setScale(4, RoundingMode.HALF_UP);
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return BigDecimal.valueOf(evaluatedValue.getNumberValue()).setScale(4, RoundingMode.HALF_UP);
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        String strVal = evaluatedValue.getStringValue().trim();
                        if (strVal.isEmpty()) return null;
                        return new BigDecimal(strVal).setScale(4, RoundingMode.HALF_UP);
                    }
                    return null;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            log.warn("Error parsing BigDecimal from cell: {}", e.getMessage());
            return null;
        }
    }

    private Double getDoubleValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return 0.0;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return 0.0;
                    return Double.parseDouble(strValue);
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return evaluatedValue.getNumberValue();
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        String strVal = evaluatedValue.getStringValue().trim();
                        if (strVal.isEmpty()) return 0.0;
                        return Double.parseDouble(strVal);
                    }
                    return 0.0;
                default:
                    return 0.0;
            }
        } catch (NumberFormatException e) {
            log.warn("Error parsing Double from cell: {}", e.getMessage());
            return 0.0;
        }
    }

    private Integer getIntValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return 0;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) Math.round(cell.getNumericCellValue());
                case STRING:
                    String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return 0;
                    return (int) Math.round(Double.parseDouble(strValue));
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return (int) Math.round(evaluatedValue.getNumberValue());
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        String strVal = evaluatedValue.getStringValue().trim();
                        if (strVal.isEmpty()) return 0;
                        return (int) Math.round(Double.parseDouble(strVal));
                    }
                    return 0;
                default:
                    return 0;
            }
        } catch (NumberFormatException e) {
            log.warn("Error parsing Integer from cell: {}", e.getMessage());
            return 0;
        }
    }
}