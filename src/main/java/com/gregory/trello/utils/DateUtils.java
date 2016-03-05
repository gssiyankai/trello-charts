package com.gregory.trello.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;

public final class DateUtils {

    public static final SimpleDateFormat YEAR_MONTH_DAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DAY_MONTH_DATE_FORMAT = new SimpleDateFormat("dd MMM");

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

    public static Date dayAfter(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

}
