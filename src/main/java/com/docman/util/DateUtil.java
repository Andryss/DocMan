package com.docman.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtil {
    private static final ZoneId zoneId = ZoneId.systemDefault();

    public static LocalDate toLocalDate(Instant instant) {
        return LocalDate.ofInstant(instant, zoneId);
    }
    public static Instant toInstant(LocalDate localDate) {
        return localDate.atStartOfDay(zoneId).toInstant();
    }
}
