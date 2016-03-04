package com.gregory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;

import java.util.Collection;

import static com.gregory.TrelloUtils.cardsPoints;
import static com.gregory.TrelloUtils.filterCardsByLabelName;

public final class Sprint {

    private final String name;
    private final String startDate;
    private final String endDate;
    private final Collection<Card> cards;

    private Sprint(String name, String startDate, String endDate, Collection<Card> cards) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cards = cards;
    }

    public int numberOfCards() {
        return cards.size();
    }

    public int numberOfPoints() {
        return cardsPoints(cards);
    }

    public Sprint printStats() {
        System.out.println(String.format("*-*-*-*-*-*-*-*-*-* Stats for %s *-*-*-*-*-*-*-*-*-*", name));
        System.out.println("Start date : " + startDate);
        System.out.println("End date : " + endDate);
        System.out.println("Number of cards: " + numberOfCards());
        System.out.println("Number of points: " + numberOfPoints());
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
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
        private String startDate;
        private String endDate;
        private String board;
        private Collection<Card> cards;

        public Builder of(String name) {
            this.name = name;
            return this;
        }

        public Builder from(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder to(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder on(String board) {
            this.board = board;
            return this;
        }

        public Builder with(Trello trello) {
            this.cards = filterCardsByLabelName(trello.getBoardCards(board), name);
            return this;
        }

        public Sprint createSprint() {
            return new Sprint(name, startDate, endDate, cards);
        }
    }

}
