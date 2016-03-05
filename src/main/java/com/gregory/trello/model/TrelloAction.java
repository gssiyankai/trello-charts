package com.gregory.trello.model;

import com.julienvey.trello.domain.Action;

import java.util.Date;

public final class TrelloAction {

    private final Action action;

    public TrelloAction(Action action) {
        this.action = action;
    }

    public boolean isDoneWithin(Date start, Date end) {
        return isDoneAfter(start) && isDoneBefore(end);
    }

    public boolean isDoneAfter(Date date) {
        return action.getDate().compareTo(date) >= 0;
    }

    public boolean isDoneBefore(Date date) {
        return action.getDate().compareTo(date) <= 0;
    }

    public boolean isMove() {
        return "updateCard".equals(action.getType());
    }

    public boolean isMoveToList(String toList) {
        return isMove() && toList.equals(action.getData().getListAfter().getName());
    }

    public String afterListId() {
        return action.getData().getListAfter().getId();
    }

    public String beforeListId() {
        return action.getData().getListBefore().getId();
    }
}
