package com.gregory.trello.charts.history;

import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gregory.trello.utils.DateUtils.YEAR_MONTH_DAY_DATE_FORMAT;
import static com.gregory.trello.utils.DateUtils.daysBetweenDates;

public final class History {

    private final Date startDate;
    private final Date endDate;
    private final List<String> listNames;
    private final List<Day> days;

    private History(Date startDate, Date endDate, List<String> listNames) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.listNames = listNames;
        this.days = new ArrayList<>();
        for (Date date : daysBetweenDates(startDate, endDate)) {
            days.add(new Day(date));
        }
    }

    public History printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* History stats *-*-*-*-*-*-*-*-*-*");
        System.out.println("Start date : " + YEAR_MONTH_DAY_DATE_FORMAT.format(startDate));
        System.out.println("End date   : " + YEAR_MONTH_DAY_DATE_FORMAT.format(endDate));
        for (Day day : days) {
            System.out.println("\t@" + YEAR_MONTH_DAY_DATE_FORMAT.format(day.date()));
            for (String listName : listNames) {
                System.out.println("\t\t" + listName + " :");
                System.out.println("\t\t\tcards : ");
                TrelloCardDeck cards = day.cardsInListNamed(listName);
                for (TrelloCard card : cards) {
                    System.out.println("\t\t\t\t" + card.title() + " " + card.url());
                }
                System.out.println("\t\t\tNumber of points : " + cards.points());
            }
        }
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Date startDate;
        private Date endDate;
        private List<String> listNames = new ArrayList<>();

        public Builder from(String startDate) throws ParseException {
            this.startDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(startDate);
            return this;
        }

        public Builder to(String endDate) throws ParseException {
            this.endDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(endDate);
            return this;
        }

        public Builder withListNamed(String listName) {
            this.listNames.add(listName);
            return this;
        }

        public History createHistory() {
            return new History(startDate, endDate, listNames);
        }
    }

}
