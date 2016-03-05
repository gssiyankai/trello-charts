package com.gregory.trello.charts.history;

import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

import static com.gregory.trello.utils.DateUtils.*;

public final class History {

    private final Date startDate;
    private final Date endDate;
    private final List<String> listNames;
    private final List<Day> days;

    private History(Date startDate, Date endDate, List<String> listNames) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.listNames = listNames;
        this.days = new ArrayList<>();
        for (Date date : daysBetweenDates(startDate, endDate)) {
            days.add(new Day(date));
        }
    }

    public History printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* History stats *-*-*-*-*-*-*-*-*-*");
        System.out.println("Start date : " + YEAR_MONTH_DAY_DATE_FORMAT.format(startDate));
        System.out.println("End date   : " + YEAR_MONTH_DAY_DATE_FORMAT.format(endDate));
        for (Day day : days) {
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

    public void generateFlowDiagram() throws IOException, URISyntaxException {
        String cumulative_flow_diagram_html = "cumulative_flow_diagram.html";
        Path templatePath = Paths.get(this.getClass().getResource("/" + cumulative_flow_diagram_html).toURI());
        List<String> lines = Files.readAllLines(templatePath, Charset.defaultCharset());
        String statsData = computeStatsData();
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cumulative_flow_diagram_html)))) {
            for (String line : lines) {
                writer.append(line.replaceAll("\\$\\{DATA\\}", statsData) + "\n");
            }
        }
    }

    private String computeStatsData() {
        String data = "";
        data += "['Day', ";
        for (int i = 0; i < listNames.size(); i++) {
            String listName = listNames.get(i);
            data += "'" + listName + "'";
            if (i < listNames.size() - 1) {
                data += ", ";
            }
        }
        data += "],";
        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            data += "['" + DAY_MONTH_DATE_FORMAT.format(day.date()) + "', ";
            for (int j = 0; j < listNames.size(); j++) {
                String listName = listNames.get(j);
                TrelloCardDeck cards = day.cardsInListNamed(listName);
                data += cards.points();
                if (j < listNames.size() - 1) {
                    data += ", ";
                }
            }
            data += "]";
            if (i < days.size() - 1) {
                data += ", \n";
            }
        }
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Date startDate;
        private Date endDate;
        private LinkedList<String> listNames = new LinkedList<>();

        public Builder from(String startDate) throws ParseException {
            this.startDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(startDate);
            return this;
        }

        public Builder to(String endDate) throws ParseException {
            this.endDate = YEAR_MONTH_DAY_DATE_FORMAT.parse(endDate);
            return this;
        }

        public Builder withListNamed(String listName) {
            this.listNames.push(listName);
            return this;
        }

        public History createHistory() {
            return new History(startDate, endDate, listNames);
        }
    }

}
