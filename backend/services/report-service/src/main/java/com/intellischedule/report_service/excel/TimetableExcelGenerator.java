package com.intellischedule.report_service.excel;

import com.intellischedule.report_service.dto.TimetableDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class TimetableExcelGenerator {

    public byte[] generateExcel(List<TimetableDTO> entries) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Timetable Schedule");
            CellStyle headerStyle = ExcelWorkbookHelper.createHeaderStyle(workbook);
            CellStyle dataStyle = ExcelWorkbookHelper.createBorderedStyle(workbook);

            String[] columns = {"ID", "Day", "Time Slot", "Subject", "Code", "Faculty", "Room", "Section"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            if (entries != null) {
                for (TimetableDTO e : entries) {
                    Row r = sheet.createRow(rowNum++);
                    r.createCell(0).setCellValue(e.id() != null ? e.id() : 0);
                    r.createCell(1).setCellValue(e.dayOfWeek());
                    r.createCell(2).setCellValue(e.timeSlot());
                    r.createCell(3).setCellValue(e.subjectName());
                    r.createCell(4).setCellValue(e.subjectCode());
                    r.createCell(5).setCellValue(e.facultyName());
                    r.createCell(6).setCellValue(e.roomNumber());
                    r.createCell(7).setCellValue(e.sectionName());
                    for (int c = 0; c < 8; c++) {
                        r.getCell(c).setCellStyle(dataStyle);
                    }
                }
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Excel export error", e);
        }
    }
}
