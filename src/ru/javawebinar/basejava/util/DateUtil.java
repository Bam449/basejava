package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;

public class DateUtil {

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }
    public static LocalDate NOW() {
        return LocalDate.of(3000, 1, 1);
    }
}
