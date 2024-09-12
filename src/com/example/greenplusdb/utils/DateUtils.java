package com.example.greenplusdb.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {

    public static List<LocalDateTime> dateListRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<LocalDateTime> dateListRange = new ArrayList<>();
        for (LocalDateTime dateTest = startDate; !dateTest.isAfter(endDate); dateTest = dateTest.plusDays(1)) {
            dateListRange.add(dateTest);
        }
        return dateListRange;
    }

    public static boolean isDateAvailable(LocalDateTime startDate, LocalDateTime endDate, List<LocalDateTime> dates) {
        return dates.stream()
                .anyMatch(date -> (date.isAfter(startDate.minusDays(1)) && date.isBefore(endDate.plusDays(1))));
    }


    public static long calculateDaysBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
