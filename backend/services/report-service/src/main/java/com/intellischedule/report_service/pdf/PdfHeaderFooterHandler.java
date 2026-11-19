package com.intellischedule.report_service.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class PdfHeaderFooterHandler extends PdfPageEventHelper {

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(
                writer.getDirectContent(),
                Element.ALIGN_CENTER,
                new Phrase("IntelliSchedule-AI — Generated Academic Timetable", PdfStyleConstants.SUBTITLE_FONT),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 15,
                0
        );

        ColumnText.showTextAligned(
                writer.getDirectContent(),
                Element.ALIGN_RIGHT,
                new Phrase("Page " + writer.getPageNumber(), PdfStyleConstants.SUBTITLE_FONT),
                document.right(),
                document.bottom() - 15,
                0
        );
    }
}
