package com.gregory.trello.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public final class DateUtils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private DateUtils() {
    }

    public static List<Date> daysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);
        while (calendar.getTime().compareTo(enddate)<=0) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

}
