package ru.netology.moneytransfer2.transferlayer;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmationsMapTest {

    ConfirmationsMap map = Mockito.mock(ConfirmationsMap.class);

    @Test
    void getConfirmationsList() {
        map.getConfirmationsList();
        Mockito.verify(map, Mockito.times(1)).getConfirmationsList();
    }

    @Test
    void addConfirm() {
        map.addConfirm("test","test");
        Mockito.verify(map, Mockito.times(1)).addConfirm("test", "test");
    }

    @Test
    void getConfirm() {
        Mockito.when(map.getConfirm("test")).thenReturn("test");
        assertEquals("test", map.getConfirm("test"));
    }
}