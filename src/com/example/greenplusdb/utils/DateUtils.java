package com.example.greenplusdb.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {


    public static List<LocalDate> dateListRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateListRange = new ArrayList<>();
        for (LocalDate dateTest = startDate; !dateTest.isAfter(endDate); dateTest = dateTest.plusDays(1)) {
            dateListRange.add(dateTest);
        }
        return dateListRange;
    }


    public static boolean isDateAvailable(LocalDate startDate, LocalDate endDate, List<LocalDate> dates) {
        return dates.stream().anyMatch(date -> (date.isAfter(startDate.minusDays(1)) && date.isBefore(endDate.plusDays(1))));
    }
}
