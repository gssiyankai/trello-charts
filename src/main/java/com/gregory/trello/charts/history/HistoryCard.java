package com.gregory.trello.charts.history;

import com.gregory.trello.model.TrelloAction;
import com.gregory.trello.model.TrelloCard;

import java.util.Date;
import java.util.List;

import static com.gregory.trello.utils.DateUtils.NOW;
import static com.gregory.trello.utils.DateUtils.numberOfDaysBetweenDates;
import static com.gregory.trello.utils.TrelloUtils.board;

final class HistoryCard {

    private final TrelloCard card;
    private final String inProgressListName;
    private final String completedListName;

    HistoryCard(TrelloCard card, String inProgressListName, String completedListName) {
        this.card = card;
        this.inProgressListName = inProgressListName;
        this.completedListName = completedListName;
    }

    boolean wasInProgess() {
        TrelloAction inProgressAction = inProgressAction();
        return inProgressAction != null || board().cardsByListName(inProgressListName).contains(card);
    }

    private TrelloAction inProgressAction() {
        List<TrelloAction> moveActions = card.moveActions();
        for (int i = moveActions.size() - 1; i >= 0; i--) {
            TrelloAction action = moveActions.get(i);
            if (action.isMoveToList(inProgressListName)) {
                return action;
            }
        }
        return null;
    }

    Date inProgressAt() {
        if (wasInProgess()) {
            TrelloAction inProgressAction = inProgressAction();
            if (inProgressAction != null) {
                return inProgressAction.date();
            } else {
                return card.createdAt();
            }
        }
        return null;
    }

    boolean isComplete() {
        TrelloAction completeAction = completeAction();
        return completeAction != null || board().cardsByListName(completedListName).contains(card);
    }

    TrelloAction completeAction() {
        TrelloAction lastMoveAction = card.lastMoveActionBefore(NOW);
        if (lastMoveAction != null && lastMoveAction.isMoveToList(completedListName)) {
            return lastMoveAction;
        }
        return null;
    }

    Date completedAt() {
        if (isComplete()) {
            TrelloAction completeAction = completeAction();
            if (completeAction != null) {
                return completeAction.date();
            } else {
                return card.createdAt();
            }
        }
        return null;
    }

    int cycleTime() {
        if (isComplete()) {
            if (wasInProgess()) {
                return numberOfDaysBetweenDates(inProgressAt(), completedAt()) + 1;
            } else {
                return 0;
            }
        }
        return -1;
    }

    public String title() {
        return card.title();
    }
}
