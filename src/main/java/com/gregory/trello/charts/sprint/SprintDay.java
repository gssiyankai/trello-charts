package com.gregory.trello.charts.sprint;

import com.gregory.trello.model.TrelloAction;
import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

final class SprintDay {

    private final Date date;
    private final String sprintBacklogListName;
    private final String inProgressListName;
    private final String completedListName;
    private final TrelloCardDeck sprintCards;

    SprintDay(Date date, String sprintBacklogListName, String inProgressListName, String completedListName, TrelloCardDeck sprintCards) {
        this.date = date;
        this.sprintBacklogListName = sprintBacklogListName;
        this.inProgressListName = inProgressListName;
        this.completedListName = completedListName;
        this.sprintCards = sprintCards;
    }

    public TrelloCardDeck cards() {
        List<TrelloCard> cards = new ArrayList<>();
        for (TrelloCard card : sprintCards) {
            if (card.isCreatedBefore(date)) {
                cards.add(card);
            }
        }
        return new TrelloCardDeck(cards);
    }

    public int numberOfCards() {
        return cards().size();
    }

    public int numberOfPoints() {
        return cards().points();
    }

    public TrelloCardDeck completedCards() {
        return cardsInListNamed(completedListName);
    }

    public int numberOfCompletedPoints() {
        return completedCards().points();
    }

    public TrelloCardDeck inProgressCards() {
        return cardsInListNamed(inProgressListName);
    }

    public int numberOfInProgressPoints() {
        return inProgressCards().points();
    }

    public TrelloCardDeck sprintBacklogCards() {
        return cardsInListNamed(sprintBacklogListName);
    }

    public int numberOfSprintBacklogPoints() {
        return sprintBacklogCards().points();
    }

    private TrelloCardDeck cardsInListNamed(String listName) {
        List<TrelloCard> cards = new ArrayList<>();
        for (TrelloCard card : cards()) {
            TrelloAction action = card.lastMoveActionBefore(date);
            if (action != null && action.isMoveToList(listName)) {
                cards.add(card);
            }
        }
        return new TrelloCardDeck(cards);
    }

}
