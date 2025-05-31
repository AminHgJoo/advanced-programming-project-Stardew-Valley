package com.example.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtility {
    public static LocalDateTime getLocalDateTime(LocalDateTime source, int day) {
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

    public static LocalDate getLocalDate(LocalDate source, int day) {
        if (source == null) {
            return null;
        }
        LocalDate date = source.plusDays(0);
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

    public static int getDayDifference(LocalDateTime smallerDate, LocalDateTime biggerDate) {
        LocalDate smallerCopy = smallerDate.toLocalDate();
        LocalDate biggerCopy = biggerDate.toLocalDate();

        int ans = 0;

        while (smallerCopy.isBefore(biggerCopy)) {
            smallerCopy = DateUtility.getLocalDate(smallerCopy, 1);
            ans++;
        }

        return ans;
    }
}
