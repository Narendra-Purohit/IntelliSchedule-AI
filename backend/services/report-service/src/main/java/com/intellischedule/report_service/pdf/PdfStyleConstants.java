package com.intellischedule.report_service.pdf;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import java.awt.Color;

public final class PdfStyleConstants {

    private PdfStyleConstants() {}

    public static final Color PRIMARY_COLOR = new Color(30, 58, 138);   // Indigo-900
    public static final Color ACCENT_COLOR = new Color(59, 130, 246);   // Blue-500
    public static final Color HEADER_BG = new Color(241, 245, 249);      // Slate-100
    public static final Color BORDER_COLOR = new Color(203, 213, 225);   // Slate-300

    public static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, PRIMARY_COLOR);
    public static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);
    public static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
    public static final Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
}
