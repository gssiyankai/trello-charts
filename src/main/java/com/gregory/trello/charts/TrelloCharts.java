package com.gregory.trello.charts;

import com.gregory.trello.charts.backlog.Backlog;
import com.gregory.trello.charts.history.History;
import com.gregory.trello.charts.release.LifeCycle;
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

    public Sprint sprint3() throws ParseException {
        return Sprint.builder()
                .of("Sprint 3")
                .from("2016-03-07")
                .to("2016-03-20")
                .withSprintBacklogListNamed("To do")
                .withInProgressListNamed("In progress")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public Sprint sprint4() throws ParseException {
        return Sprint.builder()
                .of("Sprint 4")
                .from("2016-03-21")
                .to("2016-04-03")
                .withSprintBacklogListNamed("To do")
                .withInProgressListNamed("In progress")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public Sprint sprint5() throws ParseException {
        return Sprint.builder()
                .of("Sprint 5")
                .from("2016-04-04")
                .to("2016-04-17")
                .withSprintBacklogListNamed("To do")
                .withInProgressListNamed("In progress")
                .withCompletedListNamed("Done")
                .createSprint();
    }

    public Sprint sprint6() throws ParseException {
        return Sprint.builder()
                .of("Sprint 6")
                .from("2016-04-18")
                .to("2016-05-01")
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
                .withInProgressListNamed("In progress")
                .withCompletedListNamed("Done")
                .createHistory();
    }

    public LifeCycle lifeCycle() throws ParseException {
        return LifeCycle.builder()
                .startedAt("2016-02-08")
                .withSprintStartingEveryNDays(14)
                .withCompletedListNamed("Done")
                .createLifeCycle();
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

        charts.sprint3()
                .printStats()
                .generateBurndownChart();

        charts.sprint4()
                .printStats()
                .generateBurndownChart();

        charts.sprint5()
                .printStats()
                .generateBurndownChart();

        charts.sprint6()
                .printStats()
                .generateBurndownChart();

        charts.history()
                .printStats()
                .generateCumulativeFlowDiagram()
                .generateCycleTimeChart();

        charts.lifeCycle()
                .printStats()
                .generateBurnupChart();

    }

}
