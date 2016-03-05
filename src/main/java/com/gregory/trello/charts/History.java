package com.gregory.trello.charts;

import com.gregory.trello.model.TrelloCard;

import java.text.ParseException;
import java.util.*;

import static com.gregory.trello.utils.DateUtils.DATE_FORMAT;
import static com.gregory.trello.utils.DateUtils.daysBetweenDates;
import static com.gregory.trello.utils.TrelloUtils.board;

public final class History {

    private final Date startDate;
    private final Date endDate;
    private final List<String> listNames;

    private History(Date startDate, Date endDate, List<String> listNames) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.listNames = listNames;
    }

    public Map<String, Integer> numberOfPointsAt(Date date) {
        Map<String, Integer> results = new LinkedHashMap<>();

        for (Map.Entry<String, List<TrelloCard>> entry : cardsAt(date).entrySet()) {
            String listName = entry.getKey();
            List<TrelloCard> cards = entry.getValue();
            int points = 0;
            for (TrelloCard card : cards) {
                points += card.points();
            }
            results.put(listName, points);
        }

        return results;
    }

    public Map<String, List<TrelloCard>> cardsAt(Date date) {
        Map<String, List<TrelloCard>> results = new LinkedHashMap<>();

        for (TrelloCard card : board().cardsAt(date)) {
            String listId = card.listIdAt(date);
            String listName = board().listById(listId).name();
            List<TrelloCard> cards = results.get(listName);
            if (cards == null) {
                cards = new ArrayList<>();
                results.put(listName, cards);
            }
            cards.add(card);
            results.put(listName, cards);
        }

        return results;
    }

    public History printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* History stats *-*-*-*-*-*-*-*-*-*");
        System.out.println("Start date : " + DATE_FORMAT.format(startDate));
        System.out.println("End date   : " + DATE_FORMAT.format(endDate));
        for (Date date : daysBetweenDates(startDate, endDate)) {
            System.out.println("\t@" + DATE_FORMAT.format(date));
            Map<String, List<TrelloCard>> cards = cardsAt(date);
            Map<String, Integer> points = numberOfPointsAt(date);
            for (String listName : listNames) {
                System.out.println("\t\t" + listName + " :");
                System.out.println("\t\t\tcards : ");
                if (cards.containsKey(listName)) {
                    for (TrelloCard card : cards.get(listName)) {
                        System.out.println("\t\t\t\t" + card.title() + " " + card.url());
                    }
                    System.out.println("\t\t\tNumber of points : " + points.get(listName));
                } else {
                    System.out.println("\t\t\tNumber of points : 0");
                }
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
            this.startDate = DATE_FORMAT.parse(startDate);
            return this;
        }

        public Builder to(String endDate) throws ParseException {
            this.endDate = DATE_FORMAT.parse(endDate);
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
