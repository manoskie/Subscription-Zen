package com.subscriptionzen.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Utility class for date parsing and formatting.
 * Demonstrates: java.sql.Date manipulation, SimpleDateFormat.
 */
public class DateUtils {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    /**
     * Parses a date string in yyyy-MM-dd format to java.sql.Date.
     *
     * @param dateStr the date string to parse
     * @return java.sql.Date object
     * @throws ParseException if the string is not in the expected format
     */
    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        sdf.setLenient(false);
        java.util.Date utilDate = sdf.parse(dateStr);
        return new Date(utilDate.getTime());
    }

    /**
     * Formats a java.sql.Date to yyyy-MM-dd string.
     *
     * @param date the date to format
     * @return formatted date string, or "N/A" if null
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "N/A";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        return sdf.format(date);
    }

    /**
     * Returns today's date as java.sql.Date.
     */
    public static Date today() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * Calculates renewal date (startDate + daysToAdd).
     *
     * @param startDate  base date
     * @param daysToAdd  number of days to add
     * @return new date with days added
     */
    public static Date addDays(Date startDate, int daysToAdd) {
        long millis = startDate.getTime() + ((long) daysToAdd * 24 * 60 * 60 * 1000);
        return new Date(millis);
    }
}
