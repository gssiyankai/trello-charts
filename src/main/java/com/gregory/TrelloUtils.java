package com.gregory;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;
import com.julienvey.trello.domain.TList;

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

    public static Collection<TList> filterListsByName(Collection<TList> lists, String name) {
        Collection<TList> result = new ArrayList<>();
        for (TList list : lists) {
            String listName = list.getName();
            if (listName != null && listName.equals(name)) {
                result.add(list);
            }
        }
        return result;
    }

    public static Collection<Card> filterCardsByListId(Collection<Card> cards, String id) {
        Collection<Card> result = new ArrayList<>();
        for (Card card : cards) {
            String listId = card.getIdList();
            if (listId != null && listId.equals(id)) {
                result.add(card);
            }
        }
        return result;
    }

    public static int cardsPoints(Collection<Card> cards) {
        int points = 0;
        for (Card card : cards) {
            points += extractCardPointsFromName(card);
        }
        return points;
    }

    public static int extractCardPointsFromName(Card card) {
        return Integer.parseInt(card.getName().replaceFirst("\\((\\d+)\\).*", "$1"));
    }

}
