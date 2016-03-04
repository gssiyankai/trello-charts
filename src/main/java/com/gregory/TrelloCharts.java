package com.gregory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.TrelloImpl;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public final class TrelloCharts {

    private static final String SETTINGS_PROPERTIES = "settings.properties";
    private static final String TRELLO_KEY = "trelloKey";
    private static final String TRELLO_ACCESS_TOKEN = "trelloAccessToken";
    private static final String TRELLO_BOARD_ID = "trelloBoardId";

    private final String trelloKey;
    private final String trelloAccessToken;
    private final String trelloBoardId;

    public TrelloCharts() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/" + SETTINGS_PROPERTIES));
        this.trelloKey = properties.getProperty(TRELLO_KEY);
        this.trelloAccessToken = properties.getProperty(TRELLO_ACCESS_TOKEN);
        this.trelloBoardId = properties.getProperty(TRELLO_BOARD_ID);
    }

    public void generateBurndownChart() {
        Trello trelloApi = new TrelloImpl(trelloKey, trelloAccessToken);
        Board board = trelloApi.getBoard(trelloBoardId);
        List<TList> lists = board.fetchLists();
        System.out.println(lists);
    }

    public static void main(String[] args) throws Exception {
        TrelloCharts charts = new TrelloCharts();
        charts.generateBurndownChart();
    }

}
