package com.gregory.trello.charts;

import com.gregory.trello.model.TrelloAction;
import com.gregory.trello.model.TrelloCard;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gregory.trello.utils.DateUtils.DATE_FORMAT;
import static com.gregory.trello.utils.TrelloUtils.board;

public final class Sprint {

    private final String name;
    private final Date startDate;
    private final Date endDate;
    private final String completedListName;

    private Sprint(String name, Date startDate, Date endDate, String completedListName) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completedListName = completedListName;
    }

    public List<TrelloCard> cards() {
        return board().cardsByLabelName(name);
    }

    public int numberOfCards() {
        return cards().size();
    }

    public int numberOfPoints() {
        int points = 0;
        for (TrelloCard card : cards()) {
            points += card.points();
        }
        return points;
    }

    public List<TrelloCard> completedCards() {
        List<TrelloCard> completedCards = cards();
        completedCards.retainAll(board().cardsByListName(completedListName));

        List<TrelloCard> sprintCompletedCards = new ArrayList<>();
        for (TrelloCard card : completedCards) {
            for (TrelloAction action : card.actions()) {
                if (action.isDoneWithin(startDate, endDate)
                        && action.isMoveToList(completedListName)) {
                    sprintCompletedCards.add(card);
                    break;
                }
            }
        }

        return sprintCompletedCards;
    }

    public int numberOfCompletedPoints() {
        int points = 0;
        for (TrelloCard card : completedCards()) {
            points += card.points();
        }
        return points;
    }

    public Sprint printStats() {
        System.out.println(String.format("*-*-*-*-*-*-*-*-*-* Stats for %s *-*-*-*-*-*-*-*-*-*", name));
        System.out.println("Start date : " + DATE_FORMAT.format(startDate));
        System.out.println("End date   : " + DATE_FORMAT.format(endDate));
        System.out.println("Number of cards: " + numberOfCards());
        System.out.println("Number of points: " + numberOfPoints());
        System.out.println("\tNumber of completed points: " + numberOfCompletedPoints());
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public Sprint generateBurndownChart() {
        //TODO
        throw new UnsupportedOperationException();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private Date startDate;
        private Date endDate;
        private String completedListName;

        public Builder of(String name) {
            this.name = name;
            return this;
        }

        public Builder from(String startDate) throws ParseException {
            this.startDate = DATE_FORMAT.parse(startDate);
            return this;
        }

        public Builder to(String endDate) throws ParseException {
            this.endDate = DATE_FORMAT.parse(endDate);
            return this;
        }

        public Builder withCompletedListNamed(String completedListName) {
            this.completedListName = completedListName;
            return this;
        }

        public Sprint createSprint() {
            return new Sprint(name, startDate, endDate, completedListName);
        }

    }

}
