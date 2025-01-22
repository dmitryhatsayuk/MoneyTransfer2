package ru.netology.moneytransfer2;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс-список карт.
 * По скольку на момент написания данного решения с БД работ еще не делалось,
 * то было принято решение использовать лист, как хранилище данных о картах
 */
@Log4j2
public class CardList {
    private final ConcurrentHashMap<String, Card> cards = new ConcurrentHashMap<>();
    private static volatile CardList instance;

    /**
     * Метод-синглтон возвращающий список карт
     *
     * @return - CardList возвращает список карт
     */
    protected CardList getCardList() {
        CardList localInstance = instance;
        if (localInstance == null) {
            synchronized (CardList.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CardList();
                }
            }
        }
        return localInstance;
    }

    /**
     * Метод для добавления карты в список
     *
     * @param card - объект типа Card
     * @see Card
     */
    public void addCard(Card card) {
        getCardList().cards.put(card.getCardNumber(), card);
        log.info("Added card: " + card.getCardNumber());
    }

    /**
     * Метод проверки номера карты
     *
     * @param cardNumber- номер карты
     * @return - Boolean возвращает булево значение, true если карта существует
     */
    public boolean cardExists(String cardNumber) {
        if (getCardList().cards.containsKey(cardNumber)) {
            log.info("card exist {}", getCardList().cards.containsKey(cardNumber));
            return true;
        } else {
            log.error("Card NOT exist {}", getCardList().cards.containsKey(cardNumber));
            return false;
        }
    }

    /**
     * Метод проверки карты на валидность
     *
     * @param cardNumber    - номер карты
     * @param cardValidTill - срок действия карты
     * @param cardCVV-      CVV код карты
     * @return - возвращает булево значение, true если карта валидна
     */
    public boolean cardValid(String cardNumber, String cardValidTill, String cardCVV) {
        if (getCard(cardNumber).getCardCVV().equals(cardCVV) && getCard(cardNumber).getCardValidTill().equals(cardValidTill)) {
            log.info("CARD VALID {}", getCard(cardNumber).getCardCVV().equals(cardCVV) && getCard(cardNumber).getCardValidTill().equals(cardValidTill));
            return true;
        } else {
            log.error("Card not valid ");
            return false;
        }

    }

    /**
     * Геттер для получения карты из списка
     *
     * @param cardNumber - номер карты
     * @return Card - возвращает объект типа Card
     * @see Card
     */
    Card getCard(String cardNumber) {
        return getCardList().cards.get(cardNumber);
    }

    /**
     * Метод проверки баланса карты по конкретной валюте
     *
     * @param currency   - валюта
     * @param cardNumber - номер карты
     * @return Double - возвращает баланс валютного счета карты
     */
    public Double getBalance(String cardNumber, String currency) {
        return getCardList().getCard(cardNumber).getAccounts().get(currency);
    }
}