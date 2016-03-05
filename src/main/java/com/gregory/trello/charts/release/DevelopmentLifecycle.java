package com.gregory.trello.charts.release;

import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gregory.trello.utils.DateUtils.*;
import static com.gregory.trello.utils.TrelloUtils.board;

public final class DevelopmentLifecycle {

    private final Date startDate;
    private final Date now;
    private final int sprintDurationInDays;
    private final String completedListName;

    private DevelopmentLifecycle(Date startDate, int sprintDurationInDays, String completedListName) {
        this.startDate = startDate;
        this.now = now();
        this.sprintDurationInDays = sprintDurationInDays;
        this.completedListName = completedListName;
    }

    public double numberOfPassedSprints() {
        return daysBetweenDates(startDate, now).size() * 1. / sprintDurationInDays;
    }

    public TrelloCardDeck completedCards() {
        return board().cardsByListName(completedListName);
    }

    public int numberOfCompletedPoints() {
        return completedCards().points();
    }

    public int velocity() {
        return (int) (numberOfCompletedPoints() / numberOfPassedSprints());
    }

    public int numberOfSprintsToComplete() {
        return (numberOfPoints() - numberOfCompletedPoints()) / velocity();
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

    public DevelopmentLifecycle printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* Stats for development lifecycle *-*-*-*-*-*-*-*-*-*");
        System.out.println("Number of points: " + numberOfPoints());
        System.out.println("Number of passed sprints: " + numberOfPassedSprints());
        System.out.println("Number of completed points: " + numberOfCompletedPoints());
        System.out.println("Velocity: " + velocity());
        System.out.println("Number of sprints to complete: " + numberOfSprintsToComplete());
        System.out.println("Percentage of cards estimated: " + percentageOfEstimatedCards());
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
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

        public DevelopmentLifecycle createDevelopmentLifecycle() {
            return new DevelopmentLifecycle(startDate, sprintDurationInDays, completedListName);
        }
    }

}
