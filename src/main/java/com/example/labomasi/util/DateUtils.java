package com.example.labomasi.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DateUtils() {}

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : "";
    }

    public static LocalDate parseDate(String date) {
        return date != null && !date.isEmpty() ? LocalDate.parse(date, DATE_FORMATTER) : null;
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.DAYS.between(start, end);
    }

    public static boolean isExpired(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    public static boolean isActive(LocalDate endDate) {
        return endDate == null || endDate.isAfter(LocalDate.now());
    }
}