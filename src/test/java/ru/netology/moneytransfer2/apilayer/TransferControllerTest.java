package ru.netology.moneytransfer2.apilayer;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.moneytransfer2.transferlayer.ConfirmRequest;
import ru.netology.moneytransfer2.transferlayer.TransferRequest;

class TransferControllerTest {

    @Test
    void transferProcessing() throws JSONException {
        TransferRequest transferRequest = Mockito.mock(TransferRequest.class);
        TransferController transferController = Mockito.mock(TransferController.class);
        transferController.transferProcessing(transferRequest);
        Mockito.verify(transferController).transferProcessing(transferRequest);
    }

    @Test
    void confirmOperation() throws JSONException {
        TransferController transferController = Mockito.mock(TransferController.class);
        ConfirmRequest confirmRequest = Mockito.mock(ConfirmRequest.class);
        transferController.confirmOperation(confirmRequest);
        Mockito.verify(transferController).confirmOperation(confirmRequest);
    }
}