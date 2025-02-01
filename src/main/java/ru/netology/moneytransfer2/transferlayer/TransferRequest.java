package ru.netology.moneytransfer2.transferlayer;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс для парсинга http-запросов на перевод средств
 */
@Setter
@Getter
public class TransferRequest {
    /**
     * Номер карты отправителя
     */
    private String cardFromNumber;
    /**
     * Срок действия карты отправителя
     */
    private String cardFromValidTill;
    /**
     * CVV код карты отправителя
     */
    private String cardFromCVV;
    /**
     * Номер карты получателя
     */
    private String cardToNumber;
    /**
     * Поле с объектом Amount, включающий в себя сумму и валюту перевода
     */
    private Amount amount;


    /**
     * Вложенный класс для парсинга суммы и валюты платежа http-запросов на подтверждение платежа
     *
     * @see TransferRequest
     */
    @Setter
    @Getter
    public static class Amount {
        /**
         * Номер карты получателя
         */
        private Double value;
        /**
         * Номер карты получателя
         */
        private String currency;

        /**
         * Вложенный класс платежа с валютой и суммой
         *
         * @param value    - сумма платежа
         * @param currency -валюта платежа
         */
        Amount(int value, String currency) {
            // сумма приходит от FE без дробной части в виде суммы x100
            this.value = (double) (value / 100);
            this.currency = currency;
        }

    }
}
