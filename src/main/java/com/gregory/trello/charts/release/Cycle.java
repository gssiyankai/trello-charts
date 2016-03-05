package com.gregory.trello.charts.release;

import com.gregory.trello.model.TrelloAction;
import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gregory.trello.utils.DateUtils.dayAfter;
import static com.gregory.trello.utils.TrelloUtils.board;

final class Cycle {

    private final Date endDate;
    private final String completedListName;

    Cycle(Date endDate, String completedListName) {
        this.endDate = endDate;
        this.completedListName = completedListName;
    }

    Date endDate() {
        return endDate;
    }

    TrelloCardDeck completedCards() {
        List<TrelloCard> cards = new ArrayList<>();
        for (TrelloCard card : cards()) {
            TrelloAction action = card.lastMoveActionBefore(dayAfter(endDate));
            if (action != null && action.isMoveToList(completedListName)) {
                cards.add(card);
            }
        }
        return new TrelloCardDeck(cards);
    }

    int numberOfCompletedPoints() {
        return completedCards().points();
    }

    TrelloCardDeck cards() {
        return board().cardsAt(dayAfter(endDate));
    }

    int numberOfPoints() {
        return cards().points();
    }

}
