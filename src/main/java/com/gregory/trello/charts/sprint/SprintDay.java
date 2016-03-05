package com.gregory.trello.charts.sprint;

import com.gregory.trello.model.TrelloAction;
import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gregory.trello.utils.DateUtils.dayAfter;

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

    Date date() {
        return date;
    }

    TrelloCardDeck cards() {
        List<TrelloCard> cards = new ArrayList<>();
        for (TrelloCard card : sprintCards) {
            if (card.isCreatedBefore(dayAfter(date))) {
                cards.add(card);
            }
        }
        return new TrelloCardDeck(cards);
    }

    int numberOfCards() {
        return cards().size();
    }

    int numberOfPoints() {
        return cards().points();
    }

    TrelloCardDeck completedCards() {
        return cardsInListNamed(completedListName);
    }

    int numberOfCompletedPoints() {
        return completedCards().points();
    }

    TrelloCardDeck inProgressCards() {
        return cardsInListNamed(inProgressListName);
    }

    int numberOfInProgressPoints() {
        return inProgressCards().points();
    }

    TrelloCardDeck sprintBacklogCards() {
        return cardsInListNamed(sprintBacklogListName);
    }

    int numberOfSprintBacklogPoints() {
        return sprintBacklogCards().points();
    }

    private TrelloCardDeck cardsInListNamed(String listName) {
        List<TrelloCard> cards = new ArrayList<>();
        for (TrelloCard card : cards()) {
            TrelloAction action = card.lastMoveActionBefore(dayAfter(date));
            if (action != null && action.isMoveToList(listName)) {
                cards.add(card);
            }
        }
        return new TrelloCardDeck(cards);
    }

}
