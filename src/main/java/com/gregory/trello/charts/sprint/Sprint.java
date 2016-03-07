package com.gregory.trello.charts.sprint;

import com.gregory.trello.model.TrelloCardDeck;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gregory.trello.utils.DateUtils.*;
import static com.gregory.trello.utils.FileUtils.readResourceLines;
import static com.gregory.trello.utils.FileUtils.writeToFile;
import static com.gregory.trello.utils.TrelloUtils.board;

public final class Sprint {

    private final String name;
    private final Date startDate;
    private final Date endDate;
    private final TrelloCardDeck cards;
    private final List<SprintDay> days;

    private Sprint(String name, Date startDate, Date endDate, String sprintBacklogListName, String inProgressListName, String completedListName) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cards = board().cardsByLabelName(name);
        this.days = new ArrayList<>();
        for (Date date : daysBetweenDates(startDate, endDate)) {
            days.add(new SprintDay(date, sprintBacklogListName, inProgressListName, completedListName, cards));
        }
    }

    public Sprint printStats() {
        System.out.println(String.format("*-*-*-*-*-*-*-*-*-* Stats for %s *-*-*-*-*-*-*-*-*-*", name));
        System.out.println("Start date : " + YEAR_MONTH_DAY_DATE_FORMAT.format(startDate));
        System.out.println("End date   : " + YEAR_MONTH_DAY_DATE_FORMAT.format(endDate));
        SprintDay lastDay = days.get(days.size() - 1);
        System.out.println("Number of cards: " + lastDay.numberOfCards());
        System.out.println("Number of points: " + lastDay.numberOfPoints());
        System.out.println("\tNumber of points in progress: " + lastDay.numberOfInProgressPoints());
        System.out.println("\tNumber of completed points: " + lastDay.numberOfCompletedPoints());
        System.out.println("\tNumber of sprint backlog points: " + lastDay.numberOfSprintBacklogPoints());
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public Sprint generateBurndownChart() {
        String burndownChartHtml = "burndown_chart.html";
        String template = readResourceLines(burndownChartHtml);
        writeToFile(
                name.toLowerCase().replace(" ", "_") + "_" + burndownChartHtml,
                template.replace("${DATA}", computeStatsData())
                        .replace("${SPRINT_NAME}", name));
        return this;
    }

    private String computeStatsData() {
        String data = "";
        int sprintWorkingDays = workingDaysWithin(startDate, endDate).size();
        for (SprintDay day : days) {
            Date date = day.date();
            int points = day.numberOfPoints();
            int remainingPoints = points - day.numberOfCompletedPoints();

            int numberOfPassedWorkingDays = workingDaysWithin(startDate, date).size();
            int expectedRemainingPoints = (int) (points * (sprintWorkingDays - numberOfPassedWorkingDays) * 1. / sprintWorkingDays);

            data += "['" + DAY_MONTH_DATE_FORMAT.format(date) + "', "
                    + (date.before(NOW) ? remainingPoints : "") + ", "
                    + expectedRemainingPoints
                    + "],\n";
        }
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private Date startDate;
        private Date endDate;
        private String sprintBacklogListName;
        private String inProgressListName;
        private String completedListName;

        public Builder of(String name) {
            this.name = name;
            return this;
        }

        public Builder from(String startDate) throws ParseException {
            this.startDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(startDate);
            return this;
        }

        public Builder to(String endDate) throws ParseException {
            this.endDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(endDate);
            return this;
        }

        public Builder withSprintBacklogListNamed(String sprintBacklogListName) {
            this.sprintBacklogListName = sprintBacklogListName;
            return this;
        }

        public Builder withInProgressListNamed(String inProgressListName) {
            this.inProgressListName = inProgressListName;
            return this;
        }

        public Builder withCompletedListNamed(String completedListName) {
            this.completedListName = completedListName;
            return this;
        }

        public Sprint createSprint() {
            return new Sprint(name, startDate, endDate, sprintBacklogListName, inProgressListName, completedListName);
        }

    }

}
