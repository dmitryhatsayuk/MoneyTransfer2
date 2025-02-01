package ru.netology.moneytransfer2.transferlayer;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.netology.moneytransfer2.cardlayer.CardList;

/**
 * Класс содержит бизнес-логику проверки данных в запросе на перевод и верифицирует подтверждение операции
 */
@Log4j2
public class TransferLogic {
    ConfirmationsMap confirmationsList = new ConfirmationsMap();
    CardList cardList = new CardList();
    TransferRequest requestData;

    /**
     * Метод для парсинга данных из запроса на перевод, их проверки подготовки соответствующего ответа
     *
     * @param request - объект типа ConfirmRequest для парсинга
     * @return ResponseEntity
     * @see ConfirmRequest
     */
    public ResponseEntity<String> checkBeforeTransfer(TransferRequest request) throws JSONException {
        //создаем JSON объект для его наполнения и передачи внутри ответа
        JSONObject resp = new JSONObject();
        final String operationId = String.valueOf((int) (Math.random() * 123456789));

//Если карты получателя или карты отправителя нет в списке, либо карта отправителя не валидна, то выдаем код 400
        if (!cardList.cardExists(request.getCardFromNumber()) || !cardList.cardExists(request.getCardToNumber()) || !cardList.cardValid(request.getCardFromNumber(), request.getCardFromValidTill(), request.getCardFromCVV())) {
            resp.put("message", "Wrong card details");
            resp.put("id", operationId);
            log.error("Wrong card details. ID {}", operationId);
            return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(400));
        }
//Если карты в порядке (проверили выше), то передаем код 200 и "отправляем" код подтверждения операции в специальную мапу для этого/
        if (cardList.getCardList().getCard(request.getCardFromNumber()).canPay(request.getAmount().getCurrency(), request.getAmount().getValue())) {
            resp.put("operationId", operationId);
            // здесь должен быть метод отправки смс, содержимое которого дальше кидаем в мапу, что-то из RabbitMQ, но я про него пока мало знаю :)
            String confirmCode = "0000";
            confirmationsList.getConfirmationsList().addConfirm(operationId, confirmCode);
            requestData = request;
            log.info("Transaction pre-check OK for id {}. Secret Code SENT", operationId);
            return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(200));
        }
//во всех остальных случаях возвращаем 500, как прочую ошибку и готовим ответ
        resp.put("message", "Transfer error");
        resp.put("id", operationId);
        log.error("Transfer Error for operation{}", operationId);
        return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(500));

    }

    /**
     * Endpoint для парсинга данных из подтверждения на перевод
     *
     * @param confirmRequest - объект типа ConfirmRequest для парсинга
     * @return ResponseEntity
     * @see ConfirmRequest
     */
    public ResponseEntity<String> checkAfterTransfer(ConfirmRequest confirmRequest) throws JSONException {
        //создаем JSON объект для его наполнения и передачи внутри ответа
        JSONObject resp = new JSONObject();
        //если id операции найден (была операция с таким id) и код подтверждения передан верный, то проводим операции
        if (confirmationsList.getConfirmationsList().getConfirm(confirmRequest.getOperationId()) != null && confirmRequest.getCode().equals(confirmationsList.getConfirmationsList().getConfirm(confirmRequest.getOperationId()))) {
            resp.put("operationId", confirmRequest.getOperationId());
            if (cardList.getCard(requestData.getCardFromNumber()).commissionTransfer(requestData.getAmount().getCurrency(), requestData.getAmount().getValue()) && cardList.getCard(requestData.getCardToNumber()).fill(requestData.getAmount().getCurrency(), requestData.getAmount().getValue())) {
                log.info("Transaction compete successfully for ID {}", confirmRequest.getOperationId());
                log.info("Sender card balance {}", cardList.getBalance(requestData.getCardFromNumber(), requestData.getAmount().getCurrency()));
                log.info("Receiver card balance {}", cardList.getBalance(requestData.getCardToNumber(), requestData.getAmount().getCurrency()));
                return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(200));
            }
            //Если данные не верные, то отдаем ошибку 500
            else {
                resp.put("message", "Wrong operation details for ID " + confirmRequest.getOperationId());
                log.error(String.valueOf(resp));
                return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(500));
            }
        }
        //Во всех остальных случаях просто шлем 400, ибо прочие условия нам не известны:)
        else {
            resp.put("operationId", confirmRequest.getOperationId());
            log.error("Unknown error for operation {}", confirmRequest.getOperationId());
            return new ResponseEntity<>(resp.toString(), HttpStatusCode.valueOf(400));
        }
    }
}