package com.intellischedule.report_service.pdf;

import com.intellischedule.report_service.dto.TimetableDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class RoomMatrixPdfGenerator {

    public byte[] generateRoomMatrix(String roomNumber, List<TimetableDTO> roomSlots) {
        Document document = new Document(PageSize.A4, 25, 25, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph p = new Paragraph("Classroom Allocation Matrix: Room " + roomNumber, PdfStyleConstants.TITLE_FONT);
            p.setSpacingAfter(15);
            document.add(p);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            String[] headers = {"Day", "Time", "Subject", "Assigned Faculty"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, PdfStyleConstants.HEADER_FONT));
                cell.setBackgroundColor(PdfStyleConstants.HEADER_BG);
                table.addCell(cell);
            }

            for (TimetableDTO e : roomSlots) {
                table.addCell(new Phrase(e.dayOfWeek(), PdfStyleConstants.BODY_FONT));
                table.addCell(new Phrase(e.timeSlot(), PdfStyleConstants.BODY_FONT));
                table.addCell(new Phrase(e.subjectName(), PdfStyleConstants.BODY_FONT));
                table.addCell(new Phrase(e.facultyName(), PdfStyleConstants.BODY_FONT));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error rendering room PDF", e);
        }
        return out.toByteArray();
    }
}
