package com.gregory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.impl.TrelloImpl;

import java.io.IOException;

import static com.gregory.Settings.settings;

public final class TrelloCharts {

    private final Trello trello;

    public TrelloCharts() throws IOException {
        trello = new TrelloImpl(settings().trelloKey(), settings().trelloAccessToken());
    }

    public void generateBurndownChart() throws IOException {
        Sprint sprint2 = Sprint.builder()
                .of("Sprint 2")
                .from("2016-02-22")
                .to("2016-03-04")
                .on(settings().trelloBoardId())
                .with(trello)
                .createSprint();

        System.out.println(sprint2.cards().size());
        for (Card card : sprint2.cards()) {
            System.out.println(card.getName());
        }
    }

    public static void main(String[] args) throws Exception {
        TrelloCharts charts = new TrelloCharts();
        charts.generateBurndownChart();
    }

}
