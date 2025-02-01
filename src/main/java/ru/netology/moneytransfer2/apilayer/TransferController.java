package ru.netology.moneytransfer2.apilayer;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.moneytransfer2.transferlayer.ConfirmRequest;
import ru.netology.moneytransfer2.transferlayer.TransferLogic;
import ru.netology.moneytransfer2.transferlayer.TransferRequest;

/**
 * Контроллер с двумя Endpoint /transfer и /confirmOperation
 */
@Log4j2
@RestController
public class TransferController {
    /**
     * Экземпляр бизнес-логики проверки запроса на перевод и его подтверждения
     */
    private final TransferLogic transferLogic = new TransferLogic();
    /**
     * Данные из запроса на перевод для передачи их между методами
     */
    private TransferRequest requestData;

    /**
     * Endpoint для чтения запроса на перевод и выполнения операции проверки данных и выдачи ответа о корректности данных
     *
     * @param transferRequest - объект типа TransferRequest для парсинга
     * @return ResponseEntity
     * @see TransferRequest
     */
    @ResponseBody
    @PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> transferProcessing(@RequestBody TransferRequest transferRequest) throws JSONException {
        log.info("Transfer request received");

        return transferLogic.checkBeforeTransfer(transferRequest);
    }

    /**
     * Endpoint для проведения операции подтверждения перевода и выдачи соответствующего ответа
     *
     * @param confirmRequest - объект типа ConfirmRequest для парсинга
     * @return ResponseEntity
     * @see ConfirmRequest
     */
    @ResponseBody
    @PostMapping(path = "/confirmOperation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> confirmOperation(@RequestBody ConfirmRequest confirmRequest) throws JSONException {
        log.info("Confirm request received");
        return transferLogic.checkAfterTransfer(confirmRequest);

    }
}
