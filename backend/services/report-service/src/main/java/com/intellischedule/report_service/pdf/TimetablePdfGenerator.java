package com.intellischedule.report_service.pdf;

import com.intellischedule.report_service.dto.TimetableDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class TimetablePdfGenerator {

    public byte[] generatePdf(List<TimetableDTO> entries, String title) {
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new PdfHeaderFooterHandler());
            document.open();

            Paragraph pTitle = new Paragraph(title, PdfStyleConstants.TITLE_FONT);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(pTitle);

            Paragraph pSub = new Paragraph("Academic Weekly Class Schedule", PdfStyleConstants.SUBTITLE_FONT);
            pSub.setAlignment(Element.ALIGN_CENTER);
            pSub.setSpacingAfter(15);
            document.add(pSub);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            String[] headers = {"Day", "Time Slot", "Subject", "Code", "Faculty", "Room", "Section"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, PdfStyleConstants.HEADER_FONT));
                cell.setBackgroundColor(PdfStyleConstants.HEADER_BG);
                cell.setPadding(6);
                table.addCell(cell);
            }

            if (entries != null) {
                for (TimetableDTO entry : entries) {
                    table.addCell(new Phrase(entry.dayOfWeek() != null ? entry.dayOfWeek() : "-", PdfStyleConstants.BODY_FONT));
                    table.addCell(new Phrase(entry.timeSlot() != null ? entry.timeSlot() : "-", PdfStyleConstants.BODY_FONT));
                    table.addCell(new Phrase(entry.subjectName() != null ? entry.subjectName() : "-", PdfStyleConstants.BODY_FONT));
                    table.addCell(new Phrase(entry.subjectCode() != null ? entry.subjectCode() : "-", PdfStyleConstants.BODY_FONT));
                    table.addCell(new Phrase(entry.facultyName() != null ? entry.facultyName() : "-", PdfStyleConstants.BODY_FONT));
                    table.addCell(new Phrase(entry.roomNumber() != null ? entry.roomNumber() : "-", PdfStyleConstants.BODY_FONT));
                    table.addCell(new Phrase(entry.sectionName() != null ? entry.sectionName() : "-", PdfStyleConstants.BODY_FONT));
                }
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to render PDF report", e);
        }

        return out.toByteArray();
    }
}
