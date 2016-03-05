package com.gregory.trello.charts;

import com.gregory.trello.charts.backlog.Backlog;
import com.gregory.trello.charts.history.History;
import com.gregory.trello.charts.release.DevelopmentLifecycle;
import com.gregory.trello.charts.sprint.Sprint;

import java.text.ParseException;

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
                .withSprintBacklogListNamed("To do")
                .withInProgressListNamed("In progress")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public Sprint sprint2() throws ParseException {
        return Sprint.builder()
                .of("Sprint 2")
                .from("2016-02-22")
                .to("2016-03-06")
                .withSprintBacklogListNamed("To do")
                .withInProgressListNamed("In progress")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public History history() throws ParseException {
        return History.builder()
                .from("2016-02-08")
                .withListNamed("Backlog")
                .withListNamed("To do")
                .withListNamed("In progress")
                .withListNamed("Done")
                .createHistory();
    }

    public DevelopmentLifecycle developmentLifecycle() throws ParseException {
        return DevelopmentLifecycle.builder()
                .startedAt("2016-02-08")
                .withSprintStartingEveryNDays(14)
                .withCompletedListNamed("Done")
                .createDevelopmentLifecycle();
    }

    public static void main(String[] args) throws Exception {
        TrelloCharts charts = new TrelloCharts();

        charts.backlog()
                .printStats();

        charts.sprint1()
                .printStats()
                .generateBurndownChart();

        charts.sprint2()
                .printStats()
                .generateBurndownChart();

        charts.history()
                .printStats()
                .generateCumulativeFlowDiagram();

        charts.developmentLifecycle()
                .printStats();
    }

}
