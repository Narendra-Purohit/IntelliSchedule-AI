package com.intellischedule.report_service.excel;

import com.intellischedule.report_service.dto.FacultyDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class FacultyWorkloadExcelGenerator {

    public byte[] generateWorkloadReport(List<FacultyDTO> facultyList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Faculty Workload");
            CellStyle headerStyle = ExcelWorkbookHelper.createHeaderStyle(workbook);
            CellStyle dataStyle = ExcelWorkbookHelper.createBorderedStyle(workbook);

            String[] columns = {"Faculty ID", "Name", "Email", "Department", "Max Hours/Wk", "Allocated Hours"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (FacultyDTO f : facultyList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(f.id());
                row.createCell(1).setCellValue(f.name());
                row.createCell(2).setCellValue(f.email());
                row.createCell(3).setCellValue(f.department());
                row.createCell(4).setCellValue(f.maxWeeklyHours());
                row.createCell(5).setCellValue(f.allocatedHours());
                for (int c = 0; c < 6; c++) row.getCell(c).setCellStyle(dataStyle);
            }

            for (int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Faculty workload excel export error", e);
        }
    }
}
