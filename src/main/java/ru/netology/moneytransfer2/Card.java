package ru.netology.moneytransfer2;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ConcurrentHashMap;


@Log4j2
@Getter
public class Card {
    private final String cardNumber;
    private final String cardValidTill;
    private final String cardCVV;
    private ConcurrentHashMap<String, Double> accounts;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     * @param cardNumber    - номер карты
     * @param cardValidTill - срок действия карты
     * @param cardCVV       - CVV-код карты
     * @param accounts      - валютные счета имеющиеся на карте
     * @see CardList#addCard(Card)
     */
    Card(String cardNumber, String cardValidTill, String cardCVV) {
        CardList cardList = new CardList();
        this.cardNumber = cardNumber;
        this.cardValidTill = cardValidTill;
        this.cardCVV = cardCVV;
        this.accounts = new ConcurrentHashMap<>();
        cardList.addCard(this);
    }


    /**
     * Метод пополнения карты
     *
     * @param currency - валюта
     * @param amount   - сумма пополнения
     * @return - возвращает булево значение, true если операция успешна
     */
    public boolean fill(String currency, double amount) {
        if (amount < 0) {
            log.error("amount < 0");
            return false;
        }
//если такая валюта уже есть, то увеличиваем ее баланс
        else if (accounts.containsKey(currency)) {
            accounts.put(currency, accounts.get(currency) + amount);
            log.info("FILL SUCCESS for {} {}", amount, currency);
            return true;
        }
//если валюты нет, то создаем ее с балансом суммы пополнения
        else {
            accounts.put(currency, amount);
            log.info("FILL SUCCESS for {} of new currency {}", amount, currency);
            return true;
        }
    }

    /**
     * Метод для перевода с карты
     *
     * @param currency - валюта
     * @param amount   - сумма пополнения
     * @return - возвращает булево значение, true если операция успешна
     */
    public boolean transfer(String currency, double amount) {
        if (amount < 0) {
            log.error("amount < 0");
            return false;
        }
        //Если с карты можно оплачивать и денег достаточно для платежа, то переводим.
        // Проверяем на случай если на момент транзакции средств уже не было
        if (canPay(currency, amount)) {
            accounts.put(currency, accounts.get(currency) - amount);
            log.info("Transfer complete {}", amount);
            return true;
        }
        log.error("cant TRANSFER");
        return false;
    }

    /**
     * Метод для перевода с карты с комиссией
     *
     * @param currency - валюта
     * @param amount   - сумма пополнения
     * @return - возвращает булево значение, true если операция успешна
     */
    public boolean commissionTransfer(String currency, double amount) {
        //Размер комиссии просто вынесен в константу, поскольку FE ее не передает
        double commission = 0.01;
        if (amount < 0) {
            log.error("amount < 0");
            return false;
        }
        //Если с карты можно оплачивать и денег достаточно для платежа, то переводим.
        // Проверяем на случай если на момент транзакции средств уже не было
        if (canPay(currency, amount * (1 + commission))) {
            accounts.put(currency, accounts.get(currency) - amount * (1 + commission));
            log.info("Transfer complete with commission {} {}", (amount * commission), currency);
            return true;
        }
        log.error("cant TRANSFER");
        return false;
    }

    /**
     * Метод проверки, возможен ли перевод данного количества валюты с данной карты
     *
     * @param currency - валюта
     * @param amount   - сумма пополнения
     * @return - возвращает булево значение, true если перевод возможен
     */
    public boolean canPay(String currency, double amount) {
        if (amount < 0) {
            log.error("amount < 0");

        }
        //Если нет требуемой валюты
        if (!accounts.containsKey(currency)) {
            log.error("cant find this currency. Transfer required {} Account list {}", currency, accounts.get(currency));
            return false;
        }
        //Если недостаточно валюты для платежа
        if (accounts.get(currency) < amount) {
            log.error("Not enough money on account. Need {} {} on balance {} {}", amount, currency, accounts.get(currency).toString(), currency);
            return false;
        }
        return true;
    }

}
