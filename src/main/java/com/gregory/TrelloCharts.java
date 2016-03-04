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

    }

    public static void main(String[] args) throws Exception {
        TrelloCharts charts = new TrelloCharts();
        charts.generateBurndownChart();
    }

}
