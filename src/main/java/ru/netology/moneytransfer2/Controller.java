package ru.netology.moneytransfer2;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер с двумя Endpoint /transfer и /confirmOperation
 */
@Log4j2
@RestController
public class Controller {
    /**
     * Поле для доступа к единому списку карт
     */
    private final CardList cardList = new CardList();//не боимся, там где-то синглтон
    /**
     * Автоматически генерируемый ID операции
     */
    private final int operationId = (int) (Math.random() * 123456789);
    /**
     * Поле с разрешением на операцию, по умолчанию false
     */
    private boolean operationAllowed = false;
    /**
     * Данные из запроса на перевод для передачи их между методами
     */
    private TransferRequest requestData;

    /**
     * Endpoint для парсинга данных из запроса на перевод
     *
     * @param transferRequest - объект типа TransferRequest для парсинга
     * @see TransferRequest
     */
    @ResponseBody
    @PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> transferProcessing(@RequestBody TransferRequest transferRequest) throws JSONException {
        JSONObject resp = new JSONObject();
        requestData = transferRequest;
//Если карты получателя или карты отправителя нет в списке, либо карта отправителя не валидна, то выдаем код 400
        if (!cardList.cardExists(requestData.getCardFromNumber()) || !cardList.cardExists(requestData.getCardToNumber()) || !cardList.cardValid(requestData.getCardFromNumber(), requestData.getCardFromValidTill(), requestData.getCardFromCVV())) {
            resp.put("message", "Wrong card details");
            resp.put("id", operationId);
            log.error(String.valueOf(resp));

            return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(400));
        }
//Если карты в порядке (проверили выше), то передаем код 200 и включаем флаг о том, что все готово для перевода*/
        if (cardList.getCardList().getCard(transferRequest.getCardFromNumber()).canPay(transferRequest.getAmount().getCurrency(), transferRequest.getAmount().getValue())) {
            operationAllowed = true;
            resp.put("operationId", operationId);
            log.info("Transaction allowed");
            return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(200));
        }
//во всех остальных случаях возвращаем 500, как прочую ошибку
        resp.put("message", "Transfer error");
        resp.put("id", operationId);
        log.error(String.valueOf(resp));
        return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(500));

    }

    /**
     * Endpoint для парсинга данных из запроса на подтверждение платежа
     *
     * @param confirmRequest - объект типа ConfirmRequest для парсинга
     * @see ConfirmRequest
     */
    //Если в запросе приходит корректный ID и секретный код, то проводим операции по картам и отправляем код 200
    @ResponseBody
    @PostMapping(path = "/confirmOperation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> confirmOperation(@RequestBody ConfirmRequest confirmRequest) throws JSONException {
        JSONObject resp = new JSONObject();
        if (Integer.parseInt(confirmRequest.getOperationId()) == operationId && operationAllowed) {
            resp.put("operationId", operationId);
            if (cardList.getCard(requestData.getCardFromNumber()).commissionTransfer(requestData.getAmount().getCurrency(), requestData.getAmount().getValue()) && cardList.getCard(requestData.getCardToNumber()).fill(requestData.getAmount().getCurrency(), requestData.getAmount().getValue())) {
                log.info("Transaction compete successfully");
                log.info("Sender card balance {}", cardList.getBalance(requestData.getCardFromNumber(), requestData.getAmount().getCurrency()));
                log.info("Receiver card balance {}", cardList.getBalance(requestData.getCardToNumber(), requestData.getAmount().getCurrency()));
                return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(200));
            }
            //Если данные не верные, то отдаем ошибку 500
            else {
                resp.put("message", "Wrong operation details");
                log.error(String.valueOf(resp));
                return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(500));
            }
        }
        //Во всех остальных случаях просто шлем 400, ибо прочие условия нам не известны:)
        else {
            resp.put("operationId", confirmRequest.getOperationId());
            log.error("Unknown error");
            return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(400));
        }

    }
}
