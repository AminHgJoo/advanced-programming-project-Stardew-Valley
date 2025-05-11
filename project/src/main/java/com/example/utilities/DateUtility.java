package com.example.utilities;

import java.time.LocalDateTime;

public class DateUtility {
    public static LocalDateTime getLocalDate(LocalDateTime source, int day) {
        if (source == null) {
            return null;
        }
        LocalDateTime date = source.plusDays(0);
        if (day == -1) {
            return null;
        }
        for (int i = 0; i < day; i++) {
            date = date.plusDays(1);
            if (date.getDayOfMonth() >= 29) {
                date = date.minusDays(28);
                date = date.plusMonths(1);
            }
        }
        return date;
    }
}
