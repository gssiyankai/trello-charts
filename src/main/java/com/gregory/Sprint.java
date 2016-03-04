package com.gregory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Card;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static com.gregory.TrelloUtils.*;

public final class Sprint {

    private final String name;
    private final Date startDate;
    private final Date endDate;
    private final String completedListName;
    private final String completedListId;
    private final Collection<Card> cards;

    private Sprint(String name, Date startDate, Date endDate, String completedListName, String completedListId, Collection<Card> cards) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completedListName = completedListName;
        this.completedListId = completedListId;
        this.cards = cards;
    }

    public int numberOfCards() {
        return cards.size();
    }

    public int numberOfPoints() {
        return cardsPoints(cards);
    }

    public int numberOfCompletedPoints() {
        Collection<Card> completedCards = new ArrayList<>();
        for (Card card : filterCardsByListId(cards, completedListId)) {
            for (Action action : card.getActions()) {
                if (isActionDoneWithin(action, startDate, endDate)
                    && isMoveActionToList(action, completedListName)) {
                    completedCards.add(card);
                    break;
                }
            }
        }
        return cardsPoints(completedCards);
    }

    public Sprint printStats() {
        System.out.println(String.format("*-*-*-*-*-*-*-*-*-* Stats for %s *-*-*-*-*-*-*-*-*-*", name));
        System.out.println("Start date : " + startDate);
        System.out.println("End date : " + endDate);
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
        private String board;
        private String completedListName;
        private String completedListId;
        private Collection<Card> cards;

        public Builder of(String name) {
            this.name = name;
            return this;
        }

        public Builder from(String startDate) throws ParseException {
            this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            return this;
        }

        public Builder to(String endDate) throws ParseException {
            this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            return this;
        }

        public Builder on(String board) {
            this.board = board;
            return this;
        }

        public Builder withCompletedListNamed(String completedListName) {
            this.completedListName = completedListName;
            return this;
        }

        public Builder with(Trello trello) {
            this.completedListId = filterListsByName(trello.getBoardLists(board), completedListName).iterator().next().getId();
            this.cards = filterCardsByLabelName(trello.getBoardCards(board), name);
            return this;
        }

        public Sprint createSprint() {
            return new Sprint(name, startDate, endDate, completedListName, completedListId, cards);
        }
    }

}
