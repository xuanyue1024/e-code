package com.ecode.utils;

import com.ecode.constant.MessageConstant;
import com.ecode.exception.BaseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 导入导出工具。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
public final class ExcelUtil {

    private ExcelUtil() {
    }

    public static byte[] write(String sheetName, List<String> headers, List<List<Object>> rows) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                List<Object> values = rows.get(rowIndex);
                for (int columnIndex = 0; columnIndex < values.size(); columnIndex++) {
                    row.createCell(columnIndex).setCellValue(toCellString(values.get(columnIndex)));
                }
            }
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new BaseException(MessageConstant.ERROR);
        }
    }

    public static List<Map<String, String>> read(MultipartFile file, List<String> requiredHeaders) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null
                || !file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            throw new BaseException("请上传 xlsx 文件");
        }
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
                throw new BaseException("Excel 文件为空");
            }
            List<String> headers = readHeaders(sheet.getRow(0));
            for (String requiredHeader : requiredHeaders) {
                if (!headers.contains(requiredHeader)) {
                    throw new BaseException("缺少表头：" + requiredHeader);
                }
            }

            List<Map<String, String>> rows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isBlankRow(row)) {
                    continue;
                }
                Map<String, String> rowMap = new LinkedHashMap<>();
                rowMap.put("__rowNumber", String.valueOf(i + 1));
                for (int columnIndex = 0; columnIndex < headers.size(); columnIndex++) {
                    rowMap.put(headers.get(columnIndex), readCell(row.getCell(columnIndex)));
                }
                rows.add(rowMap);
            }
            return rows;
        } catch (IOException e) {
            throw new BaseException("Excel 文件读取失败");
        }
    }

    public static String fileName(String prefix) {
        return prefix + "_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".xlsx";
    }

    private static List<String> readHeaders(Row headerRow) {
        if (headerRow == null) {
            throw new BaseException("缺少表头");
        }
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String value = readCell(headerRow.getCell(i));
            if (StringUtils.hasText(value)) {
                headers.add(value);
            }
        }
        return headers;
    }

    private static boolean isBlankRow(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (StringUtils.hasText(readCell(row.getCell(i)))) {
                return false;
            }
        }
        return true;
    }

    private static String readCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private static String toCellString(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof LocalDate localDate) {
            return localDate.toString();
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime.toString();
        }
        return String.valueOf(value);
    }
}
