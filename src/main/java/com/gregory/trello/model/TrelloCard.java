package com.gregory.trello.model;

import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class TrelloCard {

    public static final String POINTS_PATTERN = "^\\w*\\((\\d+)\\).*$";
    private final Card card;
    private final List<TrelloAction> actions;
    private final List<TrelloLabel> labels;

    public TrelloCard(Card card) {
        this.card = card;
        this.actions = new ArrayList<>();
        for (Action action : card.getActions()) {
            actions.add(new TrelloAction(action));
        }
        this.labels = new ArrayList<>();
        for (Label label : card.getLabels()) {
            labels.add(new TrelloLabel(label));
        }
    }

    public TrelloAction lastMoveActionBefore(Date date) {
        for (TrelloAction action : actions) {
            if (action.isMove() && action.isDoneBefore(date)) {
                return action;
            }
        }
        return null;
    }

    public TrelloAction firstMoveActionAfter(Date date) {
        for (int i = actions.size() - 1; i >= 0; i--) {
            TrelloAction action = actions.get(i);
            if (action.isMove() && action.isDoneAfter(date)) {
                return action;
            }
        }
        return null;
    }

    public List<TrelloLabel> labels() {
        return labels;
    }

    public String listId() {
        return card.getIdList();
    }

    public int points() {
        int points = 0;
        if(isEstimated()) {
            points = Integer.parseInt(title().replaceFirst(POINTS_PATTERN, "$1"));
        }
        return points;
    }

    public String id() {
        return card.getId();
    }

    public boolean isEstimated() {
        return title().matches(POINTS_PATTERN);
    }

    public boolean isCreatedBefore(Date date) {
        return createdAt().compareTo(date) <= 0;
    }

    public Date createdAt() {
        return new Date(Long.parseLong(id().substring(0, 8), 16) * 1000);
    }

    public String listIdAt(Date date) {
        if (actions.isEmpty()) {
            return card.getIdList();
        } else {
            TrelloAction action = lastMoveActionBefore(date);
            if (action != null) {
                return action.afterListId();
            }
            action = firstMoveActionAfter(date);
            if (action != null) {
                return action.beforeListId();
            }
            throw new UnsupportedOperationException();
        }
    }

    public String title() {
        return card.getName();
    }

    public String url() {
        return card.getUrl();
    }

}
