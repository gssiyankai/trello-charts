package com.gregory.trello.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public final class DateUtils {

    public static final SimpleDateFormat YEAR_MONTH_DAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DAY_MONTH_DATE_FORMAT = new SimpleDateFormat("dd MMM");

    private DateUtils() {
    }

    public static List<Date> daysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);
        while (calendar.getTime().compareTo(enddate) <= 0) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

}
