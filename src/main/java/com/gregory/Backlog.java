package com.gregory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.TList;

import java.util.ArrayList;
import java.util.Collection;

import static com.gregory.TrelloUtils.*;

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

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private String board;
        private String listName;
        private Collection<Card> cards;

        public Builder on(String board) {
            this.board = board;
            return this;
        }

        public Builder withListNamed(String listName) {
            this.listName = listName;
            return this;
        }

        public Builder with(Trello trello) {
            this.cards = new ArrayList<>();
            for (TList list : filterListsByName(trello.getBoardLists(board), listName)) {
                this.cards.addAll(filterCardsByListId(trello.getBoardCards(board), list.getId()));
            }
            return this;
        }

        public Backlog createBacklog() {
            return new Backlog(cards);
        }
    }

}
