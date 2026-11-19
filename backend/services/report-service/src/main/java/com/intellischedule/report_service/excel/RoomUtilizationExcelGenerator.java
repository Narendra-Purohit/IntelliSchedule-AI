package com.intellischedule.report_service.excel;

import com.intellischedule.report_service.dto.RoomUtilizationDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class RoomUtilizationExcelGenerator {

    public byte[] generateRoomUtilizationReport(List<RoomUtilizationDTO> rooms) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Room Utilization");
            CellStyle headerStyle = ExcelWorkbookHelper.createHeaderStyle(workbook);
            CellStyle dataStyle = ExcelWorkbookHelper.createBorderedStyle(workbook);

            String[] columns = {"Room ID", "Room Number", "Type", "Capacity", "Slots Occupied", "Utilization %"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (RoomUtilizationDTO r : rooms) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.roomId());
                row.createCell(1).setCellValue(r.roomNumber());
                row.createCell(2).setCellValue(r.roomType());
                row.createCell(3).setCellValue(r.capacity());
                row.createCell(4).setCellValue(r.totalSlotsOccupied());
                row.createCell(5).setCellValue(String.format("%.1f%%", r.utilizationPercentage()));
                for (int c = 0; c < 6; c++) row.getCell(c).setCellStyle(dataStyle);
            }

            for (int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Room utilization export error", e);
        }
    }
}
