package com.gregory;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;

import java.util.ArrayList;
import java.util.Collection;

public final class TrelloUtils {

    private TrelloUtils() {
    }

    public static Collection<Card> filterCardsByLabelName(Collection<Card> cards, String name) {
        Collection<Card> result = new ArrayList<>();
        for (Card card : cards) {
            for (Label label : card.getLabels()) {
                String labelName = label.getName();
                if (label != null && labelName.equals(name)) {
                    result.add(card);
                }
            }
        }
        return result;
    }

    public static int extractCardPointsFromName(Card card) {
        return Integer.parseInt(card.getName().replaceFirst("\\((\\d+)\\).*", "$1"));
    }

}
