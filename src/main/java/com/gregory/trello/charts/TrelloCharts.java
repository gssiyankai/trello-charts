package com.gregory.trello.charts;

import com.gregory.trello.charts.backlog.Backlog;
import com.gregory.trello.charts.history.History;
import com.gregory.trello.charts.sprint.Sprint;

import java.text.ParseException;
import java.util.Date;

import static com.gregory.trello.utils.DateUtils.YEAR_MONTH_DAY_DATE_FORMAT;

public final class TrelloCharts {

    public Backlog backlog() {
        return Backlog.builder()
                .withListNamed("Backlog")
                .createBacklog();
    }

    public Sprint sprint1() throws ParseException {
        return Sprint.builder()
                .of("Sprint 1")
                .from("2016-02-08")
                .to("2016-02-21")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public Sprint sprint2() throws ParseException {
        return Sprint.builder()
                .of("Sprint 2")
                .from("2016-02-22")
                .to("2016-03-06")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public History history() throws ParseException {
        return History.builder()
                .from("2016-02-08")
                .to(YEAR_MONTH_DAY_DATE_FORMAT.format(new Date()))
                .withListNamed("Backlog")
                .withListNamed("To do")
                .withListNamed("In progress")
                .withListNamed("Done")
                .createHistory();
    }

    public static void main(String[] args) throws Exception {
        TrelloCharts charts = new TrelloCharts();
        charts.backlog().printStats();
        charts.sprint1().printStats();
        charts.sprint2().printStats();
        charts.history().printStats();
    }

}
