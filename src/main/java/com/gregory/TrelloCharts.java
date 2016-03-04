package com.gregory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.impl.TrelloImpl;

import java.io.IOException;
import java.text.ParseException;

import static com.gregory.Settings.settings;

public final class TrelloCharts {

    public Backlog backlog() throws IOException {
        return Backlog.builder()
                .withListNamed("Backlog")
                .createBacklog();
    }

    public Sprint sprint1() throws IOException, ParseException {
        return Sprint.builder()
                .of("Sprint 1")
                .from("2016-02-08")
                .to("2016-02-21")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public Sprint sprint2() throws IOException, ParseException {
        return Sprint.builder()
                .of("Sprint 2")
                .from("2016-02-22")
                .to("2016-03-06")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public static void main(String[] args) throws Exception {
        TrelloCharts charts = new TrelloCharts();
        charts.backlog().printStats();
        charts.sprint1().printStats();
        charts.sprint2().printStats();
    }

}
