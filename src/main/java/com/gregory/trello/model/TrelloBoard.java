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
    private final TrelloCardDeck cards;

    public TrelloBoard(Board board, List<TList> lists, List<Card> cards) {
        this.board = board;
        this.lists = new ArrayList<>();
        for (TList list : lists) {
            this.lists.add(new TrelloList(list));
        }
        this.cards = new TrelloCardDeck();
        for (Card card : cards) {
            this.cards.addCard(new TrelloCard(card));
        }
    }

    public TrelloCardDeck cards() {
        return cards;
    }

    public TrelloCardDeck cardsByLabelName(String labelName) {
        return cards.cardsByLabelName(labelName);
    }

    public TrelloCardDeck cardsByListName(String listName) {
        return cards.cardsByListName(listName);
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

    public TrelloCardDeck cardsAt(Date date) {
        List<TrelloCard> result = new ArrayList<>();
        for (TrelloCard card : cards) {
            if (card.isCreatedBefore(date)) {
                result.add(card);
            }
        }
        return new TrelloCardDeck(result);
    }

}
