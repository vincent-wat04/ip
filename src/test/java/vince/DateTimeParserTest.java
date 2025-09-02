package vince;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import vince.util.DateTimeParser;
import vince.exception.VinceException;

public class DateTimeParserTest {

    @Test
    @DisplayName("parse dd/MM/yyyy HHmm -> LocalDateTime")
    void parse_ddMMyyyy_HHmm() {
        LocalDateTime dt = DateTimeParser.parseDateTime("15/12/2024 1800");
        Assertions.assertEquals(LocalDate.of(2024, 12, 15), dt.toLocalDate());
        Assertions.assertEquals(LocalTime.of(18, 0), dt.toLocalTime());
    }

    @Test
    @DisplayName("parse yyyy-MM-dd -> start of day LocalDateTime")
    void parse_ISO_date_only() {
        LocalDateTime dt = DateTimeParser.parseDateTime("2024-12-15");
        Assertions.assertEquals(LocalDate.of(2024, 12, 15), dt.toLocalDate());
        Assertions.assertEquals(LocalTime.MIDNIGHT, dt.toLocalTime());
    }

    @Test
    @DisplayName("format outputs English month names")
    void format_outputsEnglish() {
        LocalDateTime dt = LocalDateTime.of(2024, 12, 15, 18, 0);
        String formatted = DateTimeParser.formatDateTime(dt);
        Assertions.assertEquals("Dec 15 2024, 18:00", formatted);
    }

    @Test
    @DisplayName("invalid format throws VinceException")
    void parse_invalidFormat_throws() {
        Assertions.assertThrows(VinceException.class, () -> DateTimeParser.parseDateTime("15-12-2024 18:00"));
    }
}
