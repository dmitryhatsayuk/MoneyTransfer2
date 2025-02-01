package ru.netology.moneytransfer2.cardlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CardTest {
    Card card = new Card("test", "test", "test");

    @Test
    void fill() {
        Assertions.assertTrue(card.fill("test", 1111));

    }

    @Test
    void transfer() {
        card.fill("test", 1111);
        Assertions.assertTrue(card.transfer("test", 1));
    }

    @Test
    void canPay() {
        card.fill("test", 1111);
        Assertions.assertTrue(card.canPay("test", 1));
    }

    @Test
    void getCardNumber() {
        Assertions.assertEquals("test", card.getCardNumber());
    }

    @Test
    void getCardValidTill() {
        Assertions.assertEquals("test", card.getCardValidTill());
    }

    @Test
    void getCardCVV() {
        Assertions.assertEquals("test", card.getCardCVV());
    }

    @Test
    void getAccounts() {
        Card newCard = new Card("test", "test", "test");
        newCard.fill("test", 1);
        Assertions.assertEquals(1, newCard.getAccounts().get("test"));
    }


    @Test
    void commissionTransfer() {
        card.fill("test", 1111);
        Assertions.assertTrue(card.transfer("test", 1));
    }
}