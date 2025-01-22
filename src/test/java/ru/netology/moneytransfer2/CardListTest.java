package ru.netology.moneytransfer2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardListTest {
    CardList cardList = new CardList();

    @Test
    void addCard() {
        Card card = new Card("test", "test", "test");
        assertEquals(card, cardList.getCardList().getCard("test"));
    }

    @Test
    void cardExists() {
        Card card = new Card("test", "test", "test");
        Assertions.assertTrue(cardList.cardExists("test"));

    }

    @Test
    void cardValid() {
        Card card = new Card("test", "test", "test");
        assertTrue(cardList.cardValid("test", "test", "test"));
    }

    @Test
    void getCard() {
        Card card = new Card("test", "test", "test");
        assertEquals(card, cardList.getCard("test"));
    }

    @Test
    void getBalance() {
        Card card = new Card("test", "test", "test");
        card.fill("test", 1);
        Assertions.assertEquals(1, cardList.getBalance("test", "test"));

    }
}