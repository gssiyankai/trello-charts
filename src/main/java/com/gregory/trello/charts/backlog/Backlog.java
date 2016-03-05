package com.gregory.trello.charts.backlog;

import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.util.ArrayList;
import java.util.List;

import static com.gregory.trello.utils.TrelloUtils.board;

public final class Backlog {

    private final String listName;

    private Backlog(String listName) {
        this.listName = listName;
    }

    public TrelloCardDeck cards() {
        return board().cardsByListName(listName);
    }

    public int numberOfPoints() {
        return cards().points();
    }

    public int numberOfCards() {
        return cards().size();
    }

    public TrelloCardDeck estimatedCards() {
        List<TrelloCard> estimatedCards = new ArrayList<>();
        for (TrelloCard card : cards()) {
            if(card.isEstimated()) {
                estimatedCards.add(card);
            }
        }
        return new TrelloCardDeck(estimatedCards);
    }

    public int numberOfEstimatedCards() {
        return estimatedCards().size();
    }

    public double percentageOfEstimatedCards() {
        return numberOfEstimatedCards() * 100. / numberOfCards();
    }

    public Backlog printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* Stats for backlog *-*-*-*-*-*-*-*-*-*");
        System.out.println("Number of points: " + numberOfPoints());
        System.out.println("Percentage of cards estimated: " + percentageOfEstimatedCards());
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
            return new Backlog(listName);
        }
    }

}
