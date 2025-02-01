package ru.netology.moneytransfer2.transferlayer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс-с мапой для хранения кодов подтверждения к операциям
 * По скольку на момент написания данного решения с БД работ еще не делалось,
 * то было принято решение использовать мапу, как хранилище данных о кодах для операций
 * Для хранения используется ConcurrentHashMap и шаблон синглтон
 */
public class ConfirmationsMap {

    protected final ConcurrentHashMap<String, String> confirms = new ConcurrentHashMap<>();
    private static volatile ConfirmationsMap instance;

    /**
     * Метод возвращающий единый объект для класса, реализация синглтона
     *
     * @return ConfirmationsMap - возвращает экземпляр мапы с подтверждениями
     */
    public ConfirmationsMap getConfirmationsList() {
        ConfirmationsMap localInstance = instance;
        if (localInstance == null) {
            synchronized (ConfirmationsMap.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ConfirmationsMap();
                }
            }
        }
        return localInstance;
    }

    /**
     * Метод добавления кода подтверждения операции по ее ID
     *
     * @param operationId  - id операции
     * @param confirmation - sms код подтверждения для его дальнейшей проверки
     */
    public void addConfirm(String operationId, String confirmation) {
        confirms.put(operationId, confirmation);
    }

    /**
     * Метод для получения кода подтверждения, для дальнейшей сверки по id операции
     *
     * @param operationId - id операции
     * @return возвращается код подтверждения значение типа String
     */
    public String getConfirm(String operationId) {
        return confirms.get(operationId);
    }
}
