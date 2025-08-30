package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;

public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(2050, 1, 1);

    public static LocalDate of (int startDate, Month startMonth) {
        return LocalDate.of(startDate, startMonth, 1);
    }
}
