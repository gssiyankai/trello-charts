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

public final class History {

    private final Date startDate;
    private final Date endDate;
    private final List<String> listNames;
    private final List<HistoryDay> days;

    private History(Date startDate, List<String> listNames) {
        this.startDate = startDate;
        this.endDate = now();
        this.listNames = listNames;
        this.days = new ArrayList<>();
        for (Date date : daysBetweenDates(startDate, endDate)) {
            days.add(new HistoryDay(date));
        }
    }

    public History printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* History stats *-*-*-*-*-*-*-*-*-*");
        System.out.println("Start date : " + YEAR_MONTH_DAY_DATE_FORMAT.format(startDate));
        System.out.println("End date   : " + YEAR_MONTH_DAY_DATE_FORMAT.format(endDate));
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
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public History generateCumulativeFlowDiagram() {
        String cumulativeFlowDiagramHtml = "cumulative_flow_diagram.html";
        String template = readResourceLines(cumulativeFlowDiagramHtml);
        writeToFile(
                cumulativeFlowDiagramHtml,
                template.replace("${DATA}", computeStatsData()));
        return this;
    }

    private String computeStatsData() {
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
        for (int i = 0; i < days.size(); i++) {
            HistoryDay day = days.get(i);
            data += "['" + DAY_MONTH_DATE_FORMAT.format(day.date()) + "', ";
            for (int j = listNames.size() - 1; j >= 0; j--) {
                String listName = listNames.get(j);
                TrelloCardDeck cards = day.cardsInListNamed(listName);
                data += cards.points();
                if (j > 0) {
                    data += ", ";
                }
            }
            data += "]";
            if (i < days.size() - 1) {
                data += ",\n";
            }
        }
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Date startDate;
        private List<String> listNames = new ArrayList<>();

        public Builder from(String startDate) throws ParseException {
            this.startDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(startDate);
            return this;
        }

        public Builder withListNamed(String listName) {
            this.listNames.add(listName);
            return this;
        }

        public History createHistory() {
            return new History(startDate, listNames);
        }
    }

}
