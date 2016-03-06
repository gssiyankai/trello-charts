package com.gregory.trello.charts.history;

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

public final class History {

    private final Date startDate;
    private final List<String> listNames;
    private final List<HistoryDay> days;
    private final List<HistoryCard> cards;

    private History(Date startDate, String inProgressListName, String completedListName, List<String> listNames) {
        this.startDate = startDate;
        this.listNames = listNames;
        this.days = new ArrayList<>();
        for (Date date : daysBetweenDates(startDate, NOW)) {
            this.days.add(new HistoryDay(date));
        }
        this.cards = new ArrayList<>();
        for (TrelloCard card : board().cardsAt(NOW)) {
            this.cards.add(new HistoryCard(card, inProgressListName, completedListName));
        }
    }

    public List<HistoryCard> completedCards() {
        List<HistoryCard> completedCards = new ArrayList<>();
        for (HistoryCard card : cards) {
            if(card.isComplete()) {
                completedCards.add(card);
            }
        }
        return completedCards;
    }

    public History printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* History stats *-*-*-*-*-*-*-*-*-*");
        System.out.println("Start date : " + YEAR_MONTH_DAY_DATE_FORMAT.format(startDate));
        System.out.println("End date   : " + YEAR_MONTH_DAY_DATE_FORMAT.format(NOW));
        for (HistoryDay day : days) {
            System.out.println("\t@" + YEAR_MONTH_DAY_DATE_FORMAT.format(day.date()));
            for (String listName : listNames) {
                System.out.println("\t\t" + listName + " :");
                System.out.println("\t\t\tcards : ");
                TrelloCardDeck cards = day.cardsInListNamed(listName);
                for (TrelloCard card : cards) {
                    System.out.println("\t\t\t\t" + card.title() + " " + card.url());
                }
                System.out.println("\t\t\tNumber of points : " + cards.points());
            }
        }
        System.out.println("Cycle time :");
        for (HistoryCard card : completedCards()) {
            System.out.println("\t@" + YEAR_MONTH_DAY_DATE_FORMAT.format(card.completedAt()) + " - " + card.cycleTime() + " : " + card.title());
        }
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public History generateCumulativeFlowDiagram() {
        String cumulativeFlowDiagramHtml = "cumulative_flow_diagram.html";
        String template = readResourceLines(cumulativeFlowDiagramHtml);
        writeToFile(
                cumulativeFlowDiagramHtml,
                template.replace("${DATA}", computeHistoryStatsData()));
        return this;
    }

    public History generateCycleTimeChart() {
        String cycleTimeChartHtml = "cycle_time_chart.html";
        String template = readResourceLines(cycleTimeChartHtml);
        writeToFile(
                cycleTimeChartHtml,
                template.replace("${DATA}", computeCycleTimeStatsData()));
        return this;
    }

    private String computeCycleTimeStatsData() {
        String data = "";
        List<HistoryCard> completedCards = completedCards();
        for (int i = completedCards.size() - 1; i >= 0; i--) {
            HistoryCard card = completedCards.get(i);
            Date date = card.completedAt();
            data += "[" + String.format("new Date(%d,%d,%d)", year(date), month(date), dayOfMonth(date)) + ", "
                  + card.cycleTime()
                  + "],\n";
        }
        return data;
    }

    private String computeHistoryStatsData() {
        String data = "";
        data += "['Day', ";
        for (int i = listNames.size() - 1; i >= 0; i--) {
            String listName = listNames.get(i);
            data += "'" + listName + "'";
            if (i > 0) {
                data += ", ";
            }
        }
        data += "],\n";
        for (HistoryDay day : days) {
            data += "['" + DAY_MONTH_DATE_FORMAT.format(day.date()) + "', ";
            for (int j = listNames.size() - 1; j >= 0; j--) {
                String listName = listNames.get(j);
                TrelloCardDeck cards = day.cardsInListNamed(listName);
                data += cards.points();
                if (j > 0) {
                    data += ", ";
                }
            }
            data += "],\n";
        }
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Date startDate;
        private String completedListName;
        private String inProgressListName;
        private List<String> listNames = new ArrayList<>();

        public Builder from(String startDate) throws ParseException {
            this.startDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(startDate);
            return this;
        }

        public Builder withInProgressListNamed(String inProgressListName) {
            this.inProgressListName = inProgressListName;
            return withListNamed(inProgressListName);
        }

        public Builder withCompletedListNamed(String completedListName) {
            this.completedListName = completedListName;
            return withListNamed(completedListName);
        }

        public Builder withListNamed(String listName) {
            this.listNames.add(listName);
            return this;
        }

        public History createHistory() {
            return new History(startDate, inProgressListName, completedListName, listNames);
        }
    }

}
