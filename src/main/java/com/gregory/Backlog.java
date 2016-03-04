package com.gregory;

import com.julienvey.trello.domain.Card;

import java.util.Collection;

import static com.gregory.TrelloUtils.cardsByListName;
import static com.gregory.TrelloUtils.cardsPoints;

public final class Backlog {

    private final Collection<Card> cards;

    private Backlog(Collection<Card> cards) {
        this.cards = cards;
    }

    public int numberOfPoints() {
        return cardsPoints(cards);
    }

    public Backlog printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* Stats for backlog *-*-*-*-*-*-*-*-*-*");
        System.out.println("Number of points: " + numberOfPoints());
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String listName;

        public Builder withListNamed(String listName) {
            this.listName = listName;
            return this;
        }

        public Backlog createBacklog() {
            Collection<Card> cards = cardsByListName(listName);
            return new Backlog(cards);
        }
    }

}
