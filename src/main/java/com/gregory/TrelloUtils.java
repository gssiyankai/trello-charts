package com.gregory;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;
import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.TrelloImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.gregory.Settings.settings;

public final class TrelloUtils {

    private static final Trello TRELLO = new TrelloImpl(settings().trelloKey(), settings().trelloAccessToken());
    private static final List<TList> BOARD_LISTS = TRELLO.getBoardLists(settings().trelloBoardId());
    private static final List<Card> BOARD_CARDS = TRELLO.getBoardCards(settings().trelloBoardId());

    private TrelloUtils() {
    }

    public static Collection<Card> cards() {
        return BOARD_CARDS;
    }

    public static Collection<Card> cardsByLabelName(String name) {
        Collection<Card> result = new ArrayList<>();
        for (Card card : cards()) {
            for (Label label : card.getLabels()) {
                String labelName = label.getName();
                if (label != null && labelName.equals(name)) {
                    result.add(card);
                }
            }
        }
        return result;
    }

    public static Collection<Card> cardsByListName(String name) {
        Collection<Card> result = new ArrayList<>();
        for (Card card : cards()) {
            String listId = card.getIdList();
            for (TList list : listsByName(name)) {
                if (list.getId().equals(listId)) {
                    result.add(card);
                    break;
                }
            }
        }
        return result;
    }

    public static Collection<TList> lists() {
        return BOARD_LISTS;
    }

    public static Collection<TList> listsByName(String name) {
        Collection<TList> result = new ArrayList<>();
        for (TList list : lists()) {
            String listName = list.getName();
            if (listName != null && listName.equals(name)) {
                result.add(list);
            }
        }
        return result;
    }

    public static int cardsPoints(Collection<Card> cards) {
        int points = 0;
        for (Card card : cards) {
            points += cardPoint(card);
        }
        return points;
    }

    public static int cardPoint(Card card) {
        return Integer.parseInt(card.getName().replaceFirst("\\((\\d+)\\).*", "$1"));
    }

    public static boolean isActionDoneWithin(Action action, Date start, Date end) {
        Date actionDate = action.getDate();
        return actionDate.compareTo(start) >= 0  && actionDate.compareTo(end) <= 0;
    }

    public static boolean isUpdateAction(Action action) {
        return "updateCard".equals(action.getType());
    }

    public static boolean isMoveActionToList(Action action, String toList) {
        return isUpdateAction(action) && toList.equals(action.getData().getListAfter().getName());
    }

}
