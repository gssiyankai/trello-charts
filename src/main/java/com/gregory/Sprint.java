package com.gregory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;

import java.util.ArrayList;
import java.util.Collection;

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

    public String name() {
        return name;
    }

    public String startDate() {
        return startDate;
    }

    public String endDate() {
        return endDate;
    }

    public Collection<Card> cards() {
        return cards;
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
            Collection<Card> cards = new ArrayList<>();
            for (Card card : trello.getBoardCards(board)) {
                for (Label label : card.getLabels()) {
                    String labelName = label.getName();
                    if (label != null && labelName.equals(name)) {
                        cards.add(card);
                    }
                }
            }
            this.cards = cards;
            return this;
        }

        public Sprint createSprint() {
            return new Sprint(name, startDate, endDate, cards);
        }
    }

}
