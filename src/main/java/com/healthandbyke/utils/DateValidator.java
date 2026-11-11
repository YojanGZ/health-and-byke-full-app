package com.healthandbyke.utils;

import java.time.LocalDate;

public class DateValidator {
    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }
}
