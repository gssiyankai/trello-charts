package com.gregory.trello.model;

import com.julienvey.trello.domain.Label;

public final class TrelloLabel {

    private final Label label;

    public TrelloLabel(Label label) {
        this.label = label;
    }

    public String name() {
        return label.getName();
    }

}
