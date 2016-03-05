package com.gregory.trello.model;

import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.TList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class TrelloBoard {

    private final Board board;
    private final List<TrelloList> lists;
    private final List<TrelloCard> cards;

    public TrelloBoard(Board board, List<TList> lists, List<Card> cards) {
        this.board = board;
        this.lists = new ArrayList<>();
        for (TList list : lists) {
            this.lists.add(new TrelloList(list));
        }
        this.cards = new ArrayList<>();
        for (Card card : cards) {
            this.cards.add(new TrelloCard(card));
        }
    }

    public List<TrelloCard> cardsByLabelName(String labelName) {
        List<TrelloCard> result = new ArrayList<>();
        for (TrelloCard card : cards) {
            for (TrelloLabel label : card.labels()) {
                if (labelName.equals(label.name())) {
                    result.add(card);
                }
            }
        }
        return result;
    }

    public List<TrelloCard> cardsByListName(String listName) {
        List<TrelloCard> result = new ArrayList<>();
        for (TrelloCard card : cards) {
            String listId = card.listId();
            for (TrelloList list : listsByName(listName)) {
                if (list.id().equals(listId)) {
                    result.add(card);
                    break;
                }
            }
        }
        return result;
    }

    public List<TrelloList> listsByName(String listName) {
        List<TrelloList> result = new ArrayList<>();
        for (TrelloList list : lists) {
            if (listName.equals(list.name())) {
                result.add(list);
            }
        }
        return result;
    }

    public TrelloList listById(String listId) {
        TrelloList result = null;
        for (TrelloList list : lists) {
            if (listId.equals(list.id())) {
                result = list;
                break;
            }
        }
        return result;
    }

    public List<TrelloCard> cardsAt(Date date) {
        List<TrelloCard> result = new ArrayList<>();
        for (TrelloCard card : cards) {
            if (card.isCreatedBefore(date)) {
                result.add(card);
            }
        }
        return result;
    }

}
