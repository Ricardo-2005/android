package com.example.foodcalu.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public final class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private DateUtils() {
    }

    public static long today() {
        return startOfDay(System.currentTimeMillis());
    }

    public static long startOfDay(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String formatDate(long timeMillis) {
        return DATE_FORMAT.format(timeMillis);
    }

    public static List<Long> lastDays(int days) {
        List<Long> results = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(today());
        calendar.add(Calendar.DAY_OF_YEAR, -(days - 1));
        for (int i = 0; i < days; i++) {
            results.add(calendar.getTimeInMillis());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return results;
    }

    public static List<Long> lastDaysFrom(long end, int days) {
        List<Long> results = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startOfDay(end));
        calendar.add(Calendar.DAY_OF_YEAR, -(days - 1));
        for (int i = 0; i < days; i++) {
            results.add(calendar.getTimeInMillis());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return results;
    }

    public static long addDays(long start, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);
        calendar.add(Calendar.DAY_OF_YEAR, offset);
        return startOfDay(calendar.getTimeInMillis());
    }
}
