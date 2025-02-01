package ru.netology.moneytransfer2.apilayer;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.moneytransfer2.transferlayer.ConfirmRequest;
import ru.netology.moneytransfer2.transferlayer.TransferLogic;
import ru.netology.moneytransfer2.transferlayer.TransferRequest;

class TransferLogicTest {


    @Test
    void checkBeforeTransfer() throws JSONException {
        TransferLogic transferLogic = Mockito.mock(TransferLogic.class);
        TransferRequest request = Mockito.mock(TransferRequest.class);
        transferLogic.checkBeforeTransfer(request);
        Mockito.verify(transferLogic, Mockito.times(1)).checkBeforeTransfer(request);
    }

    @Test
    void checkAfterTransfer() throws JSONException {
        TransferLogic transferLogic = Mockito.mock(TransferLogic.class);
        ConfirmRequest request = Mockito.mock(ConfirmRequest.class);
        transferLogic.checkAfterTransfer(request);
        Mockito.verify(transferLogic, Mockito.times(1)).checkAfterTransfer(request);
    }
}