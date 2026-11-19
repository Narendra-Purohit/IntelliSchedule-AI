package com.intellischedule.report_service.excel;

import com.intellischedule.report_service.dto.TimetableDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimetableExcelGeneratorTest {

    private final TimetableExcelGenerator generator = new TimetableExcelGenerator();

    @Test
    void shouldGenerateValidExcelBytes() {
        List<TimetableDTO> entries = List.of(
                new TimetableDTO(1L, "Tuesday", "10:00 - 11:00", "Algorithms", "CS202", "Prof. Turing", "LH-102", "Sec-B", "Sem-4")
        );

        byte[] excel = generator.generateExcel(entries);
        assertNotNull(excel);
        assertTrue(excel.length > 0);
        // ZIP/OOXML magic bytes check (PK..)
        assertEquals('P', (char) excel[0]);
        assertEquals('K', (char) excel[1]);
    }
}
