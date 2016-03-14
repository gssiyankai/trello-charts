package com.gregory.trello.charts.release;

import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gregory.trello.utils.DateUtils.*;
import static com.gregory.trello.utils.FileUtils.readResourceLines;
import static com.gregory.trello.utils.FileUtils.writeToFile;
import static com.gregory.trello.utils.TrelloUtils.board;

public final class LifeCycle {

    private final Date startDate;
    private final int sprintDurationInDays;
    private final String completedListName;
    private final List<Cycle> cycles;

    private LifeCycle(Date startDate, int sprintDurationInDays, String completedListName) {
        this.startDate = startDate;
        this.sprintDurationInDays = sprintDurationInDays;
        this.completedListName = completedListName;
        this.cycles = new ArrayList<>();
        Date current = startDate;
        while (current.compareTo(NOW) <= 0) {
            cycles.add(new Cycle(current, completedListName));
            current = addDays(current, sprintDurationInDays);
        }
        cycles.add(new Cycle(current, completedListName));
    }

    public int numberOfPassedSprints() {
        return cycles.size() - 1;
    }

    public int numberOfCompletedPoints() {
        Cycle lastCycle = cycles.get(numberOfPassedSprints());
        return lastCycle.numberOfCompletedPoints();
    }

    public int velocity() {
        return (int) (Math.floor(numberOfCompletedPoints() * 1.0 / numberOfPassedSprints()));
    }

    public int numberOfSprintsToComplete() {
        return (int) (Math.ceil((numberOfPoints() - numberOfCompletedPoints()) * 1. / velocity()));
    }

    public int numberOfCards() {
        return board().cards().size();
    }

    public int numberOfPoints() {
        return board().cards().points();
    }

    public TrelloCardDeck estimatedCards() {
        List<TrelloCard> estimatedCards = new ArrayList<>();
        for (TrelloCard card : board().cards()) {
            if (card.isEstimated()) {
                estimatedCards.add(card);
            }
        }
        return new TrelloCardDeck(estimatedCards);
    }

    public int numberOfEstimatedCards() {
        return estimatedCards().size();
    }

    public double percentageOfEstimatedCards() {
        return numberOfEstimatedCards() * 100. / numberOfCards();
    }

    public LifeCycle printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* Stats for backlog *-*-*-*-*-*-*-*-*-*");
        System.out.println("Number of points: " + numberOfPoints());
        System.out.println("Number of passed sprints: " + numberOfPassedSprints());
        System.out.println("Number of completed points: " + numberOfCompletedPoints());
        System.out.println("Velocity: " + velocity());
        System.out.println("Number of sprints to complete: " + numberOfSprintsToComplete());
        System.out.println("Percentage of cards estimated: " + percentageOfEstimatedCards());
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public LifeCycle generateBurnupChart() {
        String burnupChartHtml = "burnup_chart.html";
        String template = readResourceLines(burnupChartHtml);
        writeToFile(
                burnupChartHtml,
                template.replace("${DATA}", computeStatsData())
                        .replace("${BACKLOG_POINTS}", "" + numberOfPoints())
                        .replace("${VELOCITY}", "" + velocity())
                        .replace("${SPRINTS_TO_COMPLETE}", "" + numberOfSprintsToComplete())
                        .replace("${ESTIMATED_CARDS_PERCENTAGE}", "" + percentageOfEstimatedCards()));
        return this;
    }

    private String computeStatsData() {
        int completedPoints = 0;
        String data = "";
        for (Cycle cycle : cycles) {
            data += "['" + DAY_MONTH_YEAR_DATE_FORMAT.format(cycle.endDate()) + "',"
                    + cycle.numberOfPoints() + ","
                    + cycle.numberOfCompletedPoints() + ","
                    + "true"
                    + "],\n";
            completedPoints = cycle.numberOfCompletedPoints();
        }
        for (int i = 1; i < numberOfSprintsToComplete(); i++) {
            completedPoints += velocity();
            data += "['" + DAY_MONTH_YEAR_DATE_FORMAT.format(addDays(NOW, sprintDurationInDays * i)) + "',"
                    + numberOfPoints() + ","
                    + completedPoints + ","
                    + "false"
                    + "],\n";
        }
        if (completedPoints < numberOfPoints()) {
            data += "['" + DAY_MONTH_YEAR_DATE_FORMAT.format(addDays(NOW, sprintDurationInDays * numberOfSprintsToComplete())) + "',"
                    + numberOfPoints() + ","
                    + numberOfPoints() + ","
                    + "false"
                    + "],\n";
        }
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Date startDate;
        private int sprintDurationInDays;
        private String completedListName;

        public Builder startedAt(String startDate) throws ParseException {
            this.startDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(startDate);
            return this;
        }

        public Builder withSprintStartingEveryNDays(int sprintDurationInDays) {
            this.sprintDurationInDays = sprintDurationInDays;
            return this;
        }

        public Builder withCompletedListNamed(String completedListName) {
            this.completedListName = completedListName;
            return this;
        }

        public LifeCycle createLifeCycle() {
            return new LifeCycle(startDate, sprintDurationInDays, completedListName);
        }
    }

}
