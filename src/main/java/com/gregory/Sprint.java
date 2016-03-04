package com.gregory;

import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Card;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static com.gregory.TrelloUtils.*;

public final class Sprint {

    private final String name;
    private final Date startDate;
    private final Date endDate;
    private final Collection<Card> completedCards;
    private final Collection<Card> cards;

    private Sprint(String name, Date startDate, Date endDate, Collection<Card> completedCards, Collection<Card> cards) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completedCards = completedCards;
        this.cards = cards;
    }

    public int numberOfCards() {
        return cards.size();
    }

    public int numberOfPoints() {
        return cardsPoints(cards);
    }

    public int numberOfCompletedPoints() {
        return cardsPoints(completedCards);
    }

    public Sprint printStats() {
        System.out.println(String.format("*-*-*-*-*-*-*-*-*-* Stats for %s *-*-*-*-*-*-*-*-*-*", name));
        System.out.println("Start date : " + startDate);
        System.out.println("End date : " + endDate);
        System.out.println("Number of cards: " + numberOfCards());
        System.out.println("Number of points: " + numberOfPoints());
        System.out.println("\tNumber of completed points: " + numberOfCompletedPoints());
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        return this;
    }

    public Sprint generateBurndownChart() {
        //TODO
        throw new UnsupportedOperationException();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private Date startDate;
        private Date endDate;
        private String completedList;

        public Builder of(String name) {
            this.name = name;
            return this;
        }

        public Builder from(String startDate) throws ParseException {
            this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            return this;
        }

        public Builder to(String endDate) throws ParseException {
            this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            return this;
        }

        public Builder withCompletedListNamed(String completedList) {
            this.completedList = completedList;
            return this;
        }

        public Sprint createSprint() {
            return new Sprint(name, startDate, endDate, sprintCompletedCards(), sprintCards());
        }

        private Collection<Card> sprintCards() {
            return cardsByLabelName(name);
        }

        private Collection<Card> sprintCompletedCards() {
            Collection<Card> completedCards = sprintCards();
            completedCards.retainAll(cardsByListName(completedList));

            Collection<Card> sprintCompletedCards = new ArrayList<>();
            for (Card card : completedCards) {
                for (Action action : card.getActions()) {
                    if (isActionDoneWithin(action, startDate, endDate)
                            && isMoveActionToList(action, completedList)) {
                        sprintCompletedCards.add(card);
                        break;
                    }
                }
            }
            return sprintCompletedCards;
        }

    }

}
