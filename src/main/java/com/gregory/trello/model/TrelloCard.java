package com.gregory.trello.model;

import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class TrelloCard {

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

    public List<TrelloAction> actions() {
        return actions;
    }

    public List<TrelloLabel> labels() {
        return labels;
    }

    public String listId() {
        return card.getIdList();
    }

    public int points() {
        return Integer.parseInt(title().replaceFirst("\\((\\d+)\\).*", "$1"));
    }

    public String id() {
        return card.getId();
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
            for (TrelloAction action : actions) {
                if (action.isUpdate() && action.isDoneBefore(date)) {
                    return action.afterListId();
                }
            }
            for (int i = actions.size() - 1; i >= 0; i--) {
                TrelloAction action = actions.get(i);
                if (action.isUpdate()) {
                    return action.beforeListId();
                }
            }
            throw new UnsupportedOperationException();
        }
    }

    public String title() {
        return card.getName();
    }
}
