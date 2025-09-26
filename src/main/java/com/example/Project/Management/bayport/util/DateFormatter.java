package com.example.Project.Management.bayport.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DateFormatter {

    // Common date format patterns
    public static final String YY_MM_DD_FORMAT = "yy-MM-dd";
    public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    public static final String DD_MM_YYYY_FORMAT = "dd-MM-yyyy";
    public static final String MM_DD_YYYY_FORMAT = "MM/dd/yyyy";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ISO_DATE_TIME; // e.g. 2025-12-24T21:00:00.000Z
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public static String formatDateToYyMmDd(Date date) {
        if (date == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public static Date ddMmYyStringToDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DD_MM_YYYY_FORMAT);
            formatter.setLenient(false);
            return formatter.parse(dateString.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return null;
        }

        try {
            CellType cellType = cell.getCellType();

            // Handle formula cells
            if (cellType == CellType.FORMULA) {
                cellType = evaluator.evaluateFormulaCell(cell);
            }

            switch (cellType) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // Cell contains a date
                        return cell.getDateCellValue();
                    } else {
                        // Cell contains a number, try to convert it to date
                        double numericValue = cell.getNumericCellValue();
                        return DateUtil.getJavaDate(numericValue);
                    }

                case STRING:
                    // Cell contains a string, try to parse it as a date
                    String stringValue = cell.getStringCellValue().trim();
                    return ddMmYyStringToDate(stringValue);

                case BLANK:
                    return null;

                default:
                    return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDate parseIsoToLocalDate(String isoDate) {
        return LocalDateTime.parse(isoDate, INPUT_FORMATTER).toLocalDate();
    }

    /**
     * Format LocalDate into dd-MM-yyyy string.
     */
    public static String formatLocalDate(LocalDate date) {
        return date.format(OUTPUT_FORMATTER);
    }

    /**
     * Convert ISO string directly to dd-MM-yyyy string.
     */
    public static String parseIsoToFormattedDate(String isoDate) {
        return formatLocalDate(parseIsoToLocalDate(isoDate));
    }

    public static LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate convertStringToLocalDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date string: " + dateStr + " for pattern: " + pattern, e);
        }
    }

    public static LocalDate parseJsDateString(String jsDate) {
        if (jsDate == null || jsDate.isBlank()) return null;

        // Case 1: Try ISO yyyy-MM-dd format
        try {
            return LocalDate.parse(jsDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException ignored) {}

        // Case 2: Try JavaScript Date.toString() format
        // Example: "Tue Sep 02 2025 00:00:00 GMT+0300 (East Africa Time)"
        try {
            // Remove timezone part in parentheses
            String cleanedDate = jsDate.replaceAll("\\s*\\([^)]+\\)\\s*$", "").trim();

            // Parse the cleaned date string
            DateTimeFormatter jsFormatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH);
            ZonedDateTime zdt = ZonedDateTime.parse(cleanedDate, jsFormatter);
            return zdt.toLocalDate();
        } catch (DateTimeParseException ignored) {}

        // Case 3: Try JavaScript toISOString() format (with T and Z)
        try {
            return LocalDate.parse(jsDate, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException ignored) {}

        // Case 4: Try RFC_1123_DATE_TIME format
        try {
            ZonedDateTime zdt = ZonedDateTime.parse(jsDate, DateTimeFormatter.RFC_1123_DATE_TIME);
            return zdt.toLocalDate();
        } catch (DateTimeParseException ignored) {}

        // Case 5: Try dd-MM-yyyy format
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(jsDate, formatter);
        } catch (DateTimeParseException ignored) {}

        // Case 6: Try MM/dd/yyyy format (US format)
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return LocalDate.parse(jsDate, formatter);
        } catch (DateTimeParseException ignored) {}

        // Case 7: Try yyyy/MM/dd format
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return LocalDate.parse(jsDate, formatter);
        } catch (DateTimeParseException ignored) {}

        // Case 8: Try to extract date from complex JS date string using regex
        try {
            // Extract date components using regex for format like "Tue Sep 02 2025 00:00:00 GMT+0300"
            Pattern pattern = Pattern.compile("\\w{3}\\s+(\\w{3})\\s+(\\d{1,2})\\s+(\\d{4})");
            Matcher matcher = pattern.matcher(jsDate);

            if (matcher.find()) {
                String monthStr = matcher.group(1);
                int day = Integer.parseInt(matcher.group(2));
                int year = Integer.parseInt(matcher.group(3));

                Month month = Month.valueOf(monthStr.toUpperCase());
                return LocalDate.of(year, month, day);
            }
        } catch (Exception ignored) {}

        throw new IllegalArgumentException("Cannot parse date string: " + jsDate);
    }

    private static String monthToNumber(String month) {
        return switch (month) {
            case "Jan" -> "01"; case "Feb" -> "02"; case "Mar" -> "03";
            case "Apr" -> "04"; case "May" -> "05"; case "Jun" -> "06";
            case "Jul" -> "07"; case "Aug" -> "08"; case "Sep" -> "09";
            case "Oct" -> "10"; case "Nov" -> "11"; case "Dec" -> "12";
            default -> "01";
        };
    }

}