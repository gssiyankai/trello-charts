package com.gregory.trello.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;

public final class DateUtils {

    public static final SimpleDateFormat YEAR_MONTH_DAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final SimpleDateFormat DAY_MONTH_DATE_FORMAT = new SimpleDateFormat("dd MMM", Locale.US);
    public static final SimpleDateFormat DAY_MONTH_YEAR_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    public static final Date NOW = new Date();

    private DateUtils() {
    }

    public static List<Date> daysBetweenDates(Date start, Date end) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);
        while (calendar.getTime().compareTo(end) <= 0) {
            Date date = calendar.getTime();
            dates.add(date);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static int numberOfDaysBetweenDates(Date start, Date end) {
        return daysBetweenDates(start, end).size();
    }

    public static List<Date> workingDaysWithin(Date start, Date end) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);
        while (calendar.getTime().compareTo(end) <= 0) {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek >= MONDAY && dayOfWeek <= FRIDAY) {
                Date date = calendar.getTime();
                dates.add(date);
            }
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static int numberOfWorkingDaysWithin(Date start, Date end) {
        return workingDaysWithin(start, end).size();
    }

    public static Date dayAfter(Date date) {
        return addDays(date, 1);
    }

    public static Date dayBefore(Date date) {
        return addDays(date, -1);
    }

    public static Date addDays(Date date, int n) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        return calendar.getTime();
    }

    public static int year(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int month(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int dayOfMonth(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

}
