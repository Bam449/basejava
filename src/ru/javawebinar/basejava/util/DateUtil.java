package ru.javawebinar.basejava.util;

import java.time.LocalDate;

public class DateUtil {

public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    public static LocalDate of (int startDate, int startMonth) {
        return LocalDate.of(startDate, startMonth, 1);
    }
}
