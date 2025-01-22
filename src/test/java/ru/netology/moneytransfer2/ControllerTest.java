package ru.netology.moneytransfer2;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ControllerTest {

    @Test
    void transferProcessing() throws JSONException {
        Controller controller = Mockito.mock(Controller.class);
        TransferRequest transferRequest = Mockito.mock(TransferRequest.class);
        controller.transferProcessing(transferRequest);
        Mockito.verify(controller, Mockito.times(1)).transferProcessing(transferRequest);
    }

    @Test
    void confirmOperation() throws JSONException {
        Controller controller = Mockito.mock(Controller.class);
        ConfirmRequest confirmRequest = Mockito.mock(ConfirmRequest.class);
        controller.confirmOperation(confirmRequest);
        Mockito.verify(controller, Mockito.times(1)).confirmOperation(confirmRequest);

    }
}