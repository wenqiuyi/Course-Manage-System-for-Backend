package com.coursemanage.module.announcement.util;

import com.coursemanage.module.log.pojo.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Slf4j
public class ExcelGenerator {

    private static final String[] LOG_HEADERS = {
            "ID", "操作时间", "操作者学工号", "目标学工号",
            "操作模块", "操作行为", "详情信息"
    };

    /**
     * 生成日志Excel文件
     */
    public byte[] generateLogsExcel(List<LogEntity> logs, String sheetName) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // 创建样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            // 创建工作表
            Sheet sheet = workbook.createSheet(sheetName);

            // 设置列宽
            setColumnWidth(sheet);

            // 创建表头
            createHeaderRow(sheet, headerStyle);

            // 填充数据
            fillDataRows(sheet, logs, normalStyle, dateStyle);

            // 自动调整列宽（可选）
            autoSizeColumns(sheet);

            workbook.write(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            log.error("生成Excel失败", e);
            throw new RuntimeException("Excel生成失败", e);
        }
    }

    /**
     * 创建表头样式
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());

        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    /**
     * 创建日期样式
     */
    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    /**
     * 创建普通单元格样式
     */
    private CellStyle createNormalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true); // 自动换行
        return style;
    }

    /**
     * 设置列宽
     */
    private void setColumnWidth(Sheet sheet) {
        // 设置列宽（单位：字符数 * 256）
        sheet.setColumnWidth(0, 8 * 256);   // ID
        sheet.setColumnWidth(1, 20 * 256);  // 操作时间
        sheet.setColumnWidth(2, 15 * 256);  // 操作者学工号
        sheet.setColumnWidth(3, 15 * 256);  // 目标学工号
        sheet.setColumnWidth(4, 12 * 256);  // 操作模块
        sheet.setColumnWidth(5, 10 * 256);  // 操作行为
        sheet.setColumnWidth(6, 50 * 256);  // 详情信息
    }

    /**
     * 创建表头行
     */
    private void createHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < LOG_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(LOG_HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * 填充数据行
     */
    private void fillDataRows(Sheet sheet, List<LogEntity> logs,
                              CellStyle normalStyle, CellStyle dateStyle) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int rowNum = 1;

        for (LogEntity log : logs) {
            Row row = sheet.createRow(rowNum++);

            // ID
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(log.getId() != null ? log.getId() : 0);
            cell0.setCellStyle(normalStyle);

            // 操作时间
            Cell cell1 = row.createCell(1);
            if (log.getOperationTime() != null) {
                cell1.setCellValue(log.getOperationTime());
                cell1.setCellStyle(dateStyle);
            } else {
                cell1.setCellValue("N/A");
                cell1.setCellStyle(normalStyle);
            }

            // 操作者学工号
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(getSafeString(log.getOperatorSchoolNum()));
            cell2.setCellStyle(normalStyle);

            // 目标学工号
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(getSafeString(log.getTargetSchoolNum()));
            cell3.setCellStyle(normalStyle);

            // 操作模块
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(getSafeString(log.getOperationModule()));
            cell4.setCellStyle(normalStyle);

            // 操作行为
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(getSafeString(log.getOperationAction()));
            cell5.setCellStyle(normalStyle);

            // 详情信息
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(getSafeString(log.getDetailInfo()));
            cell6.setCellStyle(normalStyle);
        }
    }

    /**
     * 自动调整列宽
     */
    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < LOG_HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
            // 设置最小列宽
            if (sheet.getColumnWidth(i) < 3000) {
                sheet.setColumnWidth(i, 3000);
            }
        }
    }

    private String getSafeString(String value) {
        return value != null ? value : "";
    }
}