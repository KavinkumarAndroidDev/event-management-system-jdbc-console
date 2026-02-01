package com.ems.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Central utility class for date and time handling.
 *
 * Responsibilities:
 * - Standardizes date and time parsing, formatting, and conversions
 * - Ensures consistent handling of local time and UTC across the application
 *
 * Design notes:
 * - Date and time values are processed or stored in UTC
 * - ZonedDateTime is used to explicitly apply system time zone during conversion
 * - Prevents errors caused by implicit or incorrect time zone assumptions
 */
public final class DateTimeUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private DateTimeUtil() {
    }

    /**
     * System default time zone used for local conversions.
     */
    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

    /**
     * Formatter used for displaying dates in the application.
     */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Supported date formats for parsing LocalDate values.
     */
    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );

    /**
     * Supported date-time formats for parsing LocalDateTime values.
     */
    private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    );

    /* ===================== CURRENT TIME ===================== */

    /**
     * Retrieves the current UTC timestamp.
     *
     * @return current UTC instant
     */
    public static Instant getCurrentUtc() {
        return Instant.now();
    }

    /**
     * Converts a UTC instant to system local time.
     *
     * @param utcInstant the UTC instant
     * @return zoned date-time in system default time zone
     */
    public static ZonedDateTime convertUtcToLocal(Instant utcInstant) {
        return utcInstant.atZone(SYSTEM_ZONE);
    }

    /* ===================== CONVERSIONS ===================== */

    /**
     * Converts a SQL Timestamp to UTC instant.
     *
     * @param timestamp the SQL timestamp
     * @return UTC instant
     */
    public static Instant convertLocalToUtc(Timestamp timestamp) {
        return timestamp.toInstant();
    }

    /**
     * Converts a LocalDateTime in system default time zone to UTC instant.
     *
     * @param localDateTime the local date-time
     * @return UTC instant
     */
    public static Instant convertLocalDefaultToUtc(LocalDateTime localDateTime) {
        return localDateTime.atZone(SYSTEM_ZONE).toInstant();
    }

    /* ===================== FORMATTING ===================== */

    /**
     * Formats a LocalDateTime into a user-friendly date and time string.
     *
     * @param localDateTime the local date-time to format
     * @return formatted date-time string
     */
    public static String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().format(DISPLAY_DATE_FORMAT)
                + " " + localDateTime.toLocalTime();
    }

    /* ===================== PARSING ===================== */

    /**
     * Parses a date string into a LocalDate using supported formats.
     *
     * @param dateString the date string to parse
     * @return parsed LocalDate or null if parsing fails
     */
    public static LocalDate parseLocalDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    /**
     * Parses a date-time string into a LocalDateTime using supported formats.
     *
     * @param dateTimeString the date-time string to parse
     * @return parsed LocalDateTime or null if parsing fails
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isBlank()) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(dateTimeString, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }
}
