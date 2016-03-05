package com.gregory.trello.charts.backlog;

import static com.gregory.trello.utils.TrelloUtils.board;

public final class Backlog {

    private final String listName;

    private Backlog(String listName) {
        this.listName = listName;
    }

    public int numberOfPoints() {
        return board()
                .cardsByListName(listName)
                .points();
    }

    public Backlog printStats() {
        System.out.println("*-*-*-*-*-*-*-*-*-* Stats for backlog *-*-*-*-*-*-*-*-*-*");
        System.out.println("Number of points: " + numberOfPoints());
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String listName;

        public Builder withListNamed(String listName) {
            this.listName = listName;
            return this;
        }

        public Backlog createBacklog() {
            return new Backlog(listName);
        }
    }

}
