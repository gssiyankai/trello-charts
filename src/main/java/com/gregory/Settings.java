package com.gregory;

import java.io.IOException;
import java.util.Properties;

public final class Settings {

    private static final String SETTINGS_PROPERTIES = "settings.properties";
    private static final String TRELLO_KEY = "trelloKey";
    private static final String TRELLO_ACCESS_TOKEN = "trelloAccessToken";
    private static final String TRELLO_BOARD_ID = "trelloBoardId";

    private static Settings SINGLETON;

    private final String trelloKey;
    private final String trelloAccessToken;
    private final String trelloBoardId;

    private Settings() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/" + SETTINGS_PROPERTIES));
        this.trelloKey = properties.getProperty(TRELLO_KEY);
        this.trelloAccessToken = properties.getProperty(TRELLO_ACCESS_TOKEN);
        this.trelloBoardId = properties.getProperty(TRELLO_BOARD_ID);
    }

    public static Settings settings() {
        if (SINGLETON == null) {
            try {
                SINGLETON = new Settings();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return SINGLETON;
    }

    public String trelloKey() {
        return trelloKey;
    }

    public String trelloAccessToken() {
        return trelloAccessToken;
    }

    public String trelloBoardId() {
        return trelloBoardId;
    }

}
