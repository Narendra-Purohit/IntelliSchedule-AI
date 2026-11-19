package com.intellischedule.report_service.pdf;

import com.intellischedule.report_service.dto.TimetableDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class FacultySchedulePdfGenerator {

    public byte[] generateFacultyPdf(String facultyName, List<TimetableDTO> facultyEntries) {
        Document document = new Document(PageSize.A4, 25, 25, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph title = new Paragraph("Faculty Teaching Schedule: " + facultyName, PdfStyleConstants.TITLE_FONT);
            title.setSpacingAfter(10);
            document.add(title);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            String[] headers = {"Day", "Time Slot", "Subject", "Room", "Section"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, PdfStyleConstants.HEADER_FONT));
                cell.setBackgroundColor(PdfStyleConstants.HEADER_BG);
                table.addCell(cell);
            }

            for (TimetableDTO e : facultyEntries) {
                table.addCell(new Phrase(e.dayOfWeek(), PdfStyleConstants.BODY_FONT));
                table.addCell(new Phrase(e.timeSlot(), PdfStyleConstants.BODY_FONT));
                table.addCell(new Phrase(e.subjectName(), PdfStyleConstants.BODY_FONT));
                table.addCell(new Phrase(e.roomNumber(), PdfStyleConstants.BODY_FONT));
                table.addCell(new Phrase(e.sectionName(), PdfStyleConstants.BODY_FONT));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error rendering faculty PDF", e);
        }
        return out.toByteArray();
    }
}
