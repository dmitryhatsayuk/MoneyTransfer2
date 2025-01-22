package ru.netology.moneytransfer2;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс для парсинга http-запросов на подтверждение платежа
 */
@Getter
@Setter
public class ConfirmRequest {


    /**
     * Поле секретного кода
     */
    private String code;
    /**
     * ID операции
     */
    private String operationId;
}
