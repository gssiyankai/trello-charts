package com.gregory.trello.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.gregory.trello.model.TrelloBoard;
import com.julienvey.trello.Trello;
import com.julienvey.trello.impl.TrelloImpl;
import org.slf4j.LoggerFactory;

import static com.gregory.trello.config.Settings.settings;

public final class TrelloUtils {

    private static TrelloBoard BOARD;

    static {
        disableLogger();
        loadTrelloBoard();
    }

    private TrelloUtils() {
    }

    private static void disableLogger() {
        Logger root = (Logger) LoggerFactory.getLogger(TrelloImpl.class);
        root.setLevel(Level.OFF);
    }

    private static void loadTrelloBoard() {
        Trello trello = new TrelloImpl(settings().trelloApplicationKey(), settings().trelloAccessToken());
        String boardId = settings().trelloBoardId();
        BOARD = new TrelloBoard(trello.getBoard(boardId), trello.getBoardLists(boardId), trello.getBoardCards(boardId));
    }

    public static TrelloBoard board() {
        return BOARD;
    }

}
