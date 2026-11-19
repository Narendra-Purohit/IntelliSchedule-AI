package com.intellischedule.report_service.pdf;

import com.intellischedule.report_service.dto.TimetableDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimetablePdfGeneratorTest {

    private final TimetablePdfGenerator generator = new TimetablePdfGenerator();

    @Test
    void shouldGenerateValidPdfBytes() {
        List<TimetableDTO> entries = List.of(
                new TimetableDTO(1L, "Monday", "09:00 - 10:00", "Data Structures", "CS201", "Dr. Alan", "LH-101", "Sec-A", "Sem-3")
        );

        byte[] pdf = generator.generatePdf(entries, "Test Schedule");
        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
        // Standard PDF magic byte signature check %PDF-
        assertEquals('%', (char) pdf[0]);
        assertEquals('P', (char) pdf[1]);
        assertEquals('D', (char) pdf[2]);
        assertEquals('F', (char) pdf[3]);
    }
}
