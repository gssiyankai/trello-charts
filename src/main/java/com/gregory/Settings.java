package com.gregory;

import java.io.IOException;
import java.util.Properties;

public final class Settings {

    private static final String TRELLO_PROPERTIES = "trello.properties";
    private static final String TRELLO_APPLICATION_KEY = "trelloApplicationKey";
    private static final String TRELLO_ACCESS_TOKEN = "trelloAccessToken";
    private static final String TRELLO_BOARD_ID = "trelloBoardId";

    private static Settings SINGLETON;

    private final Properties properties;

    private Settings() throws IOException {
        this.properties = new Properties();
        this.properties.load(this.getClass().getResourceAsStream("/" + TRELLO_PROPERTIES));
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

    public String trelloApplicationKey() {
        return property(TRELLO_APPLICATION_KEY);
    }

    public String trelloAccessToken() {
        return property(TRELLO_ACCESS_TOKEN);
    }

    public String trelloBoardId() {
        return property(TRELLO_BOARD_ID);
    }

    private String property(String key) {
        String property = properties.getProperty(key);
        if (property == null || property.isEmpty()) {
            throw new RuntimeException("Missing property " + key);
        }
        return property;
    }

}
