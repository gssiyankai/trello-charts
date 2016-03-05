package com.gregory.trello.model;

import com.julienvey.trello.domain.TList;

public final class TrelloList {

    private final TList list;

    public TrelloList(TList list) {
        this.list = list;
    }

    public String name() {
        return list.getName();
    }

    public String id() {
        return list.getId();
    }

}
